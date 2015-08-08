package com.zhl.tools.knight;

import java.util.ArrayList;
import java.util.List;

public class Knight_recursive_array {

	int[][] visited;
	int n;
	int size;
	int sum;
	
	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
		this.size=n*n;
		visited=new int[n][n];
	}
	
	public int getSum() {
		return sum;
	}

	public void setUp()
	{
		this.sum=0;
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++)
				visited[i][j]=0;
	}
	
	public void printMap()
	{
		System.out.println("-------------------------------"+(++sum));
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
				System.out.print(visited[i][j]+"\t");
			System.out.println();
		}
		
	}

	public void jump(int x, int y, int counter)
	{
		List<Step> next=getNext(x, y);
		
		for(Step s : next)
		{
			if((s.x==0)&&(s.y==0))
			{
				if(counter==this.size-1)
				{
					printMap();
					return;
				}
			}
			else
			{
				visited[s.getX()][s.getY()]=counter+1;
				jump(s.getX(), s.getY(), counter+1);
				visited[s.getX()][s.getY()]=0;
			}
		}
		
	}
	
	public List<Step> getNext(int x, int y)
	{
		ArrayList<Step> result=new ArrayList<Step>();
		addNext(x,y, 2, 1, result);
		addNext(x,y, 1, 2, result);
		addNext(x,y, -1, 2, result);
		addNext(x,y, -2, 1, result);
		addNext(x,y, -2, -1, result);
		addNext(x,y, -1, -2, result);
		addNext(x,y, 1, -2, result);
		addNext(x,y, 2, -1, result);
		
		return result;
	}
	
	public void addNext(int x, int y, int offsetx, int offsety, List<Step> result)
	{
		x+=offsetx;
		if(!isValid(x))
			return;
		y+=offsety;
		if(!isValid(y))
			return;
		if(visited[x][y]!=0)
			return;
		result.add(new Step(x,y));
	}
	
	public boolean isValid(int i)
	{
		if((i<0)||(i>=n))
			return false;
		return true;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Knight_recursive_array tester=new Knight_recursive_array();
		tester.setN(6);
		tester.jump(0, 0, 0);

	}

}
