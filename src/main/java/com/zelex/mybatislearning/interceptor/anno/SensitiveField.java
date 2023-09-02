package com.zelex.mybatislearning.interceptor.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标在实体类的字段上 OR Mapper请求参数上
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface SensitiveField {
    /**
     * 参数名称，适用于放在mapper入参上，且入参上有@Param注解时
     * 值应该和@Param的值保持一致
     * @return
     */
    String paramName() default "";
}


