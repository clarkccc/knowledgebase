package lang.concurrent.control;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yuzhm on 2016/5/24.
 */
public class ConditionTest {
    public static void main(String arg[]){
        final ReentrantLock lock=new ReentrantLock();
        final Condition conditionA = lock.newCondition();
        Condition conditionB = lock.newCondition();
        Condition conditionC = lock.newCondition();
        ExecutorService service= Executors.newFixedThreadPool(4);
        service.execute(new Job("A",lock,conditionA,conditionB));
        service.execute(new Job("B",lock,conditionB,conditionC));
        service.execute(new Job("C", lock, conditionC, conditionA));
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lock();
                    conditionA.signal();
                } finally {
                    lock.unlock();
                }
            }
        });

        service.shutdown();
    }
    public static class Job implements Runnable{
        private String name;
        private Lock   lock;
        private Condition starCondition;
        private Condition nextCondition;

        public Job(String name, Lock lock, Condition starCondition, Condition nextCondition) {
            this.name = name;
            this.lock = lock;
            this.starCondition = starCondition;
            this.nextCondition = nextCondition;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    try {
                        starCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(i + ":" + name);
                    nextCondition.signal();
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
