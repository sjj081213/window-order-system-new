package com.window.system.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
public class IpUtils {

    public static HttpServletRequest getHttpServletRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (ra instanceof ServletRequestAttributes) {
            ServletRequestAttributes sra = (ServletRequestAttributes)ra;
            return sra.getRequest();
        } else {
            return null;
        }
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = headerIp(request, "X-Forwarded-For");
        if (isNotEmpty(ip)) {
            int idx = ip.indexOf(',');
            if (idx > 0) {
                ip = ip.substring(0, idx).trim();
            }
            return ip;
        }
        ip = headerIp(request, "X-Real-IP");
        if (isNotEmpty(ip)) return ip;
        ip = headerIp(request, "Proxy-Client-IP");
        if (isNotEmpty(ip)) return ip;
        ip = headerIp(request, "WL-Proxy-Client-IP");
        if (isNotEmpty(ip)) return ip;
        ip = headerIp(request, "HTTP_CLIENT_IP");
        if (isNotEmpty(ip)) return ip;
        ip = headerIp(request, "HTTP_X_FORWARDED_FOR");
        if (isNotEmpty(ip)) return ip;
        ip = request.getRemoteAddr();
        return ip;
    }

    private static String headerIp(HttpServletRequest request, String name) {
        String v = request.getHeader(name);
        if (v == null) return null;
        v = v.trim();
        if (v.length() == 0) return null;
        if ("unknown".equalsIgnoreCase(v)) return null;
        return v;
    }

    private static boolean isNotEmpty(String s) {
        return s != null && s.length() > 0;
    }


}
