package com.gcloud.common.model;

import java.util.List;
import java.util.Map;

public class BaseParams {
	private int pageSize = 20;
	private Integer page;
	public String sort;
	public String dir;
	
	public String key;
	
	public BaseParams()
	{
		
	}
	
	public BaseParams(Map<String, List<String>> params)
	{
		if(params.get("p.pageSize") != null)
		{
			this.setPageSize(Integer.parseInt(params.get("p.pageSize").get(0).toString()));
		}
		if(params.get("p.page") != null)
		{
			this.setPage(Integer.parseInt(params.get("p.page").get(0).toString()));
		}
		if(params.get("p.sort") != null)
		{
			this.setSort(params.get("p.sort").get(0).toString());
		}
		if(params.get("p.dir") != null)
		{
			this.setDir(params.get("p.dir").get(0).toString());
		}
		if(params.get("p.key") != null)
		{
			this.setKey(params.get("p.key").get(0).toString());
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return (page==null||page.equals(0))?1:page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
