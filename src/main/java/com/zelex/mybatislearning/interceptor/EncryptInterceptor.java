package com.zelex.mybatislearning.interceptor;


import cn.hutool.core.util.ReflectUtil;
import com.zelex.mybatislearning.interceptor.anno.SensitiveField;
import com.zelex.mybatislearning.interceptor.anno.SensitiveUtil;
import com.zelex.mybatislearning.util.EnDecrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.PreparedStatement;
import java.util.*;


@Slf4j
@Component
@Intercepts({
        @Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class})
})
public class EncryptInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();//被代理对象
        Object parameterObject = parameterHandler.getParameterObject();
        if (null == parameterObject || parameterObject instanceof String) {
           return invocation.proceed();
        }
        // 判断并进行加密操作
        encryptFiled(parameterHandler, parameterObject);
        return invocation.proceed();
    }

    private void encryptFiled(ParameterHandler parameterHandler, Object sourceObject) throws NoSuchFieldException, ClassNotFoundException {
        if (null == sourceObject) {
            return;
        }
        // 这个case最为复杂，适用于map的update情况,即需要根据key,去判断是否要加密
        if (sourceObject instanceof Map) {
            Map<String, Object> mapObj = (Map<String, Object>) sourceObject;
            //先把"param"开头的过滤掉
            for (Map.Entry<String, Object> entry : mapObj.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof String) {
                    if (!key.startsWith("param") && Objects.nonNull(key)) {
                        handleString(parameterHandler, entry);
                    }
                } else {
                    // obj case 递归分支
                    encryptFiled(parameterHandler, value);
                }
            }
            return;
        }

        if (sourceObject instanceof List) {
            List<Object> objList = (List<Object>) sourceObject;
            for (Object o : objList) {
                encryptFiled(parameterHandler, o);
            }
            return;
        }

        Class<?> clazz = sourceObject.getClass();
        if (!SensitiveUtil.isSensitiveClass(clazz)) {
            return;
        }

        // object case
        try {
            // 获取满足加密注解条件的字段
            List<Field> sensitiveFields = SensitiveUtil.getSensitiveField(clazz);
            for (Field sensitiveField : sensitiveFields) {
                sensitiveField.setAccessible(true);
                String sourceStr = (String) sensitiveField.get(sourceObject);
                String encryptStr = EnDecrUtil.encrypt(sourceStr, null);
                sensitiveField.set(sourceObject, encryptStr);
                log.info("开始进行加密操作，加密字段：{}, 原始值:{}, 加密后的值:{}", sensitiveField.getName(), sourceStr, encryptStr);
            }
        } catch (Exception e) {}
    }

    private void handleString(ParameterHandler parameterHandler, Map.Entry<String, Object> entry) throws NoSuchFieldException, ClassNotFoundException {
        String key = entry.getKey();
        Class<? extends ParameterHandler> aClass = parameterHandler.getClass();
        Field mappedStatement = aClass.getDeclaredField("mappedStatement");
        ReflectUtil.setAccessible(mappedStatement);
        MappedStatement statement = (MappedStatement) ReflectUtil.getFieldValue(parameterHandler, mappedStatement);
        //方法命名空间
        String nameSpace = statement.getId();
        if (StringUtils.isBlank(nameSpace)) {
            return;
        }
        String methodName = nameSpace.substring(nameSpace.lastIndexOf(".") + 1);
        String className = nameSpace.substring(0, nameSpace.lastIndexOf("."));

        Method[] ms = Class.forName(className).getMethods();
        Optional<Method> optionalMethod = Arrays.stream(ms).filter(item -> StringUtils.equals(item.getName(), methodName)).findFirst();
        if (!optionalMethod.isPresent()) {
            return;
        }

        // TODO 需要根据Key和mappedStatement去判断是否要加密
        Method method = optionalMethod.get();
        for (Parameter parameter : method.getParameters()) {
            String name = parameter.getName();
            // 更新操作且带加密注解，才进行更新
            if (name.equals(key)
                    && parameter.isAnnotationPresent(SensitiveField.class)
                    && statement.getSqlCommandType().equals(SqlCommandType.UPDATE)) {
                String originStr = (String) entry.getValue();
                String encryptStr = EnDecrUtil.encrypt(originStr, null);
                log.info("开始进行加密操作，加密字段：{}, 原始值:{}, 加密后的值:{}", name, originStr, encryptStr);
                entry.setValue(encryptStr);
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
