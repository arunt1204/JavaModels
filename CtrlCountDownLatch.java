package javatest.concurrency.models;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CtrlCountDownLatch {
	CountDownLatch cdl;
	int count;
	int resource_id;
	
	public CtrlCountDownLatch (int count)
	{
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlCountDownLatch(int)");
		}
		cdl = new CountDownLatch(count);
		this.count = count;
		this.resource_id = TestApi.resource_count;
		
		if (TestApi.tester)
		{
			TestApi.fj.create_resource(TestApi.obj, this.resource_id);
		}
		
		TestApi.resource_count++;
	}
	
	public void await() throws InterruptedException
	{
		if (!TestApi.tester)
		{
			this.cdl.await();
		}
		
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlCountDownLatch#await()" + Thread.currentThread().getId());
		}
		if (this.count == 0)
		{
			return;
		}
		
		if (TestApi.tester)
		{
			TestApi.fj.wait_resource(TestApi.obj, this.resource_id);
		}
	}
	
	public boolean await (long timeout, TimeUnit unit) throws InterruptedException
	{
		if (!TestApi.tester)
		{
			this.cdl.await(timeout, unit);
		}
		
		
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlCountDownLatch#await(long, Timeout)");
		}
		
		if (TestApi.tester)
		{
			TestApi.fj.schedule_next(TestApi.obj);
		}
		
		if (this.count == 0)
		{
			return true;
		}
	
		return false;
	}
	
	public void countDown()
	{
		if (!TestApi.tester)
		{
			this.cdl.countDown();
		}
		
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlCountDownLatch#countDown()");
		}

		if (TestApi.tester)
		{
			TestApi.fj.schedule_next(TestApi.obj);
		}
		
		if (this.count > 0)
		{
			this.count--;
		}
		
		if (this.count == 0 && TestApi.tester)
		{
			TestApi.fj.signal_resource(TestApi.obj, this.resource_id);
		}
	}
	
	public long getCount()
	{
		if (!TestApi.tester)
		{
			return this.cdl.getCount();
		}
		
		if (TestApi.debug)
		{
		  // System.out.println("[Test]: CtrlCountDownLatch#getcount()");
		}
		
		return this.count;
	}
}
