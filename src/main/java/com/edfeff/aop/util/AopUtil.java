package com.edfeff.aop.util;

import com.edfeff.aop.annotation.AopBean;

/**
 * @author wpp
 */
public class AopUtil {
    public static String getPointClass(Object source) {
        return (String) AnnotationUtil.getAnnotationValue(source, AopBean.class, "pointClass");
    }
}
