package com.edfeff.aop.test;

/**
 * 被aop包裹的类
 *
 * @author wpp
 */
public class Normal {
    public void hello() {
        System.out.println("hello");
        throw new RuntimeException();
    }

    public void world() {
        System.out.println("world");
    }
}
