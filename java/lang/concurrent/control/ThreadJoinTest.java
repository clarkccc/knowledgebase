package lang.concurrent.control;

/**
 * Created by yuzhm on 2016/5/24.
 */
public class ThreadJoinTest {
    public static void main(String arg[]){
        final Thread threadB=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("B:2");
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("A:1");
                try {
                    threadB.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("A:3");
            }
        }).start();

    }
}
