
package com.gcloud.api;

import com.gcloud.api.security.IApiIdentity;
import com.gcloud.common.util.RequestUtils;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandlerKeeper;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.identity.user.GetUserReplyMsg;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
@Component
public class ApiHandlerMessageMethodArgumentResolver implements HandlerMethodArgumentResolver {
	@Autowired
	IApiIdentity apiIdentity;
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().getSimpleName().equals(ApiMessage.class.getSimpleName());
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    	Map<String, String> variables = (Map<String, String>)webRequest.getAttribute("org.springframework.web.servlet.View.pathVariables", 0);
    	String version=webRequest.getParameter("v");
    	MessageHandler handler;
    	if(StringUtils.isBlank(version)){
    		handler=ApiHandlerKeeper.get(variables.get("module"), variables.get("action"));
    	}else{
    		handler=ApiHandlerKeeper.get(version,variables.get("module"), variables.get("action"));
    	}
    	if(handler==null){
    	    throw new GCloudException("::api not found");
    		//exception
    	}
    	Class clazz = handler.getClass();
    	String className= clazz.getName().substring(0, clazz.getName().indexOf("$$"));
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Type types = clazz.getGenericSuperclass();
        Type[] genericType = ((ParameterizedType) types).getActualTypeArguments();
        Class<?>  target=null;
        if(genericType.length>0){
        	target=(Class<?>) genericType[0];
        }
        else{
        	//exception
        }
        
        //Class<?> target = MessageKeeper.get(variables.get("module"), variables.get("action"));
        String name = variables.get("module") + "." + variables.get("action");
		Object attribute = createAttribute(target);

        WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, name);
        if (binder.getTarget() != null) {
            bindRequestParameters(binder, webRequest);
            validateIfApplicable(binder, parameter);
            if (binder.getBindingResult().hasErrors()) {
                throw new BindException(binder.getBindingResult());
            }
        }

        Map<String, Object> bindingResultModel = binder.getBindingResult().getModel();
        mavContainer.removeAttributes(bindingResultModel);
        mavContainer.addAllAttributes(bindingResultModel);
        
        ApiMessage msg = (ApiMessage)binder.convertIfNecessary(binder.getTarget(), parameter.getParameterType(), parameter);
        msg.setIp(RequestUtils.getIpAddr(webRequest.getNativeRequest(HttpServletRequest.class)));
        if(!StringUtils.isBlank(webRequest.getParameter("currentUser.id"))) {
        	//IUserService userService = (IUserService)SpringUtil.getBean("userService");
            //ITenantUserService tenantUserService = (ITenantUserService)SpringUtil.getBean("tenantUserServiceImpl");
            //User curUser = userService.findUniqueByProperty("id", webRequest.getParameter("currentUser.id"));
            GetUserReplyMsg getUserReply= apiIdentity.getUserById(webRequest.getParameter("currentUser.id"));
        	
            msg.setCurUserId(getUserReply.getId());//暂时兼容旧代码，后续逐渐替代后删掉
            
            CurrentUser currentUser = new CurrentUser();
            currentUser.setId(getUserReply.getId());
            currentUser.setDefaultTenant(webRequest.getHeader("TenantId"));
            currentUser.setLoginName(getUserReply.getLoginName());
            currentUser.setRealName(getUserReply.getRealName());
            currentUser.setEmail(getUserReply.getEmail());
            currentUser.setRole(getUserReply.getRoleId());
            currentUser.setMobile(getUserReply.getMobile());
            currentUser.setUserTenants(getUserReply.getTenants());
            msg.setCurrentUser(currentUser);
            
            if(StringUtils.isNotBlank(currentUser.getDefaultTenant()) && !getUserReply.getTenants().contains(currentUser.getDefaultTenant())) {
            	throw new GCloudException("没有该默认租户权限");
            }
        }
        return msg;
    }

    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        Annotation[] annotations = parameter.getParameterAnnotations();
        for (Annotation ann : annotations) {
            Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
            if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
                Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
                Object[] validationHints = (hints instanceof Object[] ? (Object[])hints : new Object[] {hints});
                binder.validate(validationHints);
                break;
            }
        }
    }

    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
        ((ServletRequestDataBinder)binder).bind(servletRequest);
    }
    
    /**
	 * Extension point to create the model attribute if not found in the model.
	 * The default implementation uses the default constructor.
	 * @param cls the class of the attribute (never {@code null})
	 * @return the created model attribute (never {@code null})
	 */
	protected Object createAttribute(Class<?> cls) throws Exception {
        if (cls == null) {
        	return null;
        }
        return cls.newInstance();
		//return BeanUtils.instantiateClass(cls);
	}

}
