package com.roboticseattle.spar.body;

import com.roboticseattle.spar.mind.Nerve;

public abstract class Part implements Runnable, Startable {
	
	protected Thread thread;
	protected Nerve nerve = null;
	protected boolean isShutdown = false;
	protected int sleepBetween = 500;
	
	public Part() {
	}
	
	public Part(int aSleepBetween) {
		sleepBetween = aSleepBetween;
	}
	
	public synchronized void start(Nerve aNerve) {
		nerve = aNerve;
		isShutdown = false;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void shutdown() {
		isShutdown = true;
	}
	
	public abstract void   beforeLoop() throws Exception;
	public abstract void   insideLoop(int counter) throws Exception;
	public abstract void   afterLoop();
	public abstract String painfulStuff();
	public abstract void   insideInterrupted(); 

	@Override
	public void run() {
		try {
		    beforeLoop();
		} catch(Exception e) {
			if(nerve != null) nerve.onPainfulEvent(this, e, "beforeLoop(): "+painfulStuff());
			else {
				System.out.println("beforeLoop: painfulStuff()="+painfulStuff());
				e.printStackTrace();
			}
			isShutdown = true;
			return;
		}
		int i=0;
		while(!isShutdown) {
			try {
				insideLoop(i++);
				Thread.sleep(sleepBetween);
			} catch(InterruptedException io) {
				insideInterrupted();
			} catch(Exception e) {
				if(nerve != null) nerve.onPainfulEvent(this, e, painfulStuff());
				else {
					System.out.println("painfulStuff()="+painfulStuff());
					e.printStackTrace();
				}
			}
		}
		afterLoop();
	}
	
	

}
