package lang.concurrent.control.sync;

import lang.concurrent.trick.ThreadUtil;

import java.util.concurrent.CountDownLatch;

/**
 * Created by yuzhm on 2016/5/27.
 * 类似MapReduce过程,
 */
public class CountDownLatchTest {
    public static void main(String arg[]) throws InterruptedException {
        int workerNum=5;
        CountDownLatch latch=new CountDownLatch(workerNum);
        ThreadUtil.ServiceBuild build = new ThreadUtil.ServiceBuild();
        for (int i = 0; i < workerNum; i++) {
            build.addTask(new Worker(latch));
        }
        build.build();
        System.out.println("start:over");
        latch.await();
        System.out.println("all:over");
    }
    private static class Worker implements Runnable{
        private CountDownLatch callback;
        public Worker(CountDownLatch callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()+":start");
            ThreadUtil.sleep();
            System.out.println(Thread.currentThread().getName()+":over");
            callback.countDown();
        }
    }

}
