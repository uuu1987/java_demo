package com.example.demo.member;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if ("GET".equals(request.getMethod()) && request.getRequestURI().startsWith("/posts"))
        {
            return true;
        }

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userID") == null) {
            response.setStatus(401);
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write("로그인 필요");
            return false;
        }

        return true;
    }

} 
