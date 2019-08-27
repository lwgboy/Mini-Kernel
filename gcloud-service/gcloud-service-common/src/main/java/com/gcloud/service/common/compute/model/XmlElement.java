package com.gcloud.service.common.compute.model;
import java.util.List;
import java.util.Map;

public class XmlElement {

	private String name;
	private Map<String, String> attributes;
	private String text;
	private List<XmlElement> childrens;
	
	public XmlElement(String name, String text, Map<String, String> attributes,
			List<XmlElement> childrens) {
		super();
		this.name = name;
		this.text = text;
		this.attributes = attributes;
		this.childrens = childrens;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, String> getAttributes() {
		return attributes;
	}
	public void setAttribute(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<XmlElement> getChildrens() {
		return childrens;
	}
	public void setChildrens(List<XmlElement> childrens) {
		this.childrens = childrens;
	}
	
	
}
