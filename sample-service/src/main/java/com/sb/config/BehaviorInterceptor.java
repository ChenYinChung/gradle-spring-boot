package com.sb.config;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

@Aspect
@Component
public class BehaviorInterceptor {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HttpServletRequest request;

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    public BehaviorInterceptor(HttpServletRequest request) {
        this.request = request;
    }

    @Pointcut("execution(public * com.sb.controller..*.*(..))")
    public void behaviroLog(){}

    @Before("behaviroLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());


        logger.info("Entering in Method :  " + joinPoint.getSignature().getName());
        logger.info("Class Name :  " + joinPoint.getSignature().getDeclaringTypeName());
        logger.info("Arguments :  " + Arrays.toString(joinPoint.getArgs()));
        logger.info("Target class : " + joinPoint.getTarget().getClass().getName());

        if (null != request) {
            logger.info("Start Header Section of request ");
            logger.info("Method Type : " + request.getMethod());
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = (String)headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                logger.info("Header Name: " + headerName + " Header Value : " + headerValue);
            }
            logger.info("Request Path info :" + request.getServletPath());
            logger.info("End Header Section of request ");
        }

    }

    @AfterReturning(returning = "ret", pointcut = "behaviroLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        logger.info("RESPONSE : " + ret);
        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
    }

    @Around("behaviroLog()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        try {
            String className = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            logger.info("Method " + className + "." + methodName + " ()" + " execution time : "
                    + elapsedTime + " ms");

            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in "
                    + joinPoint.getSignature().getName() + "()");
            throw e;
        }
    }
}
