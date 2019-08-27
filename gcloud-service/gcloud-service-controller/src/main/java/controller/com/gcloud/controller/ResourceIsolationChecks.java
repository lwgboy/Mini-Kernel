package com.gcloud.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Configuration
@Inherited
public @interface ResourceIsolationChecks {
	ResourceIsolationCheck[] value();
}