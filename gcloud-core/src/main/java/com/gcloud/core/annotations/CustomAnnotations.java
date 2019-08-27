/*
 * @Date 2015-4-2
 * 
 * @Author chenyu1@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description 自定义注解（如长操作注解）
 */
package com.gcloud.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class CustomAnnotations {
	@Retention(RetentionPolicy.RUNTIME)//指定该注解是在运行期进行
	//@Target({ElementType.METHOD})//指定该注解要在方法上使用
	@Inherited
	public @interface LongTask {
	  String value() default "true";//是否是长操作
	  //String taskExpect() default "";
	  //String funcName() default "";
	  long timeout() default 5*60*1000;//默认5分钟
	  Class commandClass() default Object.class;//长操作执行的command，用于判断超时时间
	}
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD})
	public @interface ParamsCheck {
		Class beanClass();//所验证请求command
		String paramsPrefix();//参数的前缀，例如runInstances.core,那前缀就是runInstances
	}
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD})
	public @interface MultParamsCheck {
		Class[] beanClasses();//所验证请求对象数组
		String[] paramsPrefixes();//参数的前缀，例如runInstances.core,那前缀就是runInstances
	}
	
	@Target({ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	/**
	 * 不需要json序列化的属性在其get方法上使用此注解
	 */
	public @interface Invisible {
	}
	
	@Retention(RetentionPolicy.RUNTIME)//指定该注解是在运行期进行
	@Target({ElementType.METHOD})//指定该注解要在方法上使用
	public @interface MethodLog {
	  boolean isLog() default true;//方法是否需要记录到用户操作日志
	}
	
	@Retention(RetentionPolicy.RUNTIME)//指定该注解是在运行期进行
    @Target({ElementType.METHOD})//指定该注解要在方法上使用
    public @interface LogHistory {
	    boolean isLog() default true;//是否记录历史信息
	    String content();//记录的内容，例如vmHis, vmMigrateHis
    }
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	public @interface Command {
	  long timeout() default 10*1000;
	  String funName() default "";
	}
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	public @interface JsonClassMap {
		//Map<String,Class> map() default new HashMap<String,Class>();
		String[] objNames();
		Class[] classes();
	}
	
	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	public @interface AutoLog {
		//Class logClass() default Object.class;//vm操作command记录类 
		String logClass() default "";
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD})
	public @interface Rest {
		long timeout() default 5*10*1000;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ValidateField {
		public boolean isList() default false;
		public boolean isRegex() default false;
		public String param();
	}
	@Retention(RetentionPolicy.RUNTIME)//指定该注解是在运行期进行
	public @interface CallBackCommand {
		boolean isCallback() default true;//是否需要回调
	}
	
	/**记录操作日志注解
	 * @author dengyf
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)//指定该注解是在运行期进行
	@Inherited
	public @interface GcLog {
	  String taskExpect() default "";
	  boolean isMultiLog() default false;
	}
	
//	@Retention(RetentionPolicy.RUNTIME)
//	@Target({ElementType.TYPE})
//	public @interface RoleType {
//		Role role() default Role.MNG;
//	}
//	
//	@Retention(RetentionPolicy.RUNTIME)
//	@Target({ElementType.TYPE})
//	public @interface NodeObserverAnn {
//		//Role[] roles();//表示加载此的角色
//		NodeType[] nodeTypes();//节点的类型
//		NodeSubject[] nodeSubjects();//表示触发的主题 注册?解注册?
//	}
}
