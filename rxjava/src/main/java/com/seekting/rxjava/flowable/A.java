package com.seekting.rxjava.flowable;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/11/30.
 */

public class A{
    private static HashMap<Integer,String> b = new HashMap<Integer,String>();
    private static A  a = new A();

    private A(){
        b.put(123,"example");
    }
    public static A getInstance(){
        return a;
    }
    public static void main(String args[]){

        A.getInstance();
    }
}