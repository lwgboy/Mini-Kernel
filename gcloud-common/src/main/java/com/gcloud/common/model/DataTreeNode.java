/*
 * @Date 2015-5-4
 * 
 * @Author dengyf@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved. 
 * 
 * @Description 树节点数据
 */
package com.gcloud.common.model;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.gcloud.common.util.StringUtils;
import sun.reflect.Reflection;

@SuppressWarnings({"restriction", "rawtypes"})
public class DataTreeNode <T> extends TreeNode{
	// Fields
		private T data;

		// Constructors
		public DataTreeNode() { super(); }

		public DataTreeNode(T data) {
			this();
			setData(data, false);
		}
		public DataTreeNode(T data, boolean autoSeek) {
			this();
			setData(data, autoSeek);
		}
		public DataTreeNode(String id, String type, String text) {
			super(id,type,text);
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}
		
		public void setData(T data, boolean autoSeek) {
			setData(data);
			if(autoSeek) this.autoSeek();
		}
		
		protected void autoSeek(){
			T d = this.data;
			if(d==null) return;
			if(type == null) this.setType(this.getClass().getSimpleName().replace("Node", "").toLowerCase());
			if(text == null){
				Method m = null;
				String[] mms = new String[]{"getGroupName", "getText", "getId", "getID"};
				for(String mm : mms){
					//System.out.println("autoSeek > "+mm);
					try{ m = d.getClass().getMethod(mm); break; }
					catch(NoSuchMethodException e){}
					catch(SecurityException e){}
				}
				if(m!=null){
					Object obj;
					try {
						obj = m.invoke(d);
						this.setText(obj==null ? "" : obj.toString());
						this.setQtip(obj==null ? "" : obj.toString());
					}
					catch (Exception e) {}
				}
			}
		}

		public Class getGenericType(int index) {
			Type genType = getClass().getGenericSuperclass();
			if (!(genType instanceof ParameterizedType)) {
				return Object.class;
			}
			Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
			if (index >= params.length || index < 0) {
				throw new RuntimeException("Index outof bounds");
			}
			if (!(params[index] instanceof Class)) {
				return Object.class;
			}
			return (Class) params[index];
		}
		
		@SuppressWarnings("unchecked")
		public <C extends DataTreeNode> List<C> covLists(List<T> ls) {
			Class cx = this.getClass();
			List<C> list = new ArrayList<C>();
			for(T d : ls){
				try{
					C cc = (C) cx.newInstance();
					cc.setData(d);
					list.add(cc);
				}
				catch(Exception e){}
			}
			return list;
		}
		
		@SuppressWarnings("unchecked")
		public static <T, C extends DataTreeNode<T>> List<C> asDataList(List<T> ls){
			return asDataList(ls, getCurrClass());
		}
		public static <T, C extends DataTreeNode<T>> List<C> asDataList(List<T> ls, Class<C> cx){
			List<C> list = new ArrayList<C>();
			for(T d : ls){
				try {
					list.add(asData(d, cx));
				} catch (Exception e) {}
			}
			return list;
		}
		@SuppressWarnings("unchecked")
		public static <T extends Serializable> DataTreeNode<T> asData(T d){
			return asData(d, getCurrClass());
		}
		@SuppressWarnings("unchecked")
		public static <C extends DataTreeNode, T> C asData(T d, Class<C> cx){
			C c = null;
			try {
				c = (C) cx.newInstance();
				c.setData(d, true);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return c;
		}
		
		@SuppressWarnings("deprecation")
		public static Class getCurrClass(){
			return Reflection.getCallerClass(1);
		}
		//静态方法中获取当前类名
		public static String getClassName(){
			return getClassName2();
		}
		public static String getClassName1(){
			//方法1 219ms
			return new SecurityManager() {
				public String getClassName() {
					return getClassContext()[1].getName();
				}
			}.getClassName();
		}
		public static String getClassName2(){
			new Throwable().printStackTrace();
			//方法2  953ms
			return new Throwable().getStackTrace()[1].getClassName();
		}
		public static String getClassName3(){
			//方法3  31ms
			return new Object() {
				public String getClassName() {
					String className = this.getClass().getName();
					return className.substring(0, className.lastIndexOf('$'));
				}
			}.getClassName();
		}
		@SuppressWarnings("deprecation")
		public static String getClassName4(){
			//方法4  60ms
			return Reflection.getCallerClass(1).getName();
		}
		
		@Override
		public String getType(){
			String type = super.getType();
			if(StringUtils.isBlank(type)){
				return data.getClass().getSimpleName();
			}
			return type;
		}
}
