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
@Conditional(ImageCondition.class)
@Service
public @interface ConditionalImage {
	String component() default "glance";
	String value() default "";
}
