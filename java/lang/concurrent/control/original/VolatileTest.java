package lang.concurrent.control.original;

import lang.concurrent.trick.ThreadUtil;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by yuzhm on 2016/5/26.
 */
public class VolatileTest {
    private int time=0;
    private ReadWriteLock lock=new ReentrantReadWriteLock();

    private class TimeReader implements Runnable{
        @Override
        public void run() {
            while (true) {
                lock.readLock().lock();
                System.out.println(time);
                ThreadUtil.sleep(500);
                lock.readLock().unlock();
            }
        }
    }

    private class TimeWriter implements Runnable{
        @Override
        public void run() {
            while(true){
                lock.writeLock().lock();
                time++;
                System.out.println("setTime");
                ThreadUtil.sleep(1000);
                lock.writeLock().unlock();
                ThreadUtil.sleep(1000);
            }
        }
    }

    public VolatileTest(){
        ThreadUtil.runAllServices(new TimeReader(), new TimeReader()
            , new TimeWriter(),new TimeWriter());
    }

    public static void main(String arg[]){
        new VolatileTest();
    }
}
