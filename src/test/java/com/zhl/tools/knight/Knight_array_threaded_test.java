package com.zhl.tools.knight;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**Tester class for Knight_array_threaded
 * @author huizhang
 *
 */
public class Knight_array_threaded_test {

	Knight_array_threaded tester;
	List<int[][]> result;
	HashSet<String> hs=new HashSet<String>();
	String s;

	@Before
	public void setUp() throws Exception {
		tester=new Knight_array_threaded();
	}
	
	/**Transfer a n*n array to String
	 * @param a array
	 * @return String as ,a[0],a[1],...
	 */
	String arrayToString(int[][] a){
		StringBuffer sb=new StringBuffer();
		int n=a.length;
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++)
				sb.append(","+a[i][j]);
		return sb.toString();
	}
	
	/**
	 * @param map
	 * @return true if map is a closed tour of knight
	 */
	boolean is_closed_tour(int[][] map){
		int n=map.length;
		int size=n*n;
		int i=0;
		int x=0,y=0;
		boolean good;
		while(i<size)
		{			
			i++;
			List<Step> next=Worker_utils.getNext(x, y , null);
			good=false;
			for(Step s: next)
			{
				if(map[s.getX()][s.getY()]==i)
				{
					good=true;
					x=s.getX();
					y=s.getY();
					break;
				}
			}
			if(!good)
				return false;
		}
		return true;
	}

	/**test function to get results
	 * @param limit number of requested result
	 * @param expectedNumber
	 */
	public void testToLimit(int limit, int expectedNumber){
		result=tester.calculate(limit);
		assertEquals(expectedNumber, result.size());
		
		for(int[][] a: result)
		{
			assert(is_closed_tour(a));
			s=arrayToString(a);
			assert(!hs.contains(s));
			hs.add(s);
		}
		
	}
	
	@Test
	public void test() {

		hs.clear();
		//set up the board of 5*5
		tester.setUp(5);
		result=tester.calculate(0);
		assertEquals(0, result.size());

		hs.clear();
		//set up the board of 6*6, total number of results is expected to be 9862*2
		tester.setUp(6);
		testToLimit(1,1);
		testToLimit(20,20);
		testToLimit(300,300);
		testToLimit(4000,4000);
		testToLimit(10000-4321,10000-4321);
		testToLimit(9862*2-10000-5, 9862*2-10000-5);
		testToLimit(0, 5);
		
	}

}
