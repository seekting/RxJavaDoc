# getMethods与getDeclaredMethods区别

## java代码
A.java
```java
package com.seekting.retrofit;

/**
 */

public class A {
    public void publicA() {

    }

    void packagedA() {

    }

    protected void protectedA() {

    }

    private void privateA() {

    }

    public static void publicStaticA() {

    }

    protected static void protectedStaticA() {

    }

    static void packageStaticA() {

    }

    private static void privateStaticA() {

    }

}


```
B.java
```java
public class B {
    public static void publicStaticB() {

    }

    protected static void protectedStaticB() {

    }

    static void packageStaticB() {

    }

    private static void privateStaticB() {

    }

    public void publicB() {

    }

    void packagedB() {

    }

    protected void protectedB() {

    }

    private void privateB() {

    }
}


```
demo
```java
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


```


打印效果:
```Console
public static void com.seekting.retrofit.B.publicStaticB()
protected static void com.seekting.retrofit.B.protectedStaticB()
static void com.seekting.retrofit.B.packageStaticB()
private static void com.seekting.retrofit.B.privateStaticB()
public void com.seekting.retrofit.B.publicB()
void com.seekting.retrofit.B.packagedB()
protected void com.seekting.retrofit.B.protectedB()
private void com.seekting.retrofit.B.privateB()
-------------------
public static void com.seekting.retrofit.B.publicStaticB()
public void com.seekting.retrofit.B.publicB()
public final void java.lang.Object.wait() throws java.lang.InterruptedException
public final void java.lang.Object.wait(long,int) throws java.lang.InterruptedException
public final native void java.lang.Object.wait(long) throws java.lang.InterruptedException
public boolean java.lang.Object.equals(java.lang.Object)
public java.lang.String java.lang.Object.toString()
public native int java.lang.Object.hashCode()
public final native java.lang.Class<?> java.lang.Object.getClass()
public final native void java.lang.Object.notify()
public final native void java.lang.Object.notifyAll()

Process finished with exit code 0


```