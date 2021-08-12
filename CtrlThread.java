package javatest.concurrency.models;

// import java.lang.Thread.State;
import java.util.concurrent.ConcurrentHashMap;

//To-Do's: 
public class CtrlThread {
	
	// inner Thread class 
	Thread thread_obj;
	Thread.State thread_state;
	String thread_name = "Test";
	static ConcurrentHashMap<Integer, CtrlThread> thread_CtrlThread_map = new ConcurrentHashMap<>();
	boolean daemon = false;
	int thread_id;
	Runnable rbl;

// inner class
public class CtrlInnerThread extends Thread {
	   
	  CtrlThread clientThread;
	  int thread_id;

	  CtrlInnerThread(CtrlThread clientThread)
	  {
		  this (clientThread, null, null, "Test", 0);
	  }
	  
	  CtrlInnerThread(CtrlThread clientThread, String name)
	  {
		  this (clientThread, null, null, name, 0);
	  }
	  
	  CtrlInnerThread(CtrlThread clientThread, ThreadGroup group, Runnable target, String name, long stackSize)
	  {
		  super (group, target, name, stackSize);
		  
		  if (TestApi.debug)
		  {
			  // System.out.println("[Test]: CtrlInnerThread(CtrlThread, String)");
		  }
		  
		  this.clientThread = clientThread;
	  }
	  
	  public void run()
	  {
		  if (!TestApi.tester)
			{
			  this.clientThread.run();
				return;
			}
		  
		  if (TestApi.debug)
		  {
			  // System.out.println("[Test]: CtrlInnerThread#run()");
		  }
		  
		  TestApi.fj.start_operation(TestApi.obj, this.thread_id);
		  clientThread.thread_id = TestApi.fj.scheduled_operation_id(TestApi.fj);
		  this.clientThread.run();
		  if (TestApi.debug)
		  {
			  // System.out.println("[Test]: CtrlInnerThread#complete_op()");
		  }
		  TestApi.fj.complete_operation(TestApi.obj, this.thread_id);
	  }
}

public class CtrlInnerRunnable extends Thread {
	   
	 Runnable runnable;
	 int thread_id;
	  
	  CtrlInnerRunnable(Runnable target)
	  {
		  this (null, target, "Test", 0);
		  
		  if (TestApi.debug)
		  {
			  // System.out.println("[Test]: CtrlInnerRunnable(Runnable)");
		  }
	  }
	  
	  CtrlInnerRunnable(ThreadGroup group, Runnable target, String name, long stackSize)
	  {	  
		  super(group, target, name, stackSize);
		  if (TestApi.debug)
		  {
			  // System.out.println("[Test]: CtrlInnerRunnable(ThreadGroup,Runnable,String,long)");
		  }
		  this.runnable = target;
	  }
	  
	  public void run()
	  {
		  if (!TestApi.tester)
			{
			  this.runnable.run();
				return;
			}
		  
		  if (TestApi.debug)
		  {
			  // System.out.println("[Test]: CtrlInnerRunnable#run()");
		  }
		  
		  TestApi.fj.start_operation(TestApi.obj, this.thread_id);
		  this.runnable.run();
		  TestApi.fj.complete_operation(TestApi.obj, this.thread_id);
	  }
}

	public CtrlThread() 
	{
		this(null, null, null, 0);
	}
	
	public CtrlThread (Runnable runnable)
	{
		this(null, runnable, null, 0);
		
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread(Runnable)");
		}		
	}
	
	public CtrlThread (ThreadGroup group, Runnable target, String name, long stackSize)
	{
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread(ThreadGroup,Runnable,String,long)");
		}
		
		
		this.thread_id = TestApi.count;
		TestApi.count++;
		this.rbl = target;
		
		if ( target != null)
		{
			CtrlInnerRunnable cit = new CtrlInnerRunnable(this.rbl);
			this.thread_obj = cit;
			cit.thread_id = this.thread_id;
		}
		else
		{
			CtrlInnerThread cit = new CtrlInnerThread(this, this.thread_name);
			this.thread_obj = cit;
			cit.thread_id = this.thread_id;
		}
		
		thread_CtrlThread_map.put(this.thread_id, this);		
	}
	
	public CtrlThread (String name)
	{
		this(null, null, name, 0);
		
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread(String)");
		}
	}
	
	CtrlThread(Thread t)
	{
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread(Thread)");
		}
		this.thread_obj = t;
	}
	
	public void start()
	{
		if (!TestApi.tester)
		{
			this.thread_obj.start();
			return;
		}
		
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread#Start()");
		}
		
		TestApi.fj.create_operation(TestApi.obj, this.thread_id);

		this.thread_state = this.thread_obj.getState();
		this.thread_obj.start();
	}
	
	public void run()
	{
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread#run()");
		}
	}
	
	public void join() throws InterruptedException
	{
		if (!TestApi.tester)
		{
			this.thread_obj.join();
			return;
		}
		
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread#join()");
		}
		
		TestApi.fj.join_operation(TestApi.obj, this.thread_id);
		this.thread_obj.join();
	}
	
	public void join(long millis) throws InterruptedException
	{
		if (!TestApi.tester)
		{
			this.thread_obj.join(millis);
			return;
		}
		
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread#join(long)");
		}
		
		this.join();
	}
	
	public final boolean isAlive()
	{
		if (!TestApi.tester)
		{
			return this.thread_obj.isAlive();
		}
		
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread#isAlive()");
		}
		
		return this.thread_obj.isAlive();
	}
	
	public Thread.State getState()
	{	
		if (!TestApi.tester)
		{
			return this.thread_obj.getState();
		}
		
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread#getState()");
		}
		
		if (CtrlObject.thread_state.get(this.thread_id) == Thread.State.TIMED_WAITING)
		{
			return Thread.State.TIMED_WAITING;
		}
		
		return this.thread_obj.getState();
	}
	
	public static void sleep(long millis) throws InterruptedException
	{
		if (!TestApi.tester)
		{
			Thread.sleep(millis);
			return;
		}
		
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread#sleep(long)");
		}

		TestApi.fj.schedule_next(TestApi.obj);
	}
	
	public static void sleep(long millis, int nanos) throws InterruptedException
	{
		if (!TestApi.tester)
		{
			Thread.sleep(millis, nanos);
			return;
		}
		
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread#sleep(long, int)");
		}

		TestApi.fj.schedule_next(TestApi.obj);
	}
	
	public static void yield()
	{
		if (!TestApi.tester)
		{
			Thread.yield();
			return;
		}
		
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread#yield()");
		}
		
		TestApi.fj.schedule_next(TestApi.obj);
	}
	
	public static CtrlThread currentThread()
	{
		if (!TestApi.tester)
		{
			return null;
		}
		
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread#currentThread()");
		}
		
		CtrlThread ct = null;
		
		if (thread_CtrlThread_map.containsKey(TestApi.fj.scheduled_operation_id(TestApi.fj)))
		{
			ct = thread_CtrlThread_map.get(TestApi.fj.scheduled_operation_id(TestApi.fj));
		}
		else
		{
			ct = new CtrlThread(Thread.currentThread());
			thread_CtrlThread_map.put(TestApi.fj.scheduled_operation_id(TestApi.fj), ct);
		}
		
		return ct;
	}
	
	public String getName()
	{
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread#getName()");
		}
		
		return this.thread_name;
	}
	
	public void setDaemon(boolean value)
	{
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread#setDaemon()");
		}
		
		this.daemon = value;
	}
	
	public void setName(String value)
	{
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread#setName()");
		}
		
		this.thread_name = value;
	}	
	
	public static boolean holdsLock(Object obj)
	{
		if (!TestApi.tester)
		{
			CtrlReentrantLock crl = (CtrlReentrantLock)obj;
			if (crl.actual_threadid == Thread.currentThread().getId())
			{
				// System.out.println("[holdsLock]: " + crl.hashCode() + " : " + true);
				return true;
			}
			
			// System.out.println("[holdsLock]: " + crl.hashCode() + " : " + false);
			return false;
		}
		
		CtrlReentrantLock crl = (CtrlReentrantLock)obj;
		if (TestApi.debug)
		{
			  // System.out.println("[Test]: CtrlThread#holdsLock(): " + crl.hashCode() + " : " + crl.opertional_id);
		}
		
		
		if (crl.opertional_id == TestApi.fj.scheduled_operation_id(TestApi.obj))
		{
			return true;
		}
		
		return false;
	}
	
	public final void setPriority(int newPriority)
	{
		// TODO: add stuffs
	}
	
	public final int getPriority()
	{
		// TODO: add stuffs
		return 1;
	}
	
	public static boolean interrupted()
	{
		throw new UnsupportedOperationException();
	}
	
	public void interrupt()
	{
		throw new UnsupportedOperationException();
	}
	
}
