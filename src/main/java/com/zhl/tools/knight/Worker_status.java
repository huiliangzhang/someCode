package com.zhl.tools.knight;

import java.util.Stack;

/** This class is to save current status for each thread player
 * @author huizhang
 *
 */
public class Worker_status {

	//stack that saves all valid maps
	private Stack<Worker_step> maps =new Stack<Worker_step>();
	//boolean indicates whether this has been completed
	private boolean completed;
	
	/**
	 * @return true if this has been completed
	 */
	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	/** add new element to the steps stack
	 * @param ws new Worker_step
	 */
	public void addNewStep(Worker_step ws){
		this.maps.add(ws);
	}
	
	/**
	 * @return top element in the steps
	 */
	public Worker_step popStep(){
		if(this.maps.empty())
			return null;
		return this.maps.pop();
	}
	
}
