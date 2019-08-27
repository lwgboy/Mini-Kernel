package com.gcloud.core.handle;

import java.util.HashMap;
import java.util.Map;

public class MessageHandlerKeeper {
	private static Map<String,MessageHandler> handlers=new HashMap<String,MessageHandler>();
	private static Map<String,AsyncMessageHandler> asyncHandlers=new HashMap<String,AsyncMessageHandler>();
	public static void put(String name,MessageHandler handler){
		handlers.put(name, handler);
	}
	public static MessageHandler get(String name){
		return handlers.get(name);
	}
	public static void put(String name,AsyncMessageHandler handler){
		asyncHandlers.put(name, handler);
	}
	public static AsyncMessageHandler getAsyncHandler(String name){
		return asyncHandlers.get(name);
	}
}
