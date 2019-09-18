package com.nnte.ac_backend.controller.autoCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Component
public class AutoCodeInterceptor implements HandlerInterceptor {
    @Autowired
    private AutoCodeLocalConfig autoCodeLocalConfig;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //请求进入这个拦截器
        HttpSession session = request.getSession();
        if (session.getAttribute("envData")==null) {
            Map<String, Object> envData = new HashMap<>();
            envData.put("debug", autoCodeLocalConfig.getDebug().toUpperCase());
            envData.put("staticRoot", autoCodeLocalConfig.getStaticRoot());
            session.setAttribute("envData", envData);
        }
        return true;        //有的话就继续操作
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
