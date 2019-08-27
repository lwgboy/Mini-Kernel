package com.gcloud.core.handle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;

import com.gcloud.header.ApiVersion;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Configuration
@Inherited
public @interface ApiHandler {
	public ApiVersion[] versions() default {ApiVersion.V1};
	public Module module();
	public SubModule subModule() default SubModule.NONE;
	public String action();
	public String name() default "";
}
