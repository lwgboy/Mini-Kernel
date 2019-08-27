package com.gcloud.api.error;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gcloud.api.security.HttpRequestConstant;
import com.gcloud.core.error.ErrorInfo;
import com.gcloud.core.exception.GCloudException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity errorHandler(HttpServletRequest request, Exception ex) throws Exception {
        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null) {
            throw ex;
        }

        String requestId = ObjectUtils.toString(request.getAttribute(HttpRequestConstant.ATTR_REQUEST_ID), null);
        ErrorInfo error = new ErrorInfo();
        error.setCode("api_error_0001");
        error.setMessage("系统异常请联系管理员");
        error.setRequestId(requestId);

        log.error(ex.getMessage(), ex);
        return new ResponseEntity<ErrorInfo>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * GCloudException异常处理
     * @param ge
     * @return
     * @throws GCloudException
     */
    @ExceptionHandler({GCloudException.class})
    @ResponseBody
    public ResponseEntity handException(HttpServletRequest request, GCloudException ge) throws GCloudException {
        //If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation(ge.getClass(), ResponseStatus.class) != null) {
            throw ge;
        }

        String requestId = ObjectUtils.toString(request.getAttribute(HttpRequestConstant.ATTR_REQUEST_ID), null);
        ErrorInfo error = new ErrorInfo(ge);
        error.setRequestId(requestId);

        log.error(ge.getMessage(), ge);
        return new ResponseEntity<ErrorInfo>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity argumentNotValidHandler(HttpServletRequest request, BindException bex) throws Exception {
        List<ObjectError> errors = bex.getAllErrors();
        String errMsg = null;
        String requestId = ObjectUtils.toString(request.getAttribute(HttpRequestConstant.ATTR_REQUEST_ID), null);
        if(errors.size() > 0){
            ObjectError error = errors.get(0);
            errMsg = error.getDefaultMessage();

        }
        ErrorInfo errorInfo = new ErrorInfo(errMsg);
        errorInfo.setRequestId(requestId);
        log.error(bex.getMessage(), bex);
        return new ResponseEntity<ErrorInfo>(errorInfo, HttpStatus.BAD_REQUEST);
    }

}
