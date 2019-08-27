package com.gcloud.api.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;

public final class ModifiableHttpServletRequest extends HttpServletRequestWrapper {
	private final Map<String, String> customHeaders;
	private final ParameterMap<String, String[]> params;
	
	public ModifiableHttpServletRequest(HttpServletRequest request) {
		super(request);
		this.customHeaders = new HashMap<String, String>();
		params = (ParameterMap)request.getParameterMap();
	}
	
	public void putHeader(String name, String value){
        this.customHeaders.put(name, value);
    }
 
    public String getHeader(String name) {
        // check the custom headers first
        String headerValue = customHeaders.get(name);
        
        if (headerValue != null){
            return headerValue;
        }
        // else return from into the original wrapped object
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }
    
    public Enumeration<String> getHeaders(String name) {
        Set<String> set = new HashSet<String>();
        if(customHeaders.containsKey(name) && null != customHeaders.get(name)) {
        	set.add(customHeaders.get(name).toString());
        }
        
        @SuppressWarnings("unchecked")
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaders(name);
        while (e.hasMoreElements()) {
            String n = e.nextElement();
            set.add(n);
        }
 
        return Collections.enumeration(set);
    }
 
    public Enumeration<String> getHeaderNames() {
        Set<String> set = new HashSet<String>(customHeaders.keySet());
        
        // now add the headers from the wrapped request object
        @SuppressWarnings("unchecked")
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            String n = e.nextElement();
            set.add(n);
        }
 
        return Collections.enumeration(set);
    }
    
    @Override
    public String getParameter(String name) {
        String[] values = params.get(name);
        if(values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }
    
    @Override
    public Map<String, String[]> getParameterMap() {
	    return params;
    }
    
    public void addParameter(String name , Object value) {
        if(value != null) {
        	params.setLocked(false);
            if(value instanceof String[]) {
                params.put(name , (String[])value);
            } else if(value instanceof String) {
                params.put(name , new String[] {(String)value});
            } else {
                params.put(name , new String[] {String.valueOf(value)});
            }
            params.setLocked(true);
        }
    }

}
