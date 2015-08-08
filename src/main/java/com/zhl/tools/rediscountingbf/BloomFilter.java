package com.zhl.tools.rediscountingbf;

import java.util.Date;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

public class BloomFilter {

	static JedisPool pool;
	static Jedis jedis;
	// static int string_size = 1024 * 1024;
	static int string_size = 1024 * 128;
	static int counter_size;
	static int counter_cap;
	static long m;
	static int no_function;

	static long s_overflow = 0;
	static long s_collision = 0;
	static long s_lock = 0;
	static long[] s_bits;
	static HashCalculator hashCalculator;

	public void initialize(String host) {
		if(jedis==null)
			jedis = new Jedis(host);
	}

	void close() {
		jedis.close();
	}
	
	void clear() {
		jedis.flushDB();
	}

	public void initialize(long m, int k, int csize) {
		
		if(hashCalculator==null)
		{
			this.m = m;
			no_function = k;
			counter_size = csize;
			counter_cap = 1 << counter_size;
			s_bits = new long[counter_cap];
			hashCalculator=new HashCalculator();
			hashCalculator.initialize(k, m);
		}
		
	}

	void get_bit_transaction(String[] keys, int[] offsets, Transaction trans) {
		for (int j = 0; j < no_function; j++) {
			for (int i = 0; i < counter_size; i++) {
				trans.getbit("" + keys[j], offsets[j] + i);
			}
		}
	}

	int get_bit(long a) {
		long b = a * counter_size;
		int key = (int) (b / string_size);
		int offset = (int) (b % string_size);
		return get_from_redis(key, offset);
	}

	int get_from_redis(int key, int offset) {
		int r = 0;
		for (int i = 0; i < counter_size; i++) {
			r = r * 2;
			if (jedis.getbit("" + key, offset + i))
				r++;
		}
		return r;
	}

	void set_bit_transaction(String[] keys, int[] offsets, int[] values,
			Transaction trans) {
		int value;
		for (int i = 0; i < no_function; i++) {
			value = values[i] % counter_cap;

			s_bits[value]++;
			for (int j = counter_size - 1; j >= 0; j--) {
				if ((value & 1) == 0)
					trans.setbit("" + keys[i], offsets[i] + j, false);
				else
					trans.setbit("" + keys[i], offsets[i] + j, true);
				value = value >>> 1;

			}
		}
	}

	void set_bit(long a, int value) {
		value = value % counter_cap;
		long b = a * counter_size;
		int key = (int) (b / string_size);
		int offset = (int) (b % string_size);
		set_to_redis(key, offset, value);
	}

	void set_to_redis(int key, int offset, int value) {
		s_bits[value]++;
		for (int i = counter_size - 1; i >= 0; i--) {
			if (value % 2 == 0)
				jedis.setbit("" + key, offset + i, false);
			else
				jedis.setbit("" + key, offset + i, true);
			value /= 2;
		}
	}

	boolean existing(String key) {
		long[] keys = hashCalculator.calculate(key);
		int[] values = new int[no_function];
		boolean exist = true;
		for (int i = 0; i < no_function; i++) {
			values[i] = get_bit(keys[i]);
			if (values[i] == 0)
				exist = false;
		}

		return exist;
	}

	boolean remove(String key) {
		long[] keys = hashCalculator.calculate(key);
		int[] values = new int[no_function];
		boolean exist = true;
		for (int i = 0; i < no_function; i++) {
			values[i] = get_bit(keys[i]);
			if (values[i] == 0) {
				exist = false;
				break;
			}
		}

		if (exist) {
			for (int i = 0; i < no_function; i++) {
				set_bit(keys[i], values[i] - 1);
			}
		}

		return exist;
	}

	boolean putIfNotExisting(String key) {
		// long t1 = new Date().getTime();
		boolean exist = true;

		long[] dkeys = hashCalculator.calculate(key);

		String[] keys = new String[no_function];

		int[] offsets = new int[no_function];

		for (int j = 0; j < no_function; j++) {
			long b = dkeys[j] * counter_size;
			keys[j] = "" + (int) (b / string_size);
			offsets[j] = (int) (b % string_size);
		}


		String[] tt = new String[no_function * 2];

		for (int i = 0; i < no_function; i++) {
			tt[i << 1] = "T" + dkeys[i];
			tt[(i << 1) | 1] = "1";
		}

		try{

			Transaction trans = jedis.multi();

			trans.msetnx(tt);

			// System.out.println("Hash functions Time: "+(new Date().getTime() -
			// t1));
			// t1 = new Date().getTime();

			get_bit_transaction(keys, offsets, trans);

			List<Object> response = trans.exec();

			if ((Long) (response.get(0)) == 0) {
				s_lock++;
				return exist;
			}

			int index = 1;

			int[] values = new int[no_function];

			for (int i = 0; i < no_function; i++) {

				values[i] = 0;

				for (int j = 0; j < counter_size; j++) {
					values[i] = values[i] << 1;

					if ((Boolean) (response.get(index++)))
						values[i]++;

				}

				if (values[i] == 0)
					exist = false;

				if (++values[i] == counter_cap) {
					s_overflow++;
					exist = true;
					break;
				}
			}

			trans = jedis.multi();

			if (!exist) {
				set_bit_transaction(keys, offsets, values, trans);
			}

			for (int i = 0; i < no_function; i++)
				trans.del("T" + dkeys[i]);

			trans.exec();
		}
		catch(Exception e)
		{
			for (int i = 0; i < no_function; i++)
				jedis.del("T" + dkeys[i]);

			return true;
		}

		// System.out.println("Redis Time: "+(new Date().getTime() - t1));
		return exist;
	}
	
	static String charString = "0123456789abcdefghijklmnopqrstuvwxyz";
	static String generate_new() {
		char[] s = new char[13];
		s[0] = 'z';
		for (int i = 1; i < 13; i++) {
			s[i] = charString.charAt((int) (Math.random() * 36));
		}
		// System.out.println(new String(s));
		return new String(s);
	}
	
	public String request(String s) {
		if(s.length()!=13)
			s = generate_new();
		while (putIfNotExisting(s)) {
			s_collision++;
			s = generate_new();
		}
		return s;
	}

	void select_parameters(HashCalculator hc)
	{
		for(int i=0;i<20;i++)
		{
			long a=(long) (Math.random()*(hc.p-1));
			long b=(long) (Math.random()*(hc.p-1))+1;
			System.out.println(""+a+"\t"+b);
		}
	}
	

}
