package com.zhl.tools.knight;

/** This class is used to save the current step in the game
 * @author huizhang
 *
 */
public class Worker_step {

	//current position
	private int x,y;
	//current no of steps
	private int counter;
	//current map
	private int[][] map;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public int[][] getMap() {
		return map;
	}
	public void setMap(int[][] map) {
		this.map = map;
	}
	

}
