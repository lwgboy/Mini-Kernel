package com.gcloud.header.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModel {

	public boolean require() default false;
	public String example() default "";
	public String type() default "String";
	public String description() default "";
}
