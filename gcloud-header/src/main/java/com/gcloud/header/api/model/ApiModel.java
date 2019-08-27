package com.gcloud.header.api.model;

public class ApiModel {

	public String name;
	public boolean require;
	public String example;
	public String type;
	public String alias;
	public String description;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isRequire() {
		return require;
	}
	public void setRequire(boolean require) {
		this.require = require;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) {
		this.example = example;
	}
}
