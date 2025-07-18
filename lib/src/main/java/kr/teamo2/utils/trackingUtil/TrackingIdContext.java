package kr.teamo2.utils.trackingUtil;

public class TrackingIdContext {

    private static final ThreadLocal<String> trackingIdHolder = new ThreadLocal<>();

    public TrackingIdContext() {
    }

    public static String getTrackingId() {
        return trackingIdHolder.get();
    }

    public static void setTrackingId(String trackingId) {
        trackingIdHolder.set(trackingId);
    }

    public static void clear() {
        trackingIdHolder.remove();
    }
}
