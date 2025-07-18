package kr.teamo2.utils.interceptor;

import java.io.IOException;
import kr.teamo2.utils.trackingUtil.TrackingIdContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

@Log4j2
public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
        HttpRequest request,
        byte[] body,
        ClientHttpRequestExecution execution) throws IOException {

        long startTime = System.currentTimeMillis();

        try {
            ClientHttpResponse response = execution.execute(request, body);
            long duration = System.currentTimeMillis() - startTime;

            String host = request.getURI().getHost();
            String fullUrl = request.getURI().toString();
            log.info("{}: {} | [{}] {} -> {} - {}ms",
                TrackingIdContext.getTrackingId(),
                response.getStatusCode().value(),
                request.getMethod(),
                fullUrl,
                host,
                duration);

            return response;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            String fullUrl = request.getURI().toString();
            log.error("{}: ERROR | [{}] {} - {}ms - {}",
                TrackingIdContext.getTrackingId(),
                request.getMethod(),
                fullUrl,
                duration,
                e.getMessage());
            throw e;
        }
    }
}
