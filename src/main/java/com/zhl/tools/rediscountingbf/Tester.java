package com.zhl.tools.rediscountingbf;

import java.util.Date;

public class Tester {

	BloomFilter bf;
	
	public Tester(BloomFilter bf) {
		super();
		this.bf = bf;
	}
	
	void test(int n) {
		String s;
		long t1 = new Date().getTime();
		int chunk = n / 10;
		for (int i = 0; i < n; i++) {
			s = bf.generate_new();// for testing
			bf.request(s);

			if (i % chunk == 0) {
				System.out.println((new Date().getTime() - t1) + "\t" + i);
				t1 = new Date().getTime();
			}

			// long t1 = new Date().getTime();
			// System.out.println(s+"\t"+request(s)+"\t"+(new Date().getTime() -
			// t1));
			// System.out.println("-------------------------");
		}
	}
	
	public static void main(String[] args) {

		BloomFilter bf = new BloomFilter();
		// initialize("savegave.corp.ne1.yahoo.com");
		bf.initialize("localhost");
		// initialize(1200000000, 7, 4);
		bf.initialize(9600000000l, 7, 4);
		
		Tester tester=new Tester(bf);
		tester.test( 1000000);

		System.out.println("Over flow: " + bf.s_overflow);
		System.out.println("Collision: " + bf.s_collision);
		System.out.println("Lock     : " + bf.s_lock);
		for (int i = 1; i < bf.counter_cap; i++) {
			if(bf.s_bits[i]>0)
				System.out.println("BF bits " + i + " : " + bf.s_bits[i]);
		}
		bf.close();
		System.out.println("DONE!!!");
		// clear();
	}
}
