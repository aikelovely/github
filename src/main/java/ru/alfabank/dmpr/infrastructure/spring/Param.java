package ru.alfabank.dmpr.infrastructure.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация-маркер позволяющая "навесить" имя параметра метода для его использования в runtime
 * через рефлексивные операции. Используется в FrontApiController.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Param {
    String value();
}
