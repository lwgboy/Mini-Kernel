package com.gcloud.core.quartz.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface QuartzTimer {
	String cron() default "";
	long fixedDelay() default -1L;
	long fixedRate() default -1L;
}
