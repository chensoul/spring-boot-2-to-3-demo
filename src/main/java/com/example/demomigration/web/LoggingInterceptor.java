package com.example.demomigration.web;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * javax.servlet.* (Boot 2) → jakarta.servlet.* (Boot 3).
 * If this extended HandlerInterceptorAdapter (Boot 2), recipe would migrate to HandlerInterceptor (Boot 3).
 */
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }
}
