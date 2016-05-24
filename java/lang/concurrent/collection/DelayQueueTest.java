package lang.concurrent.collection;

import java.util.Timer;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yuzhm on 2016/5/24.
 */
public class DelayQueueTest {
    public static void main(String arg[]){
        DelayQueue<Delayed> buffer=new DelayQueue<Delayed>();
    }
    public static void startProducerThread(final DelayQueue<Delayed> buffer){
        new Thread(new Runnable() {
            @Override
            public void run() {
                buffer.add(new Delayed() {
                    private long init=System.currentTimeMillis();
                    @Override
                    public long getDelay(TimeUnit unit) {
                        return init;
                    }
                    @Override
                    public int compareTo(Delayed o) {
                        return 1;
                    }
                });
            }
        }).start();
    }
    public static void startComsumerThread(DelayQueue<Delayed> buffer){}
}
