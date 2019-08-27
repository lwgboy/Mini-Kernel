package com.gcloud.api.filter;

import com.gcloud.api.security.HttpRequestConstant;
import com.gcloud.core.error.ErrorInfo;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 过滤器统一异常处理
 * @author dengyf
 *
 */
@Slf4j
@Component
public class ErrorFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		ErrorInfo error = null;
		String requestId = ObjectUtils.toString(request.getAttribute(HttpRequestConstant.ATTR_REQUEST_ID), null);
        try {
        	chain.doFilter(request, response);

        } catch(GCloudException gex) {
            error = new ErrorInfo(gex);
        }catch(Exception ex) {
        	error = new ErrorInfo();
        	error.setCode("api_error_1001");
        	error.setMessage("系统异常请联系管理员");
        }
        if(error != null) {
        	error.setRequestId(requestId);

			MappingJackson2HttpMessageConverter converter = SpringUtil.getBean(MappingJackson2HttpMessageConverter.class);

            HttpServletResponse httpResponse = (HttpServletResponse) response;

            if (httpResponse.getCharacterEncoding() == null) {
            	httpResponse.setCharacterEncoding("UTF-8");
    		}
			httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
			httpResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			ServletOutputStream outputStream = httpResponse.getOutputStream();

			outputStream.write(converter.getObjectMapper().writeValueAsString(error).getBytes());
			outputStream.flush();

            return ;
        }

	}

	@Override
	public void destroy() {

	}

}
