package com.zhl.tools.knight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Knight_recursive_hashMap {

	HashMap<Step, Integer> visited=new HashMap<Step, Integer>();
	int n;
	int size;
	int index;
	
	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
		this.size=n*n;
	}

	public void printMap()
	{
		System.out.println("-------------------------------"+(++index));
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
				if((i==0)&&(j==0))
					System.out.print("0\t");
				else
					System.out.print(visited.get(new Step(i,j))+"\t");
			System.out.println();
		}
		
	}

	public void jump(int x, int y, Integer counter)
	{

		List<Step> next=getNext(x, y);
		for(Step s : next)
		{
			if(visited.containsKey(s))
				continue;
			if((s.x==0)&&(s.y==0))
			{
				if(visited.size()==this.size-1)
				{
					printMap();
					return;
				}
			}
			else
			{
				visited.put(s, counter);
				jump(s.getX(), s.getY(), counter.intValue()+1);
				visited.remove(s);
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
		Knight_recursive_hashMap tester=new Knight_recursive_hashMap();
		tester.setN(6);
		tester.jump(0, 0, 1);

	}

}
