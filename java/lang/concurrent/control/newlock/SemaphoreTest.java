package lang.concurrent.control.newlock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by yuzhm on 2016/5/26.
 * 办事柜台逻辑,5个服务窗口,一个等待队列
 * 是允许并发,但不允许随便乱来的数据结构
 */
public class SemaphoreTest {
    public static void main(String arg[]){
        Semaphore multiLock=new Semaphore(3);
        ExecutorService ser= Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            ser.submit(newTask(multiLock));
        }
        ser.shutdown();
    }
    public static Runnable newTask(final Semaphore lock){
        final long timeStart=System.currentTimeMillis();
        return new Runnable() {
            @Override
            public void run() {
                try {
                    lock.acquire();
                    long waitTimeInSeconde = TimeUnit.SECONDS.convert(
                        System.currentTimeMillis() - timeStart, TimeUnit.MILLISECONDS);
                    System.out.println("wait:"+waitTimeInSeconde);
                    Thread.sleep(1010);
                    lock.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
