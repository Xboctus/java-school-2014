package task2;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Trying to receive 146% deadlock.
 * Both threads starts and trying to receive a ReentrantLock.
 * When one of them catch the lock, this thread immediatly ends
 * and other can't perform any actions
 * because it can't receive the lock (which remains locked)
 * and unlock lock (cause second thread is not a holder of this lock)
 */
public class Deadlock {

  private static ReentrantLock lock = new ReentrantLock();

  private static Thread thread1 = new Thread(new Runnable() {
    @Override
    public void run() {
      System.out.println("Thread1 started and trying to get lock...");
      lock.lock();
      System.out.println("Thread1 locked lock ");
      System.out.println("Thread1 ends...");
    }
  });

  private static Thread thread2 = new Thread(new Runnable() {
    @Override
    public void run() {
      System.out.println("Thread2 started and trying to get lock...");
      lock.lock();
      System.out.println("Thread2 locked lock");
      System.out.println("Thread2 ends...");
    }
  });

  public static void main(String[] args) {
    thread2.start();
    thread1.start();
    System.out.println("Both thread started...");
  }

}
