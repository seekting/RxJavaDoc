package com.seekting.retrofit;

import java.lang.reflect.Method;

/**
 * getDeclaredMethods返回的是当前类或接口申明的所有方法和静态方法
 *
 * getMethods 返回的是这个类及父灰的所有公开方法
 */

public class DeclaredDemo {

    public static void main(String args[]) {
        Method[] m = B.class.getDeclaredMethods();
        for (Method method : m) {
            System.out.println(method.toGenericString());

        }

        System.out.println("-------------------");
        m = B.class.getMethods();

        for (Method method : m) {
            System.out.println(method.toGenericString());

        }
    }





}
