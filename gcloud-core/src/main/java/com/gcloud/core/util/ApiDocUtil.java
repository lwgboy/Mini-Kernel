package com.gcloud.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;

import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.framework.db.FieldUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.api.ApiModel;

public class ApiDocUtil {
	private final static ApiDocUtil INSTANCE = new ApiDocUtil();
	public static ApiDocUtil getInstance() {
        return INSTANCE;
    }
	private Map<Module,Map<SubModule,Map<String,Class>>> moduleHandlers=new HashMap<>();
	private String mdStr;
	public String getDoc(){
		if(mdStr==null)
			try {
				init();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return mdStr;
	}
	
	public void init() throws ClassNotFoundException{
		Map<String,Object>  map=SpringUtil.getApplicationContext().getBeansWithAnnotation(ApiHandler.class);
		moduleHandlers=new HashMap<>();
		for(Object o:map.values()){
			String className= o.getClass().getName().substring(0, o.getClass().getName().indexOf("$$"));
			Class<?> clazz = null;
			clazz = Class.forName(className);
			put(clazz);
			/*Type types = clazz.getGenericSuperclass();
	        Type[] genericType = ((ParameterizedType) types).getActualTypeArguments();
	        if(genericType.length>0){
	        	Class reqClazz= Class.forName(genericType[0].getTypeName());
	        	Field[] reqFields= ReflectionUtils.getDeclaredField(reqClazz);
	        	for(Field field:reqFields){
	        		field.getName();
	        	}
	        	Class respClazz= Class.forName(genericType[1].getTypeName());
	        }
	        */
		}
		mdStr=genMdStr();
		
	}
	
	public void put(Class<?> clazz){
		ApiHandler  apiHandler= clazz.getAnnotation(ApiHandler.class);
		Module module=apiHandler.module();
		SubModule subModule=apiHandler.subModule();
		String action=apiHandler.action();
		Map<SubModule,Map<String,Class>> subMap= moduleHandlers.get(module);
		if(subMap==null){
			subMap=new HashMap<>();
			Map<String,Class> actionMap=new HashMap<>();
			actionMap.put(action, clazz);
			subMap.put(subModule, actionMap);
			moduleHandlers.put(module, subMap);
		}else{
			Map<String,Class> actionMap=subMap.get(subModule);
			if(actionMap==null){
				actionMap=new HashMap<>();
				actionMap.put(action, clazz);
				subMap.put(subModule, actionMap);
			}else{
				actionMap.put(action, clazz);
			}
		}
			
	}
	
	private String genMdStr() throws ClassNotFoundException{
		StringBuilder sb=new StringBuilder();
		Iterator<Module> it=moduleHandlers.keySet().iterator();
		while(it.hasNext()){
			Module module=it.next();
			sb.append(MarkdownUtils.title(1, module.getCnName()));//主模块
			Map<SubModule,Map<String,Class>> subMap=moduleHandlers.get(module);
			Iterator<SubModule> subIt=subMap.keySet().iterator();
			while(subIt.hasNext()){
				SubModule subModule=subIt.next();
				if(subModule!=SubModule.NONE){
					sb.append(MarkdownUtils.title(2, subModule.getCnName()));//子模块
				}
				Map<String,Class> actionMap=subMap.get(subModule);
				Iterator<String> actionIt=actionMap.keySet().iterator();
				while(actionIt.hasNext()){
					String action=actionIt.next();
					Class<?> clazz=actionMap.get(action);
					ApiHandler apiHandler=clazz.getAnnotation(ApiHandler.class);
					if(StringUtils.isNotBlank(apiHandler.name())){
						sb.append(MarkdownUtils.title(3, apiHandler.name()));//功能名
					}
					sb.append(MarkdownUtils.title(4, action));
					sb.append("URI:").append(module.name().toLowerCase()).append("/").append(action).append(".do\n");//url
					sb.append("请求参数:\n");//请求参数
					Type types = clazz.getGenericSuperclass();
			        Type[] genericType = ((ParameterizedType) types).getActualTypeArguments();
			        if(genericType.length==2){
			        	Class reqClazz= Class.forName(genericType[0].getTypeName());
			        	Field[] reqFields= ReflectionUtils.getDeclaredField(reqClazz);
			        	sb.append(MarkdownUtils.tableTitle("参数","类型","必须?","例子","描述"));
			        	for(Field field:reqFields){
			        		ApiModel apiModel= field.getAnnotation(ApiModel.class);
			        		if(apiModel!=null){
			        			sb.append(MarkdownUtils.tableRow(field.getName(),apiModel.type(),apiModel.require()+"",apiModel.description()));
			        		}
			        	}
			        	sb.append("响应:\n");//响应
			        	Class respClazz= Class.forName(genericType[1].getTypeName());
			        	Field[] respFields=ReflectionUtils.getDeclaredField(respClazz);
			        	List<com.gcloud.header.api.model.ApiModel> apiModels=addModel(respFields);
			        	sb.append(MarkdownUtils.tableTitle("参数","类型","例子","描述"));
			        	for(com.gcloud.header.api.model.ApiModel apiModel:apiModels){
			        		sb.append(MarkdownUtils.tableRow(apiModel.getName(),apiModel.getType(),apiModel.getExample(),apiModel.getDescription()));
			        	}
			        }
				}
			}
		}
		return sb.toString();
	}
	
	
	private List<List<Field>> getAllFields(List<Field> chain, Field[] fields, Class<?> gClass) {
		List<List<Field>> result = new ArrayList<List<Field>>();
		for (Field field : fields) {
			if(field.getAnnotation(ApiModel.class)==null)
				continue;
			Class<?> fieldClass = field.getType();
			if (fieldClass.isPrimitive() || fieldClass.getName().startsWith("java.math") || fieldClass.getName().startsWith("java.lang") || fieldClass.getName().startsWith("java.util.Date") || fieldClass.getName().startsWith("javax") || fieldClass.getName().startsWith("com.sun") || fieldClass.getName().startsWith("sun") || fieldClass.getName().startsWith("boolean") || fieldClass.getName().startsWith("double") || fieldClass.getName().startsWith("int")) {
				List<Field> endChain = new ArrayList<Field>(chain);
				endChain.add(field);
				result.add(endChain);
				continue;
			} else {
				if (!fieldClass.equals(PageResult.class)) {
					List<Field> endChain = new ArrayList<Field>(chain);
					endChain.add(field);
					result.add(endChain);
				}

				Type fc = field.getGenericType();
				if (field.getGenericType() instanceof ParameterizedType) { // 判断是否有泛型类型

					ParameterizedType pt = (ParameterizedType) fc;

					Class<?> genericClazz = null;
					if ("sun.reflect.generics.reflectiveObjects.TypeVariableImpl".equals(pt.getActualTypeArguments()[0].getClass().getName())) {
						genericClazz = gClass;
					} else {
						genericClazz = (Class<?>) pt.getActualTypeArguments()[0]; // 【4】
					}

					if (genericClazz.getName().startsWith("java.lang") // 设置list的终止类型
							|| genericClazz.getName().startsWith("java.util.Date") || genericClazz.getName().startsWith("javax") || genericClazz.getName().startsWith("com.sun") || genericClazz.getName().startsWith("sun") || genericClazz.getName().startsWith("boolean") || genericClazz.getName().startsWith("double") || genericClazz.getName().startsWith("int")) {
						continue;
					}
					// System.out.println(genericClazz);
					// 得到泛型里的class类型对象。
					List<Field> thisChain = new ArrayList<Field>(chain);

					if (!fieldClass.equals(PageResult.class)) {
						thisChain.add(field); // !!
					}

					if (fieldClass.isAssignableFrom(PageResult.class)) {
						result.addAll(getAllFields(new ArrayList<Field>(thisChain), ReflectionUtils.getDeclaredField(fieldClass), genericClazz));
					} else {
						result.addAll(getAllFields(new ArrayList<Field>(thisChain), ReflectionUtils.getDeclaredField(genericClazz), null));
					}
				} else {
					List<Field> thisChain = new ArrayList<Field>(chain);
					thisChain.add(field);
					result.addAll(getAllFields(new ArrayList<Field>(thisChain), ReflectionUtils.getDeclaredField(fieldClass), null));
				}

			}
		}
		return result;
	}
	
	private  List<com.gcloud.header.api.model.ApiModel> addModel(Field[] responseFields) {
		List<com.gcloud.header.api.model.ApiModel> amList = new ArrayList<com.gcloud.header.api.model.ApiModel>();
		List<List<Field>> responseFieldList = getAllFields(new ArrayList<Field>(), responseFields, null);
		if (responseFieldList != null) {
			for (List<Field> responseField : responseFieldList) {
				if (responseField != null && responseField.size() > 0) {
					int size = responseField.size();
					String name = "";
					for (int i = 0; size > i; i++) {
						name += responseField.get(i).getName();
						if (i != (size - 1)) {
							name += ".";
						}
					}

					Field lastField = responseField.get(size - 1);
					com.gcloud.header.api.model.ApiModel model = new com.gcloud.header.api.model.ApiModel();
					ApiModel am = lastField.getAnnotation(ApiModel.class);
					if (am != null) {
						model.setRequire(am.require());
						model.setExample(am.example());
						model.setDescription(am.description());
						model.setType(lastField.getType().getSimpleName());
						model.setName(name);
						amList.add(model);
					}
				}

			}
		}
		return amList;
	}
}
