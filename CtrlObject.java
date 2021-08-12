package javatest.concurrency.models;

import java.lang.Thread.State;
import java.util.concurrent.ConcurrentHashMap;

public class CtrlObject implements java.lang.AutoCloseable {
	public static ConcurrentHashMap<Object, Integer> chm = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<Integer, State> thread_state = new ConcurrentHashMap<>();
	
	public CtrlReentrantLock lock;
	public String name = null;
	private boolean flag = false;

	public CtrlObject(Object obj)
	{
		this.lock = (CtrlReentrantLock)obj;
		this.lock.lock();
	}
	
	public CtrlObject(Object obj, String name)
	{
		this.lock = (CtrlReentrantLock)obj;
		this.name = name;
		
		if (this.name == null)
		{
			this.lock.lock();
		}
		else
		{
			this.lock.lock(this.name);
		}	
	}
	
	public CtrlObject(Object obj, String name, boolean flag)
	{
		this.lock = (CtrlReentrantLock)obj;
		this.name = name;
		
		if (flag)
		{
			this.flag = true;
			return;
		}
		
		if (this.name == null)
		{
			this.lock.lock();
		}
		else
		{
			this.lock.lock(this.name);
		}	
	}
	
	public void spl_lock()
	{
		this.lock.spl_lock();
	}
	
	public void spl_unlock()
	{
		this.lock.spl_unlock();
	}
	
	@Override
	public void close() {
		
		if(this.flag)
		{
			return;
		}
		
		if (this.name == null)
		{
			this.lock.unlock();
		}
		else
		{
			this.lock.unlock(this.name);
		}
	}
	
	public static void wait(Object obj)
	{
		if (TestApi.tester)
		{
			if (TestApi.debug)
			{
				  System.out.println("[Test]: CtrlObject#wait(Object)");
			}
			
			if (chm.containsKey(obj))
			{
				int resource_id = chm.get(obj);
				TestApi.fj.wait_resource(TestApi.obj, resource_id);
			}
			else
			{
				int resource_id = TestApi.resource_count;
				TestApi.fj.create_resource(TestApi.obj, resource_id);
				TestApi.resource_count++;
				
				chm.put(obj, resource_id);
				TestApi.fj.wait_resource(TestApi.obj, resource_id);
			}
		}
	}
	
	public static void wait(Object obj, long millisec)
	{	
		if (TestApi.tester)
		{
			if (TestApi.debug)
			{
				  System.out.println("[Test]: CtrlObject#wait(Object, long)");
			}
			
			int opr_id = TestApi.fj.scheduled_operation_id(TestApi.obj);
			thread_state.put(opr_id, Thread.State.TIMED_WAITING);	
			TestApi.fj.schedule_next(TestApi.obj);
			thread_state.put(opr_id, Thread.State.RUNNABLE);
		}	
	}
	
	public static void notifyAll(Object obj)
	{
		if (TestApi.tester)
		{
			if (TestApi.debug)
			{
				  System.out.println("[Test]: CtrlObject#notifyAll(Object)");
			}
			
			if (chm.containsKey(obj))
			{
				int resource_id = chm.get(obj);
				TestApi.fj.signal_resource(TestApi.obj, resource_id);
			}
		}	
	}
	
	public static void notify(Object obj)
	{
		if (TestApi.debug)
		{
			  System.out.println("[Test]: CtrlObject#notify(Object)");
		}
		
		throw new UnsupportedOperationException();
	}
}
