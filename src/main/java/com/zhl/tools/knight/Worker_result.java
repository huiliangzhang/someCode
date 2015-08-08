package com.zhl.tools.knight;

import java.util.ArrayList;
import java.util.List;

/** game result class
 * @author huizhang
 *
 */
public class Worker_result {
	//number of results requested
	private int limit;
	//results
	private List<int[][]> results=new ArrayList<int[][]>();
	//sign that requirement of current request has been reached
	private boolean finished=true;

	public int getLimit() {
		return limit;
	}

	/**
	 * @return true if requirement of current request has been reached, false else
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * @param finished set value for finished sign
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	/** if new limit is greater than 0, set new limit, clear current results list, and set finished to false
	 * @param limit new limit, negative number or 0 for infinite
	 */
	public void setUp(int limit) {		
		this.limit = limit;
		this.results.clear();
		this.finished=false;
	}

	/**
	 * @return results
	 */
	public List<int[][]> getResults() {
		return results;
	}
	
	/** synchronized function to put new result.
	 * @param map new result
	 * @return false if current results have been ready, true otherwise
	 */
	public synchronized boolean addResult(int[][] map)
	{
		if(finished)
			return false;
		
		results.add(map);
		if((limit>0)&&(results.size()>=limit))
			finished=true;
		
		if(results.size()%1000==0)
			System.out.println("   "+Worker_utils.getCurrentTime()+" "+results.size()+" results gotten.");
		
		return true;
	}
	
}
