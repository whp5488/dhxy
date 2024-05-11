
package com.kitty.common.interceptor;

import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;

public class MyInterceptor implements HandlerInterceptor
{
    Logger logger;
    
    public MyInterceptor() {
        this.logger = LoggerFactory.getLogger(MyInterceptor.class);
    }
    
    public boolean preHandle(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o) throws Exception {
        this.logger.info("preHandle被调用");
        return true;
    }
    
    public void postHandle(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o, final ModelAndView modelAndView) throws Exception {
        this.logger.info("postHandle被调用");
    }
    
    public void afterCompletion(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o, final Exception e) throws Exception {
        this.logger.info("-----------afterCompletion被调用");
    }
}
