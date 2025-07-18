package kr.teamo2.utils.loggingUtil;

import static kr.teamo2.utils.trackingUtil.TrackingIdContext.getTrackingId;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.teamo2.utils.HttpServletUtil;
import kr.teamo2.utils.trackingUtil.TrackingIdContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class ApiLogger extends LoggingPointCut {

    private final ObjectMapper objectMapper;

    public void beforeLog(JoinPoint joinPoint) {
        LogEnabled logEnabled = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(LogEnabled.class);
        String customTrackingId = logEnabled != null ? logEnabled.trackingId() : "";

        String trackingId = !customTrackingId.isEmpty() ? customTrackingId :
            (getTrackingId() != null ? getTrackingId() :
                HttpServletUtil.generateAndSetTrackingId());

        TrackingIdContext.setTrackingId(trackingId);

        Version version = logEnabled != null ? logEnabled.version() : Version.FULL;

        if (version == Version.ON_ERROR) {
            return;
        }

        requestLog(joinPoint);
    }

    public void afterLog(ResponseEntity response, JoinPoint joinPoint) {
        LogEnabled logEnabled = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(LogEnabled.class);
        Version version = logEnabled != null ? logEnabled.version() : Version.FULL;
        boolean isSuccessful = response.getStatusCode().is2xxSuccessful();

        if(version == Version.ON_ERROR){
            if(isSuccessful){
                return;
            }
            else {
                requestLog(joinPoint);
                responseLog(response);
                return;
            }
        }

        responseLog(response);
    }

    private void requestLog(JoinPoint joinPoint) {
        String trackingId = getTrackingId();
        Object[] args = joinPoint.getArgs();
        RequestLog.RequestLogBuilder requestLogBuilder = RequestLog.builder();

        for (Object o : args) {
            try {
                String requestJson = objectMapper.writeValueAsString(o);
                requestLogBuilder.request(requestJson);
            } catch (Exception e) {
                requestLogBuilder.request("Failed to serialize request: " + e.getMessage());
            }
        }
        log.info("{}: {}", trackingId, requestLogBuilder.build());
    }

    private void responseLog(ResponseEntity response) {
        String trackingId = getTrackingId();
        ResponseLog responseLog;
        Object body = response.getBody();
        String responseJson = null;
        try {
            responseJson = this.objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            responseJson = String.valueOf(body);
        }

        responseLog = ResponseLog.builder()
            .response(responseJson)
            .build();

        log.info("{}: {}", trackingId, responseLog);
    }
}
