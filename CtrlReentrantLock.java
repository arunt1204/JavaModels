package javatest.concurrency.models;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class CtrlReentrantLock
{
	ReentrantLock rl;
	boolean flag = false;
	boolean tryflag = false;
	int resource_id;
	String name_test = "";
	
	int opertional_id;
	int lock_count;
	public ConcurrentHashMap<Integer, Integer> thread_lock = new ConcurrentHashMap<>();
	public ConcurrentHashMap<Object, Integer> Object_Oprid = new ConcurrentHashMap<>();
	
	int actual_threadid;
	boolean flagt = true;
	public CtrlReentrantLock()
	{
		if (TestApi.debug)
		{
			  System.out.println("[Test]: CtrlReentrantLock(): " + this.hashCode() + " : " + Thread.currentThread().getId());
		}
		
		this.rl = new ReentrantLock();
		this.resource_id = TestApi.resource_count;
		
		if (TestApi.tester)
		{
			TestApi.fj.create_resource(TestApi.obj, this.resource_id);
		}
		
		TestApi.resource_count++;
		
		this.opertional_id = -1;
		this.lock_count = 0;
		this.actual_threadid = -1;
		
		// //System.out.println("CtrlLock(rin): " + TestApi.fj.next_integer(TestApi.obj, 4));
	}
	
	public CtrlReentrantLock(String nameDebug)
	{
		this ();
		
		if (nameDebug.equalsIgnoreCase("refreshLock"))
		{
			// flagt = true;
		}
		
		if (TestApi.debug)
		{
			  System.out.println("[Test]: CtrlReentrantLock(): " + this.hashCode() + " : " + Thread.currentThread().getId() + " : " + nameDebug);
		}
	}
	
	public void spl_lock()
	{
		if (!TestApi.tester)
		{
			return;
		}
		
		if (TestApi.debug)
		{
			System.out.println("Enter: spl_lock");
		}
		
		int pre_count = thread_lock.get(TestApi.fj.scheduled_operation_id(TestApi.obj));

		for (int i = 0; i < pre_count; i++)
		{
			this.lock();
		}
		
		if (TestApi.debug)
		{
			System.out.println("Exit: spl_lock");
		}
	}
	
	public void lock()
	{
		if (!TestApi.tester)
		{
			rl.lock();
			this.lock_count++;
			this.actual_threadid = (int) Thread.currentThread().getId();
			//System.out.println("[Acquired]: CtrlReentrantLock#lock(): " + this.hashCode()  + " : " + Thread.currentThread().getId());
			return;
		}
		
		if (TestApi.debug)
		{
			  System.out.println("[Test]: CtrlReentrantLock#lock(): " + this.hashCode()  + " : " + Thread.currentThread().getId() + " : " + TestApi.fj.error_code(TestApi.obj));
		}
		
		while (true)
		{
			if (!tryflag)
			{
				TestApi.fj.schedule_next(TestApi.obj);
			}

			if (TestApi.fj.scheduled_operation_id(TestApi.obj) == this.opertional_id)
			{
				flag = false;
			}
			
			if (flag)
			{
				if (TestApi.debug)
				{
					  System.out.println("[Test]: Thread waiting for lock: " + this.hashCode()  + " : " + Thread.currentThread().getId() + " : " + TestApi.fj.error_code(TestApi.obj) + " :LC: " + this.lock_count + " Hold-by: " + this.opertional_id + " " + this.name_test);
				}
				
				TestApi.fj.wait_resource(TestApi.obj, this.resource_id);
			}
			else
			{
				flag = true;
				this.opertional_id = TestApi.fj.scheduled_operation_id(TestApi.obj);
				this.lock_count++;
				break;
			}
		}
		
		if (TestApi.debug)
		{
			System.out.println("Lock actual acquired by: " + Thread.currentThread().getId() + " : " + this.hashCode() + " :LC: " + this.lock_count);
		}
		
		rl.lock();
		Object_Oprid.put(this, TestApi.fj.scheduled_operation_id(TestApi.obj));
		
		if (TestApi.debug)
		{
			System.out.println("Lock acquired by: " + Thread.currentThread().getId() + " : " + this.hashCode() + " :LC: " + this.lock_count);
		}
	}
	
	// For Debugging only
	public void lock(String debugMethod)
	{
		if (TestApi.debug)
		{
			System.out.println("Lock-Debug[Requested]: "  + this.hashCode() + " : " + Thread.currentThread().getId() + " : " + debugMethod);
		}
		
		this.lock();
		this.name_test = debugMethod;
		if (TestApi.debug)
		{
			System.out.println("Lock-Debug[Acquired]: "  + this.hashCode() + " : " + Thread.currentThread().getId() + " : " + debugMethod);
		}
	}
	
	public void spl_unlock()
	{
		if (!TestApi.tester)
		{
			return;
		}
		
		if (TestApi.debug)
		{
			System.out.println("Enter: spl_unlock: LC: " + this.lock_count);
		}
		
		thread_lock.put(TestApi.fj.scheduled_operation_id(TestApi.obj), this.lock_count);
		
		int temp = this.lock_count;
		for (int i = 0; i < temp; i++)
		{
			this.unlock();
		}
		
		if (TestApi.debug)
		{
			System.out.println("Exit: spl_unlock: LC: " + this.lock_count);
		}
	}
	
	public void unlock()
	{
		if (!TestApi.tester)
		{
			rl.unlock();
			this.lock_count--;
			
			if (this.lock_count == 0)
			{
				this.actual_threadid = -1;
			}

			System.out.println("[Released]: CtrlReentrantLock#lock(): " + this.hashCode()  + " : " + Thread.currentThread().getId());
			return;
		}
		
		if (TestApi.debug)
		{
			  System.out.println("[Test]: CtrlReentrantLock#unlock(): " + this.hashCode()  + " : " + Thread.currentThread().getId());
		}
		
		TestApi.fj.schedule_next(TestApi.obj);
		
		if (TestApi.debug)
		{
			System.out.println("Lock released by: " + Thread.currentThread().getId() + " : " + this.hashCode() + " :LC: " + this.lock_count);
		}
		
		rl.unlock();
		
		if (TestApi.debug)
		{
			System.out.println("Lock released actual by: " + Thread.currentThread().getId() + " : " + this.hashCode() + " :LC: " + this.lock_count);
		}
		
		if (flag)
		{	
			this.lock_count--;
			if (this.lock_count == 0)
			{
				flag = false;
				this.opertional_id = -1;
				Object_Oprid.remove(this);
				TestApi.fj.signal_resource(TestApi.fj, this.resource_id);
			}
		}
		else
		{
			// throw exception
			//System.out.println("Error");
		}
		
		if (TestApi.debug)
		{
			System.out.println("Lock released by: " + Thread.currentThread().getId() + " : " + this.hashCode() + " :LC: " + this.lock_count);
		}
		
		TestApi.fj.schedule_next(TestApi.obj);
	}
	
	// For Debugging only
	public void unlock(String debugName)
	{
		this.unlock();
		
		if (TestApi.debug)
		{
			System.out.println("Lock-Debug[Released]: " + this.hashCode() + " : " + Thread.currentThread().getId() + " : " + debugName + " EC: " + TestApi.fj.error_code(TestApi.obj));
		}
	}
	
	public boolean tryLock()
	{
		if (!TestApi.tester)
		{
			return this.rl.tryLock();
		}
		
		if (TestApi.debug)
		{
			  System.out.println("[Test]: CtrlReentrantLock#trylock(): " + this.hashCode()  + " : " + Thread.currentThread().getId());
		}
		
		if (!flag)
		{
			tryflag = true;
			this.lock();
			tryflag = false;
			return true;
		}
		
		return false;
	}
	
	public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException
	{
		if (!TestApi.tester)
		{
			return this.rl.tryLock(timeout, unit);
		}
		
		if (TestApi.debug)
		{
			  System.out.println("[Test]: CtrlReentrantLock#trylock(long, Timeout): " + this.hashCode()  + " : " + Thread.currentThread().getId());
		}
		
		return this.tryLock();
	}
	
	public boolean isHeldByCurrentThread()
	{
		if (!TestApi.tester)
		{
			return this.rl.isHeldByCurrentThread();
		}
		
		if (TestApi.debug)
		{
			System.out.println("isHeldByCurrentThread");
		}

		if (this.opertional_id == TestApi.fj.scheduled_operation_id(TestApi.obj))
		{
			return true;
		}
		
		return false;
	}
	
	/* public boolean tryLock()
	{
		return this.tryLock(0, TimeUnit.SECONDS); 
	} */
	
	public boolean isLocked()
	{
		throw new UnsupportedOperationException();
	}
}
