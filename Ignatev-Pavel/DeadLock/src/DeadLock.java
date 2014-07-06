import java.util.concurrent.locks.ReentrantLock;




public class DeadLock {
	private ReentrantLock a = new ReentrantLock();
	private ReentrantLock b = new ReentrantLock();
	private boolean z = false;
	private boolean x = false;
	
	DeadLock(){
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				pr1();
			}
		}).start();
		new Thread(new Runnable(){
			@Override
			public void run() {
				pr2();
			}
		}).start();
	}
	
	private void pr1(){
		a.lock();
		while(!z);
		z = false;
		System.out.println("trying getting lock b");
		b.lock();
		a.unlock();
		b.unlock();
		System.out.println("p1 ended");
	}
	private void pr2(){
		b.lock();
		z=true;
		while(z);
		System.out.println("trying getting lock a");
		a.lock();
		b.unlock();
		a.unlock();
		System.out.println("p2 ended");
	}
	
	public static void main(String[] args) throws InterruptedException{
		new DeadLock();
	}
}
