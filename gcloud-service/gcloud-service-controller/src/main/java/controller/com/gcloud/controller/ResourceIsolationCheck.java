package com.gcloud.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;

import com.gcloud.controller.enums.ResourceIsolationCheckType;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Configuration
@Inherited
@Repeatable(ResourceIsolationChecks.class)
public @interface ResourceIsolationCheck {
	public ResourceIsolationCheckType resourceIsolationCheckType();
	public String resourceIdField() default "id";
}
