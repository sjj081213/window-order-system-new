package com.window.system.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.window.system.annotation.Log;
import com.window.system.mapper.SysOperationLogMapper;
import com.window.system.model.entity.SysOperationLog;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.window.system.util.IpUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.window.system.security.AuthUser;

import java.lang.reflect.Method;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private SysOperationLogMapper logMapper;
    
    @Autowired
    private ObjectMapper objectMapper;

    @AfterReturning(pointcut = "@annotation(com.window.system.annotation.Log)", returning = "jsonResult")
    public void recordLog(JoinPoint joinPoint, Object jsonResult) {
        try {
            SysOperationLog opLog = new SysOperationLog();
            
            // Annotation info
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Log logAnnotation = method.getAnnotation(Log.class);
            if (logAnnotation != null) {
                opLog.setModule(logAnnotation.module());
                opLog.setOperation(logAnnotation.operation());
            }

            // Method info
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = signature.getName();
            opLog.setMethod(className + "." + methodName + "()");

            // Request params (first arg)
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                try {
                    String params = objectMapper.writeValueAsString(args[0]);
                    opLog.setParams(params.length() > 2000 ? params.substring(0, 2000) : params);
                } catch (Exception e) {
                    opLog.setParams("args serialization failed");
                }
            }

            // User from security context
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof AuthUser au) {
                opLog.setUserId(au.getId());
                opLog.setUsername(au.getUsername());
            }

            // IP
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                opLog.setIp(IpUtils.getClientIp(request));
            }

            logMapper.insert(opLog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
