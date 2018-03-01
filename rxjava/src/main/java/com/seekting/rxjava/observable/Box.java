package com.seekting.rxjava.observable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/12/27.
 */

public class Box<T> {

    public static void main(String args[]) {

        MyBox myBox = new MyBox();
        Type type = myBox.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] a = parameterizedType.getActualTypeArguments();
            if (a.length > 0) {
                System.out.println(a[0] == String.class);
            }


        }


    }

    static class MyBox extends Box<String> {

    }

}
