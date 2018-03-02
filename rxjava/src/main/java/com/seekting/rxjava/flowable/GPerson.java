package com.seekting.rxjava.flowable;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/12/11.
 */

public class GPerson {
    String name;
    int age;

    public GPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public static void main(String args[]) {
        Gson gson = new Gson();
        GPerson gPerson = new GPerson("XiaoMing", 30);

        System.out.println(gPerson.getClass().getTypeName());
        System.out.println(Cat.class.getTypeName());
        String s = gson.toJson(gPerson);
//        System.out.println("s=" + s);

        String ss = "xx";
        s = gson.toJson(ss);
        System.out.println("s=" + s);

    }

    static class Cat {
        String name;
        int age;
    }

}
