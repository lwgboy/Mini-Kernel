package com.gcloud.common.model;

public enum ResultType {
	Success(1, "Success", "成功"),
	Failure(2, "Failure", "失败"),
	Running(3, "Running", "进行中"),
	Timeout(4, "Timeout", "超时");
	
	ResultType(int value, String enname, String cnname){
		this.value=(byte)value;
		this.enname=enname;
		this.cnname=cnname;
	}
	/*ResultType(byte value, String enname, String cnname){
		this.value=value;
		this.enname=enname;
		this.cnname=cnname;
	}*/
	
	private byte value;
	private String enname;
	private String cnname;
	
	public byte getValue() {
		return value;
	}
	/*public void setValue(byte value) {
		this.value = value;
	}*/
	public String getEnname() {
		return enname;
	}
	/*public void setEnname(String enname) {
		this.enname = enname;
	}*/
	public String getCnname() {
		return cnname;
	}
	/*public void setCnname(String cnname) {
		this.cnname = cnname;
	}*/
}
