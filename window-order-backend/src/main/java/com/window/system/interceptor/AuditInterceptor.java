package com.window.system.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Properties;

@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class AuditInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        SqlCommandType sqlCommandType = ms.getSqlCommandType();

        if (parameter == null) {
            return invocation.proceed();
        }

        if (sqlCommandType == SqlCommandType.INSERT) {
            setField(parameter, "createBy", getCurrentUserId(parameter));
            setField(parameter, "updateBy", getCurrentUserId(parameter));
            setField(parameter, "isDeleted", false);
        } else if (sqlCommandType == SqlCommandType.UPDATE) {
            setField(parameter, "updateBy", getCurrentUserId(parameter));
        }

        return invocation.proceed();
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Ignore if field not exists
        }
    }

    private Long getCurrentUserId(Object parameter) {
        // Try to get currentUserId from the parameter object (Req objects usually have it)
        try {
            Field field = parameter.getClass().getDeclaredField("currentUserId");
            field.setAccessible(true);
            Object val = field.get(parameter);
            if (val != null) return (Long) val;
        } catch (Exception e) {
            // ignore
        }
        // Fallback or todo: get from ThreadLocal/SecurityContext if implemented
        return 1L; // Default system user for now if missing
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
