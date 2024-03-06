package com.heu.interceptor;

import com.alibaba.fastjson.JSON;
import com.heu.common.BaseContext;
import com.heu.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.ibatis.io.SerialFilterChecker.check;


@Slf4j
//@Component
public class LoginInterceptor implements HandlerInterceptor {
    // 路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取本次请求URI
        String uri = request.getRequestURI();
        log.info("拦截到请求:{}",uri);

        // 判断登陆状态
        Long emp = (Long) request.getSession().getAttribute("emp");
        if(emp != null){
            // 用户登录,放行
            log.info("用户id：{}",emp);
            // 将登陆者id存入线程变量副本
            BaseContext.setCurrentId(emp);
            return true;
        }

        // 用户未登录
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return false;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
