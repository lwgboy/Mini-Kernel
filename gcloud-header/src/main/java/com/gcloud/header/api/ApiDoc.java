package com.gcloud.header.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口文档
 */
@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiDoc {
		public String name();
		public String code() default "";
		public int seq() default -1;
}
