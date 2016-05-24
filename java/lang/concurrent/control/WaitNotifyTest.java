package lang.concurrent.control;

/**
 * Created by yuzhm on 2016/5/24.
 */
public class WaitNotifyTest {
    public static void main(String arg[]){
        final Object lock=new Object();

        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                        System.out.println("A:"+1);
                }

                //先获得锁才能对对象进行notify和wait操作
                synchronized (lock) {
                    lock.notify();
                    try { lock.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
                }
                System.out.println("A:"+3);
            }
        });

        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (lock) {
                    //申请wait,放弃同步锁然后进行等待,别人可以获得lock
                    try { lock.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
                    System.out.println("B:1");
                    lock.notify();
                }

            }
        });
        threadA.start();
        threadB.start();
    }
}
