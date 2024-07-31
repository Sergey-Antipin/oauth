package com.antipin.oauth.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @After("execution(* org.springframework.security.web.authentication.AuthenticationSuccessHandler.onAuthenticationSuccess(..))")
    public void logLoginSuccess(JoinPoint joinPoint) {
        Authentication authentication = (Authentication) joinPoint.getArgs()[2];
        if (authentication != null && authentication.getName() != null) {
            log.info("User '{}' successfully logged in", authentication.getName());
        }
    }

    @After("execution(* org.springframework.security.web.authentication.AuthenticationFailureHandler.onAuthenticationFailure(..))")
    public void logLoginFailure(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 1 && args[0] instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) args[0];
            log.info("Login failed for request: {}", request);
        }
    }

    @After("execution(* org.springframework.security.web.authentication.logout.LogoutSuccessHandler.onLogoutSuccess(..))")
    public void logLogoutSuccess(JoinPoint joinPoint) {
        Authentication authentication = (Authentication) joinPoint.getArgs()[2];
        if (authentication != null && authentication.getName() != null) {
            log.info("User {} successfully logged out", authentication.getName());
        }
    }

}
