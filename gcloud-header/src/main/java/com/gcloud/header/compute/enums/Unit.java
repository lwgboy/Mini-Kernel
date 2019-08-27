package com.gcloud.header.compute.enums;

public enum Unit {
	
	G("G"),
    M("M"),
    K("K"),
	T("T");
	
    private String value;
    
    Unit(String value){
        this.value=value;
    }
    public String getValue(){
        return this.value;
    }

}
