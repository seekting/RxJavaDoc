package com.seekting.retrofit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 */

public class ProxyDemo {

    interface Api {
        String request(int j);

        String request1(int j);
    }

    interface Api2 {
        int request2(String s);
    }

    static class MyApi implements Api {

        @Override
        public String request(int j) {
            return "1";
        }

        @Override
        public String request1(int j) {
            return "2";
        }
    }

    static class MyApi2 implements Api2 {

        @Override
        public int request2(String s) {
            return 4;
        }
    }


    public static void main(String args[]) {
        final Api apiProxy = (Api) Proxy.newProxyInstance(Api.class.getClassLoader(), new Class[]{Api.class, Api2.class}, new MyInvocationHandler(new MyApi(), new MyApi2()));
        String t = apiProxy.request(1);
        System.out.println("t=" + t);
        t = apiProxy.request1(1);
        System.out.println("t=" + t);
        Api2 api2 = (Api2) apiProxy;
        int jj = api2.request2("4");
        System.out.println("" + jj);


    }

    static class MyInvocationHandler implements InvocationHandler {
        MyApi mMyApi;
        MyApi2 mMyApi2;

        public MyInvocationHandler(MyApi myApiApi, MyApi2 myApiApi2) {
            mMyApi = myApiApi;
            mMyApi2 = myApiApi2;
        }


        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (method.getDeclaringClass().isAssignableFrom(mMyApi.getClass())) {
                return method.invoke(mMyApi, args);
            }
            if (method.getDeclaringClass().isAssignableFrom(mMyApi2.getClass())) {
                return method.invoke(mMyApi2, args);
            }
            throw new IllegalAccessError("xxxx");
        }
    }
}
