package lang.concurrent.control;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by yuzhm on 2016/5/25.
 * 线程级别锁,不用对象实现,线程随时停止,等待线程引用的持有者对其unpark后恢复
 */
public class LockSurportTest {
    public static void main(String arg[]) throws InterruptedException {
        Thread aSuspendedThread = createASuspendedThread();
        Thread aSuspendedThread1 = createASuspendedThread();
        LockSupport.unpark(aSuspendedThread);
        Thread.sleep(1000);
        LockSupport.unpark(aSuspendedThread1);
    }
    public static Thread createASuspendedThread(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+":park");
                LockSupport.park();
                System.out.println(Thread.currentThread().getName()+":unpark");
            }
        });
        thread.start();
        return thread;
    }
}
