package kr.teamo2.utils.loggingUtil;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ApiLoggingAspect extends LoggingPointCut {

    private final ApiLogger apiLogger;

    @Before("logEnabled()")
    public void apiBeforeLogging(JoinPoint joinPoint) {
        Class<?> returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType();
        if (ResponseEntity.class.isAssignableFrom(returnType)) {
            apiLogger.beforeLog(joinPoint);
        }
    }

    @AfterReturning(value = "logEnabled()", returning = "response")
    public void apiAfterLogging(ResponseEntity response) {
        apiLogger.afterLog(response);
    }
}
