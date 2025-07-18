package kr.teamo2.utils.trackingUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.teamo2.utils.HttpServletUtil;
import org.springframework.web.filter.OncePerRequestFilter;

public class TrackingIdFilter extends OncePerRequestFilter {

    private static final String TRACKING_ID_HEADER = "X-Tracking-ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {
        try {
            String trackingId = request.getHeader(TRACKING_ID_HEADER);
            if (trackingId == null) {
                trackingId = HttpServletUtil.generateAndSetTrackingId();
            }
            TrackingIdContext.setTrackingId(trackingId);
            filterChain.doFilter(request, response);
        } finally {
            TrackingIdContext.clear();
        }
    }
}
