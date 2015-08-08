package com.zhl.tools.rediscountingbf;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;

public class HashCalculator {

	int numBase = 36;
	int middle_string=7;
	int remaining=6;
	int total=31;
	int left=total-remaining;
//	long p=1200000041;
	long p=9600000001l;
	long m;
	int no_function=0;
	ArrayList<FunctionParameters> parameters=new ArrayList<FunctionParameters>();
	
	class FunctionParameters{
		long a;
		long b;
		public FunctionParameters(long a, long b) {
			super();
			this.a = a;
			this.b = b;
		}
	}
	
	void initialize(int k,long m)
	{
		this.m=m;
		no_function=k;			
		read_parameters_from_file("HashParameter.tsv", no_function);
	}
	
	void read_parameters_from_file(String filename, int sum)
	{
		try {
			InputStream fin = HashCalculator.class.getClassLoader().getResourceAsStream(filename);

			BufferedReader br = new BufferedReader(new InputStreamReader(fin));
			String line;
			int k=0;
			while ((line = br.readLine())!=null) {
				String[] cc=line.split("\t");
				if(cc.length!=2)
					continue;
				parameters.add(new FunctionParameters(Long.parseLong(cc[0]),Long.parseLong(cc[1])));
				k++;
				if(k==sum)
					break;
			}
			fin.close();
		} catch (Exception e) {		
			e.printStackTrace();
		}
		
	}
	
	long hash(String s, FunctionParameters fp, long m)
	{
		BigInteger k=new BigInteger(s,numBase);
		return k.multiply(new BigInteger(""+fp.a))
		.add(new BigInteger(""+fp.b))
		.mod(new BigInteger(""+p))
		.mod(new BigInteger(""+m))
		.longValue();
	}
	long hash(BigInteger k, FunctionParameters fp, long m)
	{
		return k.multiply(new BigInteger(""+fp.a))
		.add(new BigInteger(""+fp.b))
		.mod(new BigInteger(""+p))
		.mod(new BigInteger(""+m))
		.longValue();
	}

	long f1(long k, double A, long m)
	{
		return (1);
	}
	
	long convert_to_long(String s)
	{
		long result;
		long a=Long.valueOf(s.substring(middle_string), numBase);
		long b=Long.valueOf(s.substring(0, middle_string), numBase);
		long mask=(1<<remaining)-1;
		long r1=(a&mask)^(a>>>remaining);
		long r2=(b&mask<<left)^(b>>>remaining);
		result=r1^r2;
		return result;
	}
	
	long[] calculate(BigInteger s)
	{
		long[] results=new long[no_function];
		
		for(int i=0;i<parameters.size();i++)
		{
			FunctionParameters fp=parameters.get(i);
			results[i]=hash(s,fp,m);
		}
		
		return results;
	}
	
	long[] calculate2(BigInteger s)
	{
		long[] results=new long[no_function];
		for(int i=0;i<2;i++)
		{
			FunctionParameters fp=parameters.get(i);
			results[i]=hash(s,fp,m);
		}
		for(int i=2;i<no_function;i++)
		{
			results[i]=results[0]+i*results[1];
		}
		return results;
	}
	long[] calculate2(String s)
	{
		return calculate2(new BigInteger(s, numBase));
	}
	long[] calculate(String s)
	{
		return calculate(new BigInteger(s, numBase));
	}
	
	
}
