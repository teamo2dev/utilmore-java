package kr.teamo2.utils.loggingUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogEnabled {

    @AliasFor("collection")
    String value() default "";

    @AliasFor("value")
    String collection() default "";

    boolean isEnabled() default true;
}
