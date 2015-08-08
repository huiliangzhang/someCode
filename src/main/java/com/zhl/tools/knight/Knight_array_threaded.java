package com.zhl.tools.knight;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**main class for knight closed tour game
 * @author huizhang
 *
 */
public class Knight_array_threaded {

	//board dimension
	private int n;
	//number of CPUs
	private int processors;
	//thread pool
	private ExecutorService executor;
	//game result
	private volatile Worker_result result;
	//list of Worker_status for thread players
	private List<Worker_status> status;

	/**Set board, initialize result object, and calculate initial seeds as status
	 * @param n dimension of board
	 */
	public void setUp(int n) {
		processors = Runtime.getRuntime().availableProcessors();
		this.n = n;
		result = new Worker_result();
		initialize_worker_status();
	}

	/**
	 * Calculate starting work status
	 */
	private void initialize_worker_status() {
		status = new ArrayList<Worker_status>();
		int[][] visited=new int[n][n];
		//the exit is set at level 3, it is 44 seeds, big and fair enough for 8 cores
		get_initial_status(0, 0, 0, visited, 3);
		System.out.println("Number of seeds: "+status.size());
	}

	/**Calculate initial work status
	 * @param x current position
	 * @param y
	 * @param counter current counter
	 * @param visited current visited map
	 * @param deep the exit deep
	 */
	private void get_initial_status(int x, int y, int counter, int[][] visited, int deep)
	{
		if(counter==deep) //add to Worker_status
		{
			Worker_status ws=new Worker_status();
			Worker_step wstep=new Worker_step();
			wstep.setX(x);
			wstep.setY(y);
			wstep.setCounter(counter);
			wstep.setMap(Worker_utils.copy_map(visited));
			ws.addNewStep(wstep);
			status.add(ws);
			
			return;
		}
		
		//recursively to get possible next steps
		List<Step> next=Worker_utils.getNext(x, y, visited);
		for(Step s : next)
		{
			if(!((s.x==0)&&(s.y==0)))
			{
				visited[s.getX()][s.getY()]=counter+1;
				get_initial_status(s.getX(), s.getY(), counter+1,visited, deep);
				visited[s.getX()][s.getY()]=0;
			}
		}
		
	}
	
	/** main function for calculation
	 * @param limit number of requested results
	 * @return results
	 */
	public List<int[][]> calculate(int limit) {
		result.setUp(limit);

		System.out.println("Starting "+processors+" threads ...");
		//create thread pool
		this.executor = Executors.newFixedThreadPool(processors);

		for (int i = 0; i < status.size(); i++) {
			Worker_status ws=status.get(i);
			if(ws.isCompleted())
				continue;
			Worker_Thread worker = new Worker_Thread();
			worker.setStatus(ws);
			worker.setResult(result);
			executor.execute(worker);
		}
		
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		
		return result.getResults();
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Knight_array_threaded tester = new Knight_array_threaded();
		tester.setUp(6);
		
		System.out.println(Worker_utils.getCurrentTime()+"   START");
		List<int[][]> r=tester.calculate(9862*2-5);
		System.out.println(Worker_utils.getCurrentTime()+"   END");
		System.out.println(r.size());
//		for(int i=0;i<r.size();i++)
//			tester.printMap(r.get(i), i+1);

	
		System.out.println(Worker_utils.getCurrentTime()+"   START");
		r=tester.calculate(1000);
		System.out.println(Worker_utils.getCurrentTime()+"   END");
		System.out.println(r.size());
		for(int i=0;i<r.size();i++)
			Worker_utils.printMap(r.get(i), i+1);
	
	}

}
