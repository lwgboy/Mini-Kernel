package com.gcloud.controller.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gcloud.core.exception.GCloudException;


/*
 * @Date 2015-4-3
 * 
 * @Author zhangzj@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description 用于接收前台传过来的分页和全局查询的参数，在功能Action类所继承的基类中已经定义了这个类的实例，开发具体功能时直接使用,前台页面传的参数要求是"p.xxxx"
 */
public class BaseParams {
	public Long id;
	public String username;
	public String begin;
	public String end;

	private int pageSize = 20;
	private Integer page;
	public Integer start;
	public Integer limit;
	public String sort;
	public String dir;
	
	public String key;
	public Map<String, Object> keys;
	public List<String> searchFields;
	public boolean isSearch = false;
	
	/*public void clone(BaseParams p){
		this.id = p.id;
		this.username = p.username;
		this.begin = p.begin;
		this.end = p.end;
		this.pageSize = p.pageSize;
		this.page = p.page;
		this.start = p.start;
		this.limit = p.limit;
		this.sort = p.sort;
		this.dir = p.dir;
		this.key = p.key;
		this.isSearch = p.isSearch;
	}*/
	/**
	 * 暂时不使用
	 * @return
	 */
	/*
	public OrderBy getOrderBy(){
		OrderBy order = OrderBy.desc("id");
		if(sort!=null){
			if(dir==null) order = OrderBy.asc(sort);
			else if(dir.equalsIgnoreCase("ASC")) order = OrderBy.asc(sort);
			else if(dir.equalsIgnoreCase("DESC")) order = OrderBy.desc(sort);
			else order = OrderBy.asc(sort);
		}
		return order;
	}*/
	
	/**
	  * @Title: objectToMap
	  * @Description: 把对象转成Map，key为对象属性名，value为对象属性值，属性必须要有getset方法，只支持属性get方法（即如果是基本数据类型要转程对应包装类型，或者获取方法改成对应get方法）
	  * @date 2015-5-12 下午4:53:07
	  *
	  * @param obj
	  * @return
	  * @throws GCloudException    
	  */
	public Map<String, Object> objectToMap(Object obj) throws GCloudException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Class clazz = obj.getClass();
		Field[] fileds = clazz.getDeclaredFields();
		
		for(Field f : fileds){
			
			String key = f.getName();
			Object value = "";
			
			String getterName = "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
			
			try {
				Method getMethod = clazz.getMethod(getterName);
				value = getMethod.invoke(obj);
			} catch ( Exception e ) {
				throw new GCloudException("assign value failed ");
			}
			map.put(key, value);
		}
		
		return map;
		
	}
	
	/**
	 * 记录ID，页面传参名为p.id
	 * @return　记录ID
	 */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 不使用
	 * @return　用户名
	 */
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 不使用
	 * @return
	 */
	public String getBegin() {
		return begin;
	}
	public void setBegin(String begin) {
		this.begin = begin;
	}
	/**
	 * 不使用
	 * @return
	 */
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	/**
	 * 开始页序号，即第几页，页面传参名为p.start
	 * @return　开始页序号，即第几页
	 */
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	/**
	 * 每页记录数，页面传参名为p.limit
	 * @return　每页记录数
	 */
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	/**
	 * 排序字段
	 * @return　排序字段
	 */
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	/**
	 * 不使用
	 * @return
	 */
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	/**
	 * 获取记录的页数
	 * @return　获取记录的页数
	 */
	public int getPageSize() {
		if(limit != null && limit>0) pageSize = limit;
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * 第几页
	 * @return　第几页
	 */
	public Integer getPage() {
		if(page == null || page == 0)
			page = start==null || start==0 ? 1 : (start<0?1:(start / getPageSize() + 1));
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	/**
	 * 不使用
	 * @return
	 */
	public boolean isSearch() {
		return isSearch;
	}
	/**
	 * 不使用
	 * @param isSearch
	 */
	public void setIsSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}
	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}
	/**
	 * 查询关键字，通常用于模糊查询，页面传参名为p.key
	 * @return　查询关键字
	 */
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * 不使用
	 * @return
	 */
	public List<String> getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(List<String> searchFields) {
		this.searchFields = searchFields;
	}

	public Map<String, Object> getKeys() {
		return keys;
	}

	public void setKeys(Map<String, Object> keys) {
		this.keys = keys;
	}
	
	
}
