package com.zhl.tools.knight;

import java.util.List;

/** This is the thread class for game player
 * @author huizhang
 *
 */
public class Worker_Thread implements Runnable {

	//worker status
	private Worker_status status;
	//game result object, shared among all thread players
	private Worker_result result;

	/** Set game result
	 * @param result game result
	 */
	public void setResult(Worker_result result) {
		this.result = result;
	}

	/** Set game status
	 * @param ws game status
	 */
	public void setStatus(Worker_status ws)
	{
		this.status=ws;
	}

	public void run() {
		while(true)
		{
			//return if the goal limit has been reached
			if(result.isFinished())
				return;
			
			//retrieve current top step
			Worker_step wstep=this.status.popStep();
			if(wstep==null)
				break;
			int n=wstep.getMap().length;

			//get all possible next positions
			List<Step> next=Worker_utils.getNext(wstep.getX(), wstep.getY() , wstep.getMap());
			//iterate the possible positions
			for(Step s : next)
			{
				//if it is (0,0)
				if((s.getX()==0)&&(s.getY()==0))
				{
					//if it is a closed tour
					if(wstep.getCounter()==n*n-1)
					{
						if(result.addResult(wstep.getMap()))//successfully added to game result
							break;
						else{
							this.status.addNewStep(wstep);//game goal has been reached
							return;
						}
					}
				}
				else//add a new Worker_step into the status stack
				{
					int[][] new_map=Worker_utils.copy_map(wstep.getMap());
					new_map[s.getX()][s.getY()]=wstep.getCounter()+1;
					Worker_step a=new Worker_step();
					a.setX(s.getX());
					a.setY(s.getY());
					a.setMap(new_map);
					a.setCounter(wstep.getCounter()+1);
					this.status.addNewStep(a);
				}
			}
		}
		
		//signal that this player has finished
		this.status.setCompleted(true);
    }

	
}