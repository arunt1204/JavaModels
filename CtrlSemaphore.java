package javatest.concurrency.models;

import java.util.concurrent.Semaphore;

public class CtrlSemaphore {
	int MaxCount;
	int NumAcquired;
	int resource_id;
	Semaphore sp;
	boolean tryflag;
	
	public CtrlSemaphore (int permits)
	{
		if (TestApi.debug)
		{
			 // System.out.println("[Test]: CtrlSemaphore(int)");
		}
		this.sp = new Semaphore(permits);
		
		this.MaxCount = permits;
		this.NumAcquired = 0;
		this.resource_id = TestApi.resource_count;
		this.tryflag = false;
		
		if (TestApi.tester)
		{
			TestApi.fj.create_resource(TestApi.obj, this.resource_id);
		}
		
		TestApi.resource_count++;
	}
	
	public void acquire() throws InterruptedException
	{
		if (!TestApi.tester)
		{
			this.sp.acquire();
			return;
		}
		
		if (TestApi.debug)
		{
			 // System.out.println("[Test]: CtrlSemaphore#acquire()");
		}
		
		if (!tryflag)
		{
			TestApi.fj.schedule_next(TestApi.obj);
		}
		
		while (this.MaxCount - this.NumAcquired <= 0)
		{
			TestApi.fj.wait_resource(TestApi.obj, this.resource_id);
		}
		
		this.NumAcquired++;
	}
	
	public boolean tryAcquire()
	{
		if (!TestApi.tester)
		{
			return this.sp.tryAcquire();
		}
		
		if (TestApi.debug)
		{
			 // System.out.println("[Test]: CtrlSemaphore#tryAcquire()");
		}
		
		if (this.MaxCount - this.NumAcquired >= 1)
		{
			try {
				this.tryflag = true;
				this.acquire();
				this.tryflag = false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		
		return false;
	}
	
	public void release()
	{
		if (!TestApi.tester)
		{
			this.sp.release();
			return;
		}
		
		if (TestApi.debug)
		{
			 // System.out.println("[Test]: CtrlSemaphore#release()");
		}
		
		TestApi.fj.schedule_next(TestApi.obj);
		this.NumAcquired--;
		
		TestApi.fj.signal_resource(TestApi.obj, this.resource_id);
		TestApi.fj.schedule_next(TestApi.obj);
	}
	
	public int availablePermits()
	{
		if (!TestApi.tester)
		{
			return this.sp.availablePermits();
		}
		
		if (TestApi.debug)
		{
			 // System.out.println("[Test]: CtrlSemaphore#availablePermits()");
		}
		
		return this.MaxCount - this.NumAcquired;
	}
	
	public void acquire(int permits) throws InterruptedException
	{
		if (!TestApi.tester)
		{
			this.sp.acquire(permits);
			return;
		}
		
		if (TestApi.debug)
		{
			 // System.out.println("[Test]: CtrlSemaphore#acquire(int)");
		}
		
		TestApi.fj.schedule_next(TestApi.obj);
		
		while (this.MaxCount - this.NumAcquired < permits)
		{
			TestApi.fj.wait_resource(TestApi.obj, this.resource_id);
		}
		
		this.NumAcquired++;
	}
	
	public void release(int permits)
	{
		if (!TestApi.tester)
		{
			this.sp.release(permits);
			return;
		}
		
		if (TestApi.debug)
		{
			 // System.out.println("[Test]: CtrlSemaphore#release(int)");
		}
		
		TestApi.fj.schedule_next(TestApi.obj);
		this.NumAcquired = this.NumAcquired - permits;
		
		TestApi.fj.signal_resource(TestApi.obj, this.resource_id);
		TestApi.fj.schedule_next(TestApi.obj);
	}
}
