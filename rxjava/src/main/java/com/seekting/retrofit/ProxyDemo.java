package com.seekting.retrofit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2017/12/13.
 */

public class ProxyDemo {

    interface Api {
        String request(int j);

        String request1(int j);
    }

    interface Api2 {
        int request2(String s);
    }

    static class MyYourApi implements Api {

        @Override
        public String request(int j) {
            return "1";
        }

        @Override
        public String request1(int j) {
            return "2";
        }
    }

    static class MyYourApi2 implements Api2 {

        @Override
        public int request2(String s) {
            return 4;
        }
    }

    static Object object;

    public static void main(String args[]) {
        final Api apiProxy = (Api) Proxy.newProxyInstance(Api.class.getClassLoader(), new Class[]{Api.class, Api2.class}, new MyInvocationHandler(new MyYourApi(), new MyYourApi2()));
        String t = apiProxy.request(1);
        System.out.println("t=" + t);
        t = apiProxy.request1(1);
        System.out.println("t=" + t);
        Api2 api2 = (Api2) apiProxy;
        int jj = api2.request2("4");
        System.out.println("" + jj);

//        System.out.println(object);


    }

    static class MyInvocationHandler implements InvocationHandler {
        MyYourApi mMyYourApi;
        MyYourApi2 mMyYourApi2;

        public MyInvocationHandler(MyYourApi myYourApi, MyYourApi2 myYourApi2) {
            mMyYourApi = myYourApi;
            mMyYourApi2 = myYourApi2;
        }


        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (method.getDeclaringClass().isAssignableFrom(mMyYourApi.getClass())) {
                return method.invoke(mMyYourApi, args);
            }
            if (method.getDeclaringClass().isAssignableFrom(mMyYourApi2.getClass())) {
                return method.invoke(mMyYourApi2, args);
            }
            throw new IllegalAccessError("xxxx");
        }
    }
}
