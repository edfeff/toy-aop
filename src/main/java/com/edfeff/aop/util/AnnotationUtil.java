package com.edfeff.aop.util;

import com.edfeff.aop.annotation.AopBefore;
import com.edfeff.aop.test.TestBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wpp
 */
public class AnnotationUtil {
    public static boolean hasAnnotationType(Class<?> source, Class<? extends Annotation> annotationType) {
        Annotation[] annotationsByType = source.getAnnotationsByType(annotationType);
        if (annotationsByType == null || annotationsByType.length == 0) {
            return false;
        }
        return true;
    }

    public static Map<String, Object> getAttributesOfAnnotation(Class<?> source, Class<? extends Annotation> annotationType) {
        Annotation annotation = source.getAnnotation(annotationType);
        if (annotation == null) return null;
        Map<String, Object> map = new HashMap<>();
        Method[] declaredMethods = annotationType.getDeclaredMethods();
        try {
            for (Method method : declaredMethods) {
                method.setAccessible(true);
                map.put(method.getName(), method.invoke(annotation));
            }
        } catch (Exception e) {
        }
        return map;
    }

    public static Map<String, Object> getAttributesOfAnnotation(Object source, Class<? extends Annotation> annotationType) {
        return getAttributesOfAnnotation(source.getClass(), annotationType);
    }

    public static Object getAnnotationValue(Object source, Class<? extends Annotation> annotationType, String value) {
        Map<String, Object> allAttributesOfAnnotation = getAttributesOfAnnotation(source, annotationType);
        return allAttributesOfAnnotation.get(value);
    }

    public static List<Method> getMethodsOfAnnotation(Object source, Class<? extends Annotation> annotationType) {
        Method[] declaredMethods = source.getClass().getDeclaredMethods();
        List<Method> list = new ArrayList<>();
        for (Method method : declaredMethods) {
            Annotation annotation = method.getAnnotation(annotationType);
            if (annotation != null) {
                method.setAccessible(true);
                list.add(method);
            }
        }
        return list;
    }

    public static List<Method> getMethods(Object source, String method) {
        return null;
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        TestBean bean = new TestBean();
        List<Method> methodsOfAnnotation = getMethodsOfAnnotation(bean, AopBefore.class);
        Method method = methodsOfAnnotation.get(0);
        System.out.println(method.getName());
        System.out.println(methodsOfAnnotation.size());

    }
}
