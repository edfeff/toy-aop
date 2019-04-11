package com.edfeff.aop.test;

import com.edfeff.aop.annotation.AopAfter;
import com.edfeff.aop.annotation.AopBean;
import com.edfeff.aop.annotation.AopBefore;
import com.edfeff.aop.annotation.AopException;

/**
 * @author wpp
 */
@AopBean(pointClass = "com.edfeff.aop.test.Normal")
public class TestBean {

    @AopBefore(pointCut = "hello")
    public void before() {
        System.out.println("===========before==============");
    }

    @AopAfter(pointCut = "hello")
    public void after() {
        System.out.println("==========after===============");
    }

    @AopException(pointCut = "hello")
    public void handleException() {
        System.out.println("===========exception==============");
    }

    @AopBefore(pointCut = "world")
    public void beforeWorld() {
        System.out.println("===========before==============");
    }

}
