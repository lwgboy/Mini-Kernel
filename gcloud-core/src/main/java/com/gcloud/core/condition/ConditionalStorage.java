package com.gcloud.core.condition;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(StorageCondition.class)
@Service
public @interface ConditionalStorage {
	String component() default "cinder";
	String value() default "";
}
