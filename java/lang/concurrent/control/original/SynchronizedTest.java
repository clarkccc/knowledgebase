package lang.concurrent.control.original;

/**
 * Created by yuzhm on 2016/5/24.
 */
public class SynchronizedTest {
    public static void main(String arg[]){
        final Object lock=new Object();
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("A:"+1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //获取对象锁,由于这里被B占用这里是用不了的
                synchronized (lock) {
                    System.out.println("A:"+3);
                }
            }
        });
        final Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    //由于这里没有使用wait,没有放弃锁
                    try {
                        Thread.currentThread().sleep(1000000000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadA.start();
        threadB.start();
    }
}
