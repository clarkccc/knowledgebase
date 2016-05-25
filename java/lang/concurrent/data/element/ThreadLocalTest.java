package lang.concurrent.data.element;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yuzhm on 2016/5/25.
 * 测试ThreadLocal是否存在线程安全问题,答案是不存在
 */
public class ThreadLocalTest {
    public static void main(String arg[]) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            addAThread(new ReanAble());
        }
        Thread.sleep(3000);
        System.gc();
        Thread.sleep(10000);
    }
    public static class DataBlock{
        byte[] data=new byte[10*1024*1024];
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name+":block";
        }

        @Override
        protected void finalize() throws Throwable {
            System.out.println(name+":finalize");
        }
    }

    private static ExecutorService service= Executors.newCachedThreadPool();

    public static void addAThread(Runnable runnable){
//        service.execute(runnable);
        new Thread(runnable).start();
    }

    public static class ReanAble implements Runnable{
        private static ThreadLocal<DataBlock> data=new ThreadLocal<DataBlock>();
        @Override
        public void run() {
            DataBlock dataBlock = new DataBlock();
            String threadName = Thread.currentThread().getName();
            dataBlock.setName(threadName);
            data.set(dataBlock);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(threadName +":out");
        }
    }

}
