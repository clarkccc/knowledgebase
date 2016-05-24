package lang.concurrent.collection;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by yuzhm on 2016/5/24.
 */
public class BlockingQueueTest {
    public static void main(String arg[]){
        final BlockingQueue<String> timeBuffer=new ArrayBlockingQueue<String>(200);
        genProducer(timeBuffer).start();
        genComsumer(timeBuffer).start();
        genComsumer(timeBuffer).start();
    }
    public static Thread genProducer(final BlockingQueue<String> timeBuffer){
        return  new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        timeBuffer.put(new Date().toString());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public static Thread genComsumer(final BlockingQueue<String> buffer){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        String take = buffer.take();
                        System.out.println(Thread.currentThread().getId()+":"+take);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
