package com.gcloud.api.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.util.StringUtils;

import com.gcloud.api.ApiIdentityConfig;
import com.gcloud.api.security.HttpRequestConstant;
import com.gcloud.api.security.IApiIdentity;
import com.gcloud.api.security.TokenUser;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthFilter implements Filter{
	private static List<String> excludedUrls = Arrays.asList("/user/login", "/authcode/kaptcha"); 
	@Autowired
	ApiIdentityConfig identityConfig;
	@Autowired
	IApiIdentity apiIdentity;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("---------------------开始进入请求地址拦截----------------------------"); 
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		//IdentityConfig identityConfig = SpringUtil.getBean(IdentityConfig.class);
		String reqPath = httpRequest.getRequestURI().replace(".do", "");
		log.debug(String.format("【认证拦截器】拦截地址：%s", reqPath));
		
		if(excludedUrls.contains(reqPath)) {
			chain.doFilter(request, response);
			return;
		}
		
		String tokenId = httpRequest.getHeader(HttpRequestConstant.HEADER_TOKEN_ID);
		if(null != tokenId) {
			TokenUser tokenUser=(TokenUser) CacheContainer.getInstance().get(CacheType.TOKEN_USER, tokenId);
			if(tokenUser==null)
				tokenUser=apiIdentity.checkToken(tokenId);
			if(tokenUser!=null&&tokenUser.getExpressTime() > System.currentTimeMillis()) {
				
			//SessionUser sessionUser = (SessionUser)CacheContainer.getInstance().get(tokenId);
	        //if(!StringUtils.isEmpty(sessionUser) 
	        	//	&& sessionUser.getToken().getTokenId().equals(tokenId) 
	        		//&& sessionUser.getToken().getExpireTime() > System.currentTimeMillis()) {  
	        	ModifiableHttpServletRequest modifyReq = new ModifiableHttpServletRequest(httpRequest);
	        	modifyReq.addParameter("currentUser.id", tokenUser.getUserId());
	        	
	        	chain.doFilter(modifyReq, response);  
	        } else {  
	        	throw new GCloudException("C003_MNG_0001::会话无效，请登录");
	        }
		} else if(identityConfig.isSignatureCheck()) {
			chain.doFilter(request, response);
			return;
		} else {
			chain.doFilter(request, response);
			return;
			//throw new GCloudException("C003_MNG_0002::会话无效，请登录");
		}
	}

	@Override
	public void destroy() {
		
	}

	public static List<String> getExcludedUrls() {
		return excludedUrls;
	}
	
}
