package com.gcloud.header.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//import org.springframework.web.bind.annotation.RequestMethod;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiOperation {

	public String name();
	public String url();
	//public RequestMethod requestMethod() default RequestMethod.GET;
	public String permissionCode() default "";//若此值不是空字串，表示些操作是一个单独的权限
	public String[] permissions() default {};//对应所属权限，某些操作可能对应多个权限,若为空，取用permissionCode
	public boolean admin() default false;//表示super admin的接口
	public boolean common() default false;//表示一个共公接口，不受权限拦截
	public Class request() default Object.class;  // 参数类
	public Class response() default Object.class;  // 返回类
	public String description() default "";
	
}
