package com.gcloud.core.handle;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.gcloud.header.ApiVersion;
import com.gcloud.header.Module;

public class ApiHandlerKeeper {
private static Map<String,MessageHandler> apiHandler=new HashMap<String,MessageHandler>();
	
	public static void put(Module module,String action,MessageHandler handler){
		put(ApiVersion.V1,module, action, handler);
	}

	public static void put(ApiVersion version,Module module,String action,MessageHandler handler){
		String key=version.name()+"_"+module.name()+"_"+action;
		if(!apiHandler.containsKey(key))
			apiHandler.put(key.toLowerCase(), handler);
	}
	
	public static MessageHandler get(String version,String module,String action){
		String key=version+"_"+module+"_"+action;
		return apiHandler.get(key.toLowerCase());
	}
	
    public static MessageHandler get(String module, String action) {
        String version = "";
        if (StringUtils.isBlank(version)) {
            version = ApiVersion.V1.name();
        }
        return get(version, module, action);
    } 
}
