package lang.concurrent.control.newlock;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yuzhm on 2016/5/26.
 */
public class ReadWriteLockTest {
    private String time="";
    private class TimeReader implements Runnable{
        @Override
        public void run() {
            while (true) {
                System.out.println(time);
                try {
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class TimeWriter implements Runnable{
        @Override
        public void run() {
            while(true){
                time=new Date().toString();
                System.out.println("setTime");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ReadWriteLockTest(){
        ExecutorService service=Executors.newFixedThreadPool(4);
        service.submit(new TimeReader());
        service.submit(new TimeReader());
        service.submit(new TimeWriter());
        service.submit(new TimeWriter());
        service.shutdown();
    }

    public static void main(String arg[]){
        new ReadWriteLockTest();
    }
}
