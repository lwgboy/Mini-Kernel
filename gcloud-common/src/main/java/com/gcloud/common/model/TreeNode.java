/*
 * @Date 2015-5-4
 * 
 * @Author dengyf@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved. 
 * 
 * @Description 树节点
 */
package com.gcloud.common.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class TreeNode  implements Comparable<TreeNode>
{

	protected String id;
	protected String type;
	protected String text;
	protected String url;
	protected String icon;
	protected String qtip;
	protected String key;
	protected Boolean leaf = false;
	
	protected String name;
	
	protected List<TreeNode> children=new ArrayList<TreeNode>();
	
	public TreeNode(){}
	public TreeNode(String id,String type, String text,String url) {
		super();
		this.id = id;
		this.type = type;
		this.text = text;
		this.url = url;
	}
	public TreeNode(String id,String type, String text) {
		super();
		this.id = id;
		this.type = type;
		this.text = text;
	}
	public TreeNode(String type, String text) {
		super();
		this.type = type;
		this.text = text;
	}	

	@Override
	public int compareTo(TreeNode o) {
		return key.compareTo(o.getKey());
	}

	public void addChildren(TreeNode item) {
		children.add(item);
	}
	public void addChildrens(Collection<? extends TreeNode> items) {
		children.addAll(items);
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<TreeNode> getChildren() {
		if(url!=null  && children.size()==0){
			return null;
		}
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	@SuppressWarnings("unchecked")
	public void setChildrens(List<? extends TreeNode> children) {
		this.children = (List<TreeNode>) children;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getQtip() {
		return qtip;
	}
	public void setQtip(String qtip) {
		this.qtip = qtip;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Boolean getLeaf() {
		return leaf;
	}
	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}
	public void setName(String name)
    {
        this.name = name;
    }
    public String getName() {
        return ( this.name == null || this.name.length() == 0 ) ? this.text : this.name;
    }

}
