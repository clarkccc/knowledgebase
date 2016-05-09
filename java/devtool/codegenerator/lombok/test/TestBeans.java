package test;

import beans.DataBean;

/**
 * Created by yuzhm on 2016/5/9.
 */
public class TestBeans {
    public static void main(String arg[]){
        DataBean bean=new DataBean(true,1,"test");
        System.out.println(bean);
    }
}
