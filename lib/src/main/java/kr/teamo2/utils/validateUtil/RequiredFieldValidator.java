package kr.teamo2.utils.validateUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequiredFieldValidator {

    static final List<String> JAVA_PACKAGE_PREFIXES = List.of("java.", "javax.");

    public static void validateField(Object object) {
        if (object == null) {
            return;
        }

        Class<?> current = object.getClass();
        while (current != null && current != Object.class) {
            Field[] fields = current.getDeclaredFields();

            // validate basic type value
            Arrays.stream(fields)
                .peek(field -> field.setAccessible(true))
                .filter(field -> field.isAnnotationPresent(RequiredField.class))
                .filter(field -> isNull(object, field))
                .forEach(field -> validate(object, field));

            // validate object type
            Arrays.stream(fields)
                .peek(field -> field.setAccessible(true))
                .filter(field -> !isEnum(field.getType()))
                .filter(field -> !isJavaPackage(field.getType()))
                .forEach(field -> {
                    try {
                        Object innerObject = field.get(object);
                        if (innerObject != null) {
                            validateField(innerObject);
                        }
                    } catch (IllegalAccessException e) {
                        throw RequiredFieldErrorCode.INTERNAL_ERROR.toRequiredException(e);
                    }
                });

            // Collection and Array traversal
            Arrays.stream(fields)
                .peek(field -> field.setAccessible(true))
                .filter(field -> Collection.class.isAssignableFrom(field.getType()) || field.getType().isArray())
                .forEach(field -> {
                    try {
                        Object value = field.get(object);
                        if (value instanceof Collection<?> collection) {
                            for (Object item : collection) {
                                if (item != null && !isEnum(item.getClass()) && !isJavaPackage(item.getClass())) {
                                    validateField(item);
                                }
                            }
                        }
                    } catch (IllegalAccessException e) {
                        throw RequiredFieldErrorCode.INTERNAL_ERROR.toRequiredException(e);
                    }
                });

            current = current.getSuperclass();
        }
    }

    private static boolean isJavaPackage(Class<?> type) {
        return Optional.ofNullable(type)
            .map(Class::getPackage)
            .map(Package::getName)
            .map(name -> JAVA_PACKAGE_PREFIXES.stream().anyMatch(name::startsWith))
            .orElse(false);
    }

    private static boolean isEnum(Class<?> type) {
        return type.isEnum();
    }

    private static boolean isNull(Object object, Field field) {
        field.setAccessible(true);
        try {
            return field.get(object) == null;
        } catch (IllegalAccessException e) {
            throw RequiredFieldErrorCode.INTERNAL_ERROR.toRequiredException(e);
        }
    }

    private static void validate(Object object, Field field) {
        RequiredField annotation = field.getAnnotation(RequiredField.class);
        if (!annotation.hasDefaultValue()) {
            throw RequiredFieldErrorCode.REQUIRED_FIELD_NOT_EXIST.toRequiredException(field);
        }
        try {
            field.set(object, getValue(field, annotation));
        } catch (IllegalAccessException e) {
            throw RequiredFieldErrorCode.INTERNAL_ERROR.toRequiredException(e);
        }
    }

    private static Object getValue(Field field, RequiredField annotation) {
        Class<?> defaultValueType = annotation.defaultValueType();
        String defaultValue = annotation.defaultValue();
        if (defaultValueType == String.class) {
            return defaultValue;
        }
        if (defaultValueType == int.class) {
            return Integer.parseInt(defaultValue);
        }
        if (defaultValueType == BigDecimal.class) {
            return new BigDecimal(defaultValue);
        }
        if (defaultValueType == boolean.class) {
            return Boolean.parseBoolean(defaultValue);
        }
        if (defaultValueType == Array.class) {
            return Array.newInstance(field.getType().getComponentType(), 0);
        }
        return null;
    }
}
