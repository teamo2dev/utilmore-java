package kr.teamo2.utils.loggingUtil;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class ResponseLog {

    private String requestID;

    private Integer statusCode;

    private Object body;

    private boolean isError;

    @Builder(builderClassName = "InitSuccess", builderMethodName = "initSuccess")
    public ResponseLog(String requestId, Object body) {
        this.requestID = requestId;
        this.statusCode = 200;
        this.body = body;
        this.isError = false;
    }

    @Builder(builderClassName = "InitFail", builderMethodName = "initFail")
    public ResponseLog(String requestId, Object body, Integer statusCode) {
        this.requestID = requestId;
        this.statusCode = statusCode;
        this.body = body;
        this.isError = true;
    }
}

