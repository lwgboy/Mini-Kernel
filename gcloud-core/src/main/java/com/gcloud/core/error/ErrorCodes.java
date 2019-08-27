package com.gcloud.core.error;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/*
 * @Desccription 错误码存储map，不使用configuration，脱离依赖注入也可以使用
 * @Date 2018/2/5
 * @Created by yaowj
 */
@Slf4j
public class ErrorCodes extends HashMap<String, String> {
	private static final long serialVersionUID = 1L;

	public String get(String errorCode, String errorMsg, Object... params){

        String msg = this.get(errorCode);
        if(!StringUtils.isBlank(msg)){
            errorMsg = msg;
        }
        if(params != null && StringUtils.isNotBlank(errorMsg)){
            try{
                errorMsg = String.format(errorMsg, params);
            }catch (Exception ex){
                log.error(String.format("format error message error, msg=%s", errorMsg), ex);
            }
        }
        return errorMsg;

    }

    @SuppressWarnings("unchecked")
	public void init() {
        Yaml yaml = new Yaml();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = null;
        try{
            resources = resolver.getResources("classpath*:errorcode/gcloud-errorcode-*.yml");
        }catch(Exception re){
            log.error("get resources error", re);
        }
        if(resources != null && resources.length > 0){

            for(Resource resource : resources){

                InputStream io = null;
                Map<String, Object> errorObj = null;
                try{
                    io = resource.getInputStream();
                    errorObj = (Map<String, Object>) yaml.load(io);
                }catch(Exception ex){
                    log.error("read error code error", ex);
                }finally {
                    if(io != null){
                        try {
                            io.close();
                        } catch (IOException e) {
                            log.error("InputStream closed error", e);
                        }
                    }
                }
                try{
                    handleErrorCode(errorObj);
                }catch (Exception ex){
                    log.error("fail to read error code", ex);
                }

            }
        }

    }

    @SuppressWarnings("unchecked")
	private void handleErrorCode(Map<String, Object> errorObj){

        if(errorObj == null){
            return;
        }

//        String module = ObjectUtils.toString(errorObj.get("prefix"));

        Map<String, Object> subModules = (Map<String, Object>)errorObj.get("errorcodes");
        if(subModules == null || subModules.size() == 0){
            return;
        }
        for (Map.Entry<String, Object> subMod : subModules.entrySet()) {
            Map<String, Object> functions = (Map<String, Object>)subMod.getValue();
            if(functions == null || functions.size() == 0){
                continue;
            }
            for(Map.Entry<String, Object> func : functions.entrySet()){
                Map<String, String> errors = (Map<String, String>)func.getValue();
                if(errors == null || errors.size() == 0){
                    continue;
                }
                for(Map.Entry<String, String> err : errors.entrySet()){
                    //默认补全四个
//                    String codeNum = StringUtils.leftPad(err.getKey(), 4, "0");
//                    String errorcode = String.format("", module, subModStr, funcStr, codeNum);
                    this.put(err.getKey(), err.getValue());
                }

            }
        }

    }

}
