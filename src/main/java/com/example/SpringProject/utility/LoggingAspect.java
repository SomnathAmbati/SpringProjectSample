package com.example.SpringProject.utility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.example.SpringProject.Exception.ICinemaException;

@Aspect
@Component
public class LoggingAspect {

    private static final Log LOGGER = LogFactory.getLog(LoggingAspect.class);

    @AfterThrowing(
            pointcut = "execution(* com.example.SpringProject..*Impl.*(..))" ,
            throwing = "exception"
    )
    public void logServiceException(ICinemaException exception) {
        LOGGER.error(exception.getMessage(), exception);
    }
}