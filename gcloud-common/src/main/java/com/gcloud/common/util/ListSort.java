/*
 * @Date 2015-4-14
 * 
 * @Author dengyf@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved. 
 * 
 * @Description List按指定字段排序
 * 使用方法：<br/>
 * 创建ListSort的实例，在调用其构造方法的时候传入3个参数：
 * list：要进行排序的List；
 * name：List中存放的对象的某个字段，排序根据该字段进行；
 * direction：排序的方式，"asc"升序，"desc"降序；
 * 然后调用Sort()方法，返回结果即是按要求排好序的List
 */
package com.gcloud.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListSort<T> {
	
	/** 
	* @Fields list : 要进行排序的List
	*/
	List<T> list = new ArrayList<T>();
	/** 
	* @Fields name : List中存放的对象的某个字段，排序根据该字段进行；
	*/
	String name;
	/** 
	* @Fields direction : 排序的方式，"asc"升序，"desc"降序
	*/
	Direction direction = Direction.ASC;

	/** 
	* <p>Title: ListSort</p> 
	* <p>Description: ListSort无参构造方法</p>  
	*/
	public ListSort(){
	  
	}
	
	/** 
	* <p>Title: ListSort</p> 
	* <p>Description: ListSort的全参构造方法</p> 
	* @param list： 要进行排序的List
	* @param name：List中存放的对象的某个字段，排序根据该字段进行
	* @param direction:排序的方式，"asc"升序，"desc"降序
	*/
	public ListSort(List<T> list,String name,String direction) {
	   this.list = list;
	   this.name = "get"+name.substring(0,1).toUpperCase()+name.substring(1, name.length());
	   Direction direct;
	   if(direction==null)
	   {
		   direction = "asc";
	   }
	   if(direction.equals("asc"))
	   {
		   direct  = Direction.ASC;
	   }
	   else
	   {
		   direct  = Direction.DESC;
	   }
	   this.direction = direct;
	}
	
	/** 
	* @Title: Sort 
	* @Description: 对ListSort中的list字段进行排序
	* @param @return  
	* @return ArrayList  按要求排好序的List
	* @throws 
	*/
	public List<T> Sort() {
	   Collections.sort(list,getSorterById(direction));
	   return this.list;
	}

	@SuppressWarnings("unchecked")
	private Comparator<T> getSorterById(final Direction order) {
	   return new Comparator<T>() {
		   public int compare(Object o1, Object o2) {
		     T u1 = (T) o1;
		     T u2 = (T) o2;
		     Method m1 = null;
		     Method m2 = null;
		     try {
			      m1 = u1.getClass().getMethod(name);//获取list对象中又name指定的方法
			      m2 = u2.getClass().getMethod(name);
		     } catch (SecurityException e) {
			      e.printStackTrace();
		     } catch (NoSuchMethodException e) {
			      e.printStackTrace();
		     }
		    
		     int s1 = 0;
		     if (Direction.ASC == order) {
			      try {
				       if (m1.invoke(u1).toString().compareTo(m2.invoke(u2).toString())>0) {
				    	   s1 = 1;
				       } else if (m1.invoke(u1).toString().compareTo(m2.invoke(u2).toString())<0) {
				    	   s1 = -1;
				       } else {
				    	   s1 = 0;
				       }
			      } catch (NumberFormatException e) {
				       e.printStackTrace();
			      } catch (IllegalArgumentException e) {
				       e.printStackTrace();
			      } catch (IllegalAccessException e) {
				       e.printStackTrace();
			      } catch (InvocationTargetException e) {
				       e.printStackTrace();
			      }
		     } else {
			      try {
				       if (m2.invoke(u2).toString().compareTo(m1.invoke(u1).toString())>0) {
				    	   s1 = 1;
				       } else if (m2.invoke(u2).toString().compareTo(m1.invoke(u1).toString())<0) {
				    	   s1 = -1;
				       } else {
				    	   s1 = 0;
				       }
			      } catch (NumberFormatException e) {
				       e.printStackTrace();
			      } catch (IllegalArgumentException e) {
				       e.printStackTrace();
			      } catch (IllegalAccessException e) {
				       e.printStackTrace();
			      } catch (InvocationTargetException e) {
				       e.printStackTrace();
			      }
		     }
		     return s1;
		   }
	   };
	}
	
	/**
	 * @ClassName Direction
	 * @Description 排序方式：<p>
	 * ASC:升序<br/>
	 * DESC:降序
	 * @author dengyf@gdeii.com.cn
	 * @date 2012-6-27
	 */
	enum Direction{
		   ASC,
		   DESC
	};
}
