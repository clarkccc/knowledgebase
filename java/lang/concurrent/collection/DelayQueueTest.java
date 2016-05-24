package lang.concurrent.collection;

import java.util.Date;
import java.util.Timer;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yuzhm on 2016/5/24.
 * 用DelayQueue实现循环的TimerTask
 */

public class DelayQueueTest {
    private static  final DelayQueue<TimeTask> buffer=new DelayQueue<TimeTask>();
    public static void main(String arg[]){
        buffer.add(new TimeTask(new Runnable() {
            @Override
            public void run() {
                System.out.println("3:"+new Date().toString());
            }
        },3,TimeUnit.SECONDS));
        buffer.add(new TimeTask(new Runnable() {
            @Override
            public void run() {
                System.out.println("5:"+new Date().toString());
            }
        },5,TimeUnit.SECONDS));
        startComsumerThread();
    }

    public static class TimeTask implements Delayed{
        Runnable task;
        final long time;
        final TimeUnit unit;
        long timeStart;

        public TimeTask(Runnable task, long time, TimeUnit unit) {
            this.task = task;
            this.time = time;
            this.unit = unit;
            timeStart = System.currentTimeMillis()+unit.toMillis(time);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(timeStart-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(getDelay(TimeUnit.MILLISECONDS),o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        protected Object clone() {
            return new TimeTask(task,time,unit);
        }
    }

    public static void startComsumerThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        TimeTask take = buffer.take();
                        new Thread(take.task).start();
                        buffer.add((TimeTask)(take.clone()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
