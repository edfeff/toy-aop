package com.edfeff.aop;

import com.edfeff.aop.annotation.AopAfter;
import com.edfeff.aop.annotation.AopBean;
import com.edfeff.aop.annotation.AopBefore;
import com.edfeff.aop.annotation.AopException;
import com.edfeff.aop.test.Normal;
import com.edfeff.aop.test.TestBean;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 写一个Aop
 * <p>
 * 使用注解
 * 0.@AopBean
 * 1.@AopBefore
 * 2.@AopAfter
 * 3.@AopException
 *
 * @author wpp
 */

public class Aop {
    Set<Class<?>> typeSet = new HashSet<>();
    Map<String, Class<?>> aopMap = new HashMap<>();

    public void registerToIoc(Class<?> value) {
        typeSet.add(value);
        AopBean annotation = value.getAnnotation(AopBean.class);
        if (annotation != null) {
            aopMap.put(annotation.pointClass(), value);
        }
    }

    public <T> T getBeanOfType(Class<T> type) throws IllegalAccessException, InstantiationException {
        Class<?> aop = aopMap.get(type.getName());
        final Object aopObj = aop.newInstance();
        Object o = null;
        if (aop != null) {

            Set<String> beforeMethods = new HashSet<>();
            Set<String> afterMethods = new HashSet<>();
            Set<String> exceptionMethods = new HashSet<>();

            Map<String, List<Method>> beforeMethodMap = new HashMap<>();
            Map<String, List<Method>> afterMethodMap = new HashMap<>();
            Map<String, List<Method>> exceptionMethodMap = new HashMap<>();

            Method[] methods = aop.getMethods();
            for (Method m : methods) {
                m.setAccessible(true);
                AopBefore beforeAnnotation = m.getAnnotation(AopBefore.class);
                if (beforeAnnotation != null) {
                    init(beforeAnnotation.pointCut(), beforeMethods, beforeMethodMap, m);
                }
                AopAfter afterAnnotation = m.getAnnotation(AopAfter.class);
                if (afterAnnotation != null) {
                    init(afterAnnotation.pointCut(), afterMethods, afterMethodMap, m);
                }
                AopException aopException = m.getAnnotation(AopException.class);
                if (aopException != null) {
                    init(aopException.pointCut(), exceptionMethods, exceptionMethodMap, m);
                }
            }
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(type);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    boolean noError = true;
                    //前
                    if (beforeMethods.contains(method.getName())) {
                        invokeMethods(aopObj, beforeMethodMap, method.getName(), objects);
                    }
                    Object result = null;
                    try {
                        result = methodProxy.invokeSuper(o, objects);
                    } catch (Throwable throwable) {
                        noError = false;
                        //异常后处理
                        if (exceptionMethods.contains(method.getName()) && null != exceptionMethodMap.get(method.getName())) {
                            invokeMethods(aopObj, exceptionMethodMap, method.getName(), objects);
                        } else {
                            throw throwable;
                        }
                    }
                    if (noError) {
                        //正常后处理
                        if (afterMethods.contains(method.getName())) {
                            invokeMethods(aopObj, afterMethodMap, method.getName(), objects);
                        }
                    }
                    return result;
                }

                private void invokeMethods(Object aop, Map<String, List<Method>> map, String name, Object[] objects) throws InvocationTargetException, IllegalAccessException {
                    List<Method> list = map.get(name);
                    for (Method m : list) {
                        m.invoke(aop, objects);
                    }
                }
            });
            o = enhancer.create();
        } else {
            try {
                o = type.newInstance();
            } catch (Exception e) {
            }
        }
        return (T) o;
    }

    public void init(String pointCut, Set<String> set, Map<String, List<Method>> map, Method m) {
        set.add(pointCut);
        if (map.containsKey(pointCut)) {
            map.get(pointCut).add(m);
        } else {
            List<Method> list = new ArrayList<>();
            list.add(m);
            map.put(pointCut, list);
        }
    }


}
