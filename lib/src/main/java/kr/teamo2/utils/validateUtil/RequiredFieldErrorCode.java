package kr.teamo2.utils.validateUtil;

import java.lang.reflect.Field;
import java.util.Optional;
import kr.teamo2.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RequiredFieldErrorCode implements BaseErrorCode<RequiredException> {

    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "unknown internal error"),
    REQUIRED_FIELD_NOT_EXIST(HttpStatus.UNPROCESSABLE_ENTITY, "REQUIRED_FIELD_NOT_EXIST", "required field {} is null");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    @Override
    public RequiredException toException() {
        return new RequiredException(message);
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public RequiredException toRequiredException(Field field) {
        String requiredValue = getFieldNameWithClass(field);
        String errorMessage = message.replace("{}", requiredValue);
        return new RequiredException(errorCode, errorMessage, requiredValue);
    }

    public RequiredException toRequiredException(Throwable throwable) {
        return new RequiredException(errorCode, message, throwable);
    }

    private String getFieldNameWithClass(Field field) {
        return Optional.ofNullable(field)
            .map(f -> f.getDeclaringClass().getSimpleName() + "." + f.getName())
            .orElse("UnknownField");
    }

}
