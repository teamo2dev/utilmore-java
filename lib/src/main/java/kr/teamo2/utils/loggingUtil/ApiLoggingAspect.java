package kr.teamo2.utils.loggingUtil;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ApiLoggingAspect extends LoggingPointCut {

    private final ApiLogger apiLogger;

    @Before("logEnabled()")
    public void apiBeforeLogging(JoinPoint joinPoint) {
        apiLogger.beforeLog(joinPoint);
    }

    @AfterReturning(value = "logEnabled()", returning = "response")
    public void apiAfterLogging(JoinPoint joinPoint, ResponseEntity response) {
        apiLogger.afterLog(response, joinPoint);
    }
}
