package kr.teamo2.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpServletUtil {

    public static final String INTERNAL_REQUEST_ID_HEADER_KEY = "X-Internal-Request-ID";

    public static String generateAndSetTrackingId() {
        String trackingId = UUID.randomUUID().toString();
        Optional
            .ofNullable(getHttpServletRequest())
            .ifPresent(request -> request.setAttribute(INTERNAL_REQUEST_ID_HEADER_KEY, trackingId));
        return trackingId;
    }

    private static HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        } catch (IllegalStateException ignored) {
            return null;
        }
    }

    public static String getTrackingId() {
        return Optional
            .ofNullable(getHttpServletRequest())
            .flatMap(httpServletRequest -> Optional
                .ofNullable(httpServletRequest.getAttribute(INTERNAL_REQUEST_ID_HEADER_KEY))
                .map(Object::toString))
            .orElse(null);
    }

    public static Map<String, String> requestToHeaderMap() {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return new HashMap<>();
        }
        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames == null) {
            return new HashMap<>();
        }
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headerMap.put(headerName, headerValue);
        }
        return headerMap;
    }

    public static String getHttpMethod() {
        return Optional
            .ofNullable(getHttpServletRequest())
            .map(HttpServletRequest::getMethod)
            .orElse(null);
    }

    public static String getUrlAndQueryString() {
        return Optional
            .ofNullable(getHttpServletRequest())
            .map(request -> request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""))
            .orElse(null);
    }
}

