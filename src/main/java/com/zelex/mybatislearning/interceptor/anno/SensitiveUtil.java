package com.zelex.mybatislearning.interceptor.anno;

import cn.hutool.core.util.ReflectUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SensitiveUtil {

    /**
     * 判断一个类是否敏感类，即是否有注解 @SensitiveData
     * @param clazz
     * @return
     */
    public static boolean isSensitiveClass(Class<?> clazz){
        SensitiveEntity sensitiveData = AnnotationUtils.findAnnotation(clazz,SensitiveEntity.class);
        return ObjectUtils.isNotEmpty(sensitiveData);
    }
    /**
     * 获取敏感类里面的敏感字段
     * @param clazz
     * @return
     */
    public static List<Field> getSensitiveField(Class<?> clazz){
        List<Field> list = new ArrayList<>();
        Field[] fields = ReflectUtil.getFields(clazz);
        for (Field fs: fields) {
            boolean annotationPresent = fs.isAnnotationPresent(SensitiveField.class);
            if(annotationPresent){
                list.add(fs);
            }
        }
        return list;
    }
}
