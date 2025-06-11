package kr.teamo2.utils.loggingUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.teamo2.utils.HttpServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class ApiLogger extends LoggingPointCut {

    private final ObjectMapper objectMapper;

    public void beforeLog(JoinPoint joinPoint) {
        String trackingId = HttpServletUtil.generateAndSetTrackingId();

        Object[] args = joinPoint.getArgs();

        String urlAndQueryString = HttpServletUtil.getUrlAndQueryString();
        RequestLog.RequestLogBuilder requestLogBuilder = RequestLog.builder()
            .requestID(trackingId)
            .url(urlAndQueryString)
            .method(HttpServletUtil.getHttpMethod())
            .header(HttpServletUtil.requestToHeaderMap());

        for (Object o : args) {
            if (HttpServletUtil.getHttpMethod().equals(HttpMethod.POST.name())) {
                try {
                    String bodyJson = objectMapper.writeValueAsString(o);
                    requestLogBuilder.body(bodyJson);
                } catch (Exception e) {
                    requestLogBuilder.body("Failed to serialize body: " + e.getMessage());
                }
            }
        }
        log.info("[ApiLogger] - {}", requestLogBuilder.build());
    }

    public void afterLog(ResponseEntity response) {
        ResponseLog responseLog;
        boolean isSuccessful = response.getStatusCode().is2xxSuccessful();

        Object body = response.getBody();
        String bodyJson = null;

        try {
            bodyJson = this.objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            bodyJson = String.valueOf(body);
        }

        responseLog = isSuccessful
            ? ResponseLog.initSuccess()
            .requestId(HttpServletUtil.getTrackingId())
            .body(bodyJson)
            .build()
            : ResponseLog.initFail()
                .requestId(HttpServletUtil.getTrackingId())
                .body(bodyJson)
                .statusCode(response.getStatusCode().value())
                .build();

        log.info("[ApiLogger] - {}", responseLog);
    }
}
