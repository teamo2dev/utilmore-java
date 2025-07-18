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

    private Object response;

    private boolean isError;

    @Builder
    public ResponseLog(Object response) {
        this.response = response;
        this.isError = false;
    }
}

