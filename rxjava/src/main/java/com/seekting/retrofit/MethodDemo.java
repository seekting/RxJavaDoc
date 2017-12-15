package com.seekting.retrofit;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * Created by Administrator on 2017/12/14.
 */

public class MethodDemo {


    public static void main(String args[]) {

        TypeVariable<Class<Box>> j = Box.class.getTypeParameters()[0];
        System.out.println(j.getName());
        Method[] methods = Api.class.getMethods();
        for (Method method : methods) {
            System.out.println(method.getReturnType());
            System.out.println(method.getGenericReturnType());
            Type[] genericParameterTypes = method.getGenericParameterTypes();
            Type[] parameterTypes = method.getParameterTypes();

            Type type = method.getGenericReturnType();
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type type0 = parameterizedType.getActualTypeArguments()[0];
                Type rawType = parameterizedType.getRawType();
                Type ownerType = parameterizedType.getOwnerType();
                System.out.println("type0=" + type0);
                System.out.println("rawType=" + rawType);
                System.out.println("ownerType=" + ownerType);
            }
        }

    }

}

interface Api {
     Box<String> login(String userName, String pwd);
}

class Box<T> {
    T t;

    public Box(T t) {
        this.t = t;
    }
}
