package com.gcloud.service.common.util;

import com.gcloud.core.exception.GCloudException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtil {
	
	public static void handleLog(String[] cmd, int res, String errorCode) throws GCloudException {
		if (res != 0) {
			String commandString = "";
			for (String part : cmd) {
				commandString += part + " ";
			}
			
			Throwable ex = new Throwable();
			log.error(commandString, ex);
			throw new GCloudException(errorCode);
		}
	}
}
