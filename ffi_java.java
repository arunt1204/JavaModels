package javatest.concurrency.models;

public class ffi_java {

	static {
			// System.out.println("one");
	      System.loadLibrary("coyote");
	      // System.out.println("two");
	   }
	 
	   
	   public native Object create_scheduler();
	   
	   public native Object create_scheduler_with_random_strategy(long seed);
	   
	   public native Object create_scheduler_with_random_strategy(long seed, long prob);
	   
	   public native Object create_scheduler_with_pct(long seed, long bound);
	   
	   public native int attach(Object scheduler);
	   
	   public native int detach(Object scheduler);
	   
	   public native int create_operation(Object scheduler, long operation_id);
	   
	   public native int start_operation(Object scheduler, long operation_id);
	   
	   public native int join_operation(Object scheduler, long operation_id);
	   
	   public native int complete_operation(Object scheduler, long operation_id);
	   
	   public native int create_resource(Object scheduler, long resource_id);
	   
	   public native int wait_resource(Object scheduler, long resource_id);
	   
	   public native int signal_resource(Object scheduler, long resource_id);
	   
	   public native int delete_resource(Object scheduler, long resource_id);
	   
	   public native int schedule_next(Object scheduler);
	   
	   public native int scheduled_operation_id(Object scheduler);
	   
	   public native int next_boolean(Object scheduler);
	   
	   public native int next_integer(Object scheduler, int max_value);
	   
	   public native int error_code(Object scheduler);
	   
	   public native long random_seed(Object scheduler);
	   
	   public static void main(String[] args) throws InterruptedException {
		     
	   }

}
