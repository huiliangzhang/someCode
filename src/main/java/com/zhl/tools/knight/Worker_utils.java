package com.zhl.tools.knight;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/** class containing utility functions
 * @author huizhang
 *
 */
public class Worker_utils {

	/** get all valid next step
	 * @param x current position
	 * @param y
	 * @param map current map, put null if not needed
	 * @return valid steps in a list List<Step>
	 */
	public static List<Step> getNext(int x, int y, int[][] map)
	{
		ArrayList<Step> result=new ArrayList<Step>();
		addNext(x,y, 2, 1, result, map);
		addNext(x,y, 1, 2, result, map);
		addNext(x,y, -1, 2, result, map);
		addNext(x,y, -2, 1, result, map);
		addNext(x,y, -2, -1, result, map);
		addNext(x,y, -1, -2, result, map);
		addNext(x,y, 1, -2, result, map);
		addNext(x,y, 2, -1, result, map);
		
		return result;
	}

	/** if new position is valid, add new step to list result
	 * @param x current position
	 * @param y
	 * @param offsetx offset to new position
	 * @param offsety
	 * @param result result list
	 * @param map existing map, put null if not needed
	 */
	public static void addNext(int x, int y, int offsetx, int offsety, List<Step> result, int[][] map)
	{
		x+=offsetx;
		if(!isValid(x, map.length))
			return;
		y+=offsety;
		if(!isValid(y, map.length))
			return;
		if((map!=null)&&(map[x][y]!=0))
			return;
		result.add(new Step(x,y));
	}
	
	/** judge valid index in array with length n
	 * @param i 
	 * @param n
	 * @return true if 0<=i<n
	 */
	public static boolean isValid(int i, int n)
	{
		if((i<0)||(i>=n))
			return false;
		return true;
	}
	
	/**Deep copy of n*n int array
	 * @param original array
	 * @return coped array
	 */
	public static int[][] copy_map(int[][] original){
		int n=original.length;
		int[][] new_array=new int[n][n];
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++)
				new_array[i][j]=original[i][j];
		return new_array;
	}
	
	
	/**
	 * @return current time in the format "yyyy/MM/dd HH:mm:ss"
	 */
	public static String getCurrentTime(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}

	/**function to print out map
	 * @param map
	 * @param no number of the map in results list
	 */
	public static void printMap(int[][] map, int no)
	{
		int n=map.length;
		System.out.println("-------------------------------"+no);
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
				System.out.print(map[i][j]+"\t");
			System.out.println();
		}
		
	}
}
