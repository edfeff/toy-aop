package com.edfeff;

import com.edfeff.aop.Aop;
import com.edfeff.aop.test.Normal;
import com.edfeff.aop.test.TestBean;

/**
 * @author wpp
 */
public class Example {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Aop aop = new Aop();
        //注册所有的bean，包括AopBean
        aop.registerToIoc(TestBean.class);
        aop.registerToIoc(Normal.class);
        //直接从AOP中获取即可，已经被装配好了的bean
        Normal normal = aop.getBeanOfType(Normal.class);
        normal.hello();
        normal.world();
    }
}
