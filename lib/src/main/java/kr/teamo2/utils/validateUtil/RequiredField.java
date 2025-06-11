package kr.teamo2.utils.validateUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RequiredField {

    boolean hasDefaultValue() default false;

    String defaultValue() default "";

    Class<?> defaultValueType() default String.class;
}
