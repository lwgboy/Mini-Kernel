package com.gcloud.api.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gcloud.api.ApiIdentityConfig;
import com.gcloud.api.security.HttpRequestConstant;
import com.gcloud.api.security.IApiIdentity;
import com.gcloud.api.security.SecurityParameter;
import com.gcloud.api.security.SignUser;
import com.gcloud.common.util.SignCore;
import com.gcloud.common.util.SignUtil;
import com.gcloud.common.util.StringUtils;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpSignatureFilter implements Filter {
	@Autowired
	IApiIdentity apiIdentity;
	@Autowired
	ApiIdentityConfig identityConfig;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String reqPath = request.getRequestURI().replace(".do", "");
		//IdentityConfig identityConfig = SpringUtil.getBean(IdentityConfig.class);
		//IUserService userService = (IUserService)SpringUtil.getBean("userService");
		String tokenId = request.getHeader(HttpRequestConstant.HEADER_TOKEN_ID);
		if(tokenId == null) {  

			if(!(StringUtils.isEmpty(request.getHeader(SecurityParameter.AccessKeyId.toString())) 
					|| StringUtils.isEmpty(request.getHeader(SecurityParameter.Signature.toString()))
					|| StringUtils.isEmpty(request.getHeader(SecurityParameter.Timestamp.toString())))) {
				long timestamp = Long.parseLong(request.getHeader(SecurityParameter.Timestamp.toString()));
    			long currentTimestamp = System.currentTimeMillis();
    			if(Math.abs(timestamp - currentTimestamp) > identityConfig.getApiTimeout() * 60 * 1000) {
    				log.debug("::非法api请求");
                    throw new GCloudException("::非法api请求");
    			}
    			
                String sig = request.getHeader("Signature");
                Map<String, String> paramMap = SignUtil.getAllParamAsString(request.getParameterMap());
    			Map<String, String> newParams = SignCore.paraFilter(paramMap);

    			String url = request.getRequestURL().toString();

    			String httpMethod = request.getMethod();

    			Map<String, String> headers = new HashMap<>();
    			headers.put("SignatureMethod", request.getHeader(SecurityParameter.SignatureMethod.toString()));
    			headers.put("AccessKeyId", request.getHeader(SecurityParameter.AccessKeyId.toString()));
    			headers.put("Timestamp", request.getHeader(SecurityParameter.Timestamp.toString()));
                
    			//User curUser = userService.findUniqueByProperty("accessKey", request.getHeader(SecurityParameter.AccessKeyId.toString()));
    			SignUser signUser= (SignUser) CacheContainer.getInstance().get(CacheType.SIGN_USER,request.getHeader(SecurityParameter.AccessKeyId.toString()));
    			if(signUser==null)
    				signUser=apiIdentity.getUserByAccessKey(request.getHeader(SecurityParameter.AccessKeyId.toString()));
    			if(signUser==null) {
    				log.debug("::非法api请求");
                    throw new GCloudException("::非法api请求");
    			}
                boolean isValid = false;
    			//通过认证获取secretKey
    			try {
    				isValid = SignUtil.verify(sig, httpMethod, url, null, newParams, new HashMap<String, String>(), signUser.getSecretKey());
    			}catch(Exception e) {
    				log.error("api签名失败" +e.getMessage());
    				throw new GCloudException("::系统异常，请联系管理员");
    			}
    			if(!isValid) {
                	log.debug("::非法api请求");
                    throw new GCloudException("::非法api请求");
                } else {
                	ModifiableHttpServletRequest modifyReq = new ModifiableHttpServletRequest(request);
    	        	modifyReq.addParameter("currentUser.id", signUser.getUserId());
    	        	
    	        	chain.doFilter(modifyReq, servletResponse);
                }
            } else if(!AuthFilter.getExcludedUrls().contains(reqPath)) {
            	log.debug("::非法api请求");
                throw new GCloudException("::非法api请求");
            } else {
    	    	chain.doFilter(servletRequest, servletResponse);
    	    }
	    } else {
	    	chain.doFilter(servletRequest, servletResponse);
	    }
	}

	@Override
	public void destroy() {

	}

}
