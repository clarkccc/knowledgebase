package lang.concurrent.trick;

/**
 * Created by yuzhm on 2016/5/25.
 * 主动造成死锁的代码,连续申请两个锁,但顺序相反
 */
public class DeadLockTest {
    public static void main(String arg[]){
        final Object lock1="lock1";
        final Object lock2="lock2";
        genDoubleLock(lock1,lock2).start();
        genDoubleLock(lock2,lock1).start();
    }
    public static Thread genDoubleLock(final Object inner,final Object outter){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (outter){
                    System.out.println(Thread.currentThread().getName()+":getlock "+outter);
                    synchronized (inner){
                        try {
                            inner.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
