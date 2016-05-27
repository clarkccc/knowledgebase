package lang.concurrent.control.sync;

import lang.concurrent.trick.ThreadUtil;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yuzhm on 2016/5/27.
 * 模拟起跑,大家等待发令枪发动后一起起跑
 * 现实中用的少了,需要定义需要同步的数字,限制了其应用场景
 */
public class CyclicBarrierTest {
    public static void main(String arg[]){
        final int threadToSync=5;
        CyclicBarrier barrier=new CyclicBarrier(threadToSync);
        new ThreadUtil.ServiceBuild()
            .addTask(new Runner(barrier))
            .addTask(new Runner(barrier), 5, TimeUnit.SECONDS)
            .addTask(new Runner(barrier), 10, TimeUnit.SECONDS)
            .addTask(new Runner(barrier), 15, TimeUnit.SECONDS)
            .addTask(new Runner(barrier), 20, TimeUnit.SECONDS)
            .build();
    }

    private static AtomicInteger idGenerator=new AtomicInteger(0);
    private static class Runner implements Runnable{
        private final String id="THREAD_"+idGenerator.incrementAndGet();
        private final CyclicBarrier startingGun;

        public Runner(CyclicBarrier startingGun) {
            this.startingGun = startingGun;
        }

        @Override
        public void run() {
            System.out.println(id+":ready");
            try {
                startingGun.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(id+":start");
        }
    }
}
