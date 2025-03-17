package org.example.expert.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AdminAccessLoggingAspect {

    private final HttpServletRequest request;

    @Before("execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public void logAfterChangeUserRole(JoinPoint joinPoint) {
        String userId = String.valueOf(request.getAttribute("userId"));
        String requestUrl = request.getRequestURI();
        LocalDateTime requestTime = LocalDateTime.now();

        log.info("Admin Access Log - User ID: {}, Request Time: {}, Request URL: {}, Method: {}",
                userId, requestTime, requestUrl, joinPoint.getSignature().getName());
    }

    @Around("execution(* org.example.expert.domain.manager.controller.ManagerController.saveManager(..))")
    public Object logAroundSaveManager(ProceedingJoinPoint joinPoint) throws Throwable {
        String userId = String.valueOf(request.getAttribute("managerUserId"));
        String requestUrl = request.getRequestURI();
        LocalDateTime requestTime = LocalDateTime.now();
        log.info("Manager Register Log - User ID: {}, Request Time: {}, Request URL: {}, Method: {}",
                userId, requestTime, requestUrl, joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        if (result instanceof ManagerSaveResponse response) {
            userId = String.valueOf(response.getId());
            LocalDateTime responseTime = LocalDateTime.now();
            log.info("Manager Register Log - User ID: {}, Request Time: {}, Request URL: {}, Method: {}",
                    userId, responseTime, requestUrl, joinPoint.getSignature().getName());
        }
        return result;
    }
}
