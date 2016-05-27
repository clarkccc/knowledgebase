package lang.concurrent.data.element;

import lang.concurrent.trick.ThreadUtil;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yuzhm on 2016/5/25.
 * 查看线程池条件下使用ThreadLocal,必须初始化或清空,以免串话
 */

public class ThreadLocalInExecutorTest {

    public static void main(String arg[]) throws InterruptedException {
        ThreadUtil.ServiceBuild build = new ThreadUtil.ServiceBuild();
        build=build.withExecutor(Executors.newFixedThreadPool(3));
        for (int i = 0; i < 10; i++) {
            build.addTask(new ReanAble());
        }
        build.build();
    }

    public static class ReanAble implements Runnable{
        private static ThreadLocal<String> data=new ThreadLocal<String>();
        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            String timeToKen=new Date().toString();
            String current=threadName+"_"+timeToKen;
            if (data.get()==null) {
                System.out.println("NULL:");
            }else {
                System.out.println("LAGA:"+data.get()+" NOW:"+current);
            }
            data.set(current);
        }
    }

}
