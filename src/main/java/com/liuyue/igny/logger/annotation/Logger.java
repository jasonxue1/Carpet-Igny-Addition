package com.liuyue.igny.logger.annotation;

import com.liuyue.igny.logger.callback.LoggerCallback;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Logger {
    String defaultValue() default "";
    String[] options() default {""};
    boolean strictOptions() default false;
    Class<? extends LoggerCallback> callback() default LoggerCallback.class;
}
