public class DeadLock2{
    public static void main(String[] args){
        BoundedBuffer myBoundedBuffer = new BoundedBuffer(10);
        new Producer(myBoundedBuffer).start();
        new Consumer(myBoundedBuffer).start();
    }
}

class BoundedBuffer{
    private int bufferSize, in, out, count;
    private int element[];

    public BoundedBuffer(int mybufferSize){
        bufferSize = mybufferSize;
        in = 0;
        out = 0;
        count = 0;
        element = new int[bufferSize];
    }

    public synchronized void add(int x){
        while (count == bufferSize)
//        {
//            try{
//                wait();
//            }
//            catch (InterruptedException e){}
//        }

        Thread.yield();
        ++count;
        element[in] = x;
        in = (in + 1)%bufferSize;
        System.err.println("New element " + x + " is added. Count=" + count);

//        notify();
    }

    public synchronized int remove(){
        while (count == 0)
//        {
//            try{
//                wait();
//            }
//            catch (InterruptedException e){}
//        }

        Thread.yield();
        int temporal = element[out];
        element[out] = 0;
        out = ++out%bufferSize;
        count--;
        System.err.println("            Element "+temporal+" is removed. Count="+count);
//        notify();
        return temporal;
    }
}

class Producer extends Thread{
    private BoundedBuffer buffer;

    public Producer(BoundedBuffer b){
        buffer = b;
}

    public void run(){
        int newItem;
        for (int i = 0; i < 10; i++)
        {
            newItem = (int)(Math.random()* 100);
            try
            {
                Thread.sleep((int)(Math.random()* 3000));
            }
            catch(InterruptedException ie){}
            buffer.add(newItem);
        }
    }
}

class Consumer extends Thread{
    private BoundedBuffer buffer;

    Consumer(BoundedBuffer b){
        buffer = b;
    }

    public void run(){
        int remItem;
        for (int i=0; i<10; i++)   // usually it is while(true)
        {
            try
            {
                Thread.sleep((int)(Math.random()* 5000));
            }
            catch(InterruptedException ie){}
            remItem = buffer.remove();
        }
    }
}
 
