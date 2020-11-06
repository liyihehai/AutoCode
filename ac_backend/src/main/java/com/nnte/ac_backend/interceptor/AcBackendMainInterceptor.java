package com.nnte.ac_backend.interceptor;

import com.nnte.ac_backend.controller.autoCode.AutoCodeLocalConfig;
import com.nnte.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AcBackendMainInterceptor implements HandlerInterceptor {

    @Autowired
    private AutoCodeLocalConfig autoCodeLocalConfig;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean originSet = false;
        String sourceUrl = request.getHeader("REFERER");
        String[] allowList = autoCodeLocalConfig.getFrontHosts().split(",");
        for (String allow : allowList) {
            if (StringUtils.isNotEmpty(sourceUrl)) {
                if (sourceUrl.contains(allow)) {
                    response.setHeader("Access-Control-Allow-Origin", allow);
                    originSet = true;
                    break;
                }
            }
        }
        if (!originSet)
            response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token");
        response.setHeader("Access-Control-Expose-Headers", "*");

        if (request.getMethod().equals("OPTIONS")) {
            ServletOutputStream outputStream=response.getOutputStream();
            outputStream.write(200);
            outputStream.close();
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
