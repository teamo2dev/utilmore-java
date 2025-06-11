package kr.teamo2.utils.validateUtil;

import lombok.Getter;

@Getter
public class RequiredException extends RuntimeException {

    private String code;
    private String message;
    private String fieldName;

    public RequiredException(String message) {
        super(message);
    }

    public RequiredException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.fieldName = "";
    }

    public RequiredException(String code, String message, String fieldName) {
        super(message);
        this.code = code;
        this.message = message;
        this.fieldName = fieldName;
    }
}
