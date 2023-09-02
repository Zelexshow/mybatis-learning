package com.zelex.mybatislearning.interceptor;

import com.zelex.mybatislearning.interceptor.anno.SensitiveUtil;
import com.zelex.mybatislearning.util.EnDecrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 读操作时采用的策略
 */
@Slf4j
@Component
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class DecryptInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        // 实现解密
        decryptFiled(result);
        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }

    private void decryptFiled(Object sourceObject) {

        if (null == sourceObject) {
            return;
        }
        if (sourceObject instanceof Map) {
            // TODO 目前只支持简单DO返回，所以不考虑Map实现
        }
        if (sourceObject instanceof List) {
            ((List<?>) sourceObject).stream().forEach(this::decryptFiled);
            return;
        }
        Class<?> clazz = sourceObject.getClass();

        if (!SensitiveUtil.isSensitiveClass(clazz)) {
            return;
        }
        try {
            List<Field> sensitiveFields = SensitiveUtil.getSensitiveField(clazz);
            for (Field sensitiveField : sensitiveFields) {
                sensitiveField.setAccessible(true);
                String sourceStr = (String) sensitiveField.get(sourceObject);
                String decryptStr = EnDecrUtil.decrypt(sourceStr, null);
                sensitiveField.set(sourceObject, decryptStr);
                log.info("开始进行解密操作，解密字段：{}, 原始值:{}, 解密后的值:{}", sensitiveField.getName(), sourceStr, decryptStr);
            }
        } catch (Exception e) {
        }

    }
}
