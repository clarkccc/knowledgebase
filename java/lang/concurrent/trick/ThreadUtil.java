package lang.concurrent.trick;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by yuzhm on 2016/5/26.
 */
public class ThreadUtil {
    public static void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void sleep(){
        sleep(new Random().nextInt(2000));
    }

    public static void runAllServices(Runnable... runnables){
        ExecutorService service= Executors.newCachedThreadPool();
        for (Runnable runnable : runnables) {
            service.submit(runnable);
        }
        service.shutdown();
    }
    public static class ServiceBuild{
        private  ExecutorService service= Executors.newCachedThreadPool();
        private  DelayQueue<DelayWork> delayWorks=new DelayQueue<DelayWork>();
        private  boolean inProcessing=false;

        public ServiceBuild withExecutor(ExecutorService service){this.service=service; return this;}

        public ServiceBuild addTask(Runnable task){delayWorks.add(new DelayWork(task));return this;}

        public ServiceBuild addTask(Runnable task,int period,TimeUnit unit){delayWorks.add(new DelayWork(task,period,unit));return this;}

        private static class DelayWork implements Runnable,Delayed{
            private Runnable task;
            private long time=System.currentTimeMillis();

            public DelayWork(Runnable task) {
                this.task = task;
            }

            public DelayWork(Runnable task,int time,TimeUnit timeUnit) {
                this.task = task;
                this.time+=TimeUnit.MILLISECONDS.convert(time,timeUnit);
            }

            @Override
            public long getDelay(TimeUnit unit) {
                return unit.convert(time-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
            }

            @Override
            public int compareTo(Delayed o) {
                return Long.compare(getDelay(TimeUnit.MILLISECONDS),o.getDelay(TimeUnit.MILLISECONDS));
            }

            @Override
            public void run() {
                task.run();
            }
        }

        public void build(){
            inProcessing=true;
            while(!delayWorks.isEmpty()){
                try {
                    service.submit(delayWorks.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            service.shutdown();
        }
    }
}
