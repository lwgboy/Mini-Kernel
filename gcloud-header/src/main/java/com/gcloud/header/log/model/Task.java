package com.gcloud.header.log.model;

import java.io.Serializable;
import java.util.UUID;

import com.gcloud.header.log.enums.LogResult;

public class Task implements Serializable{
	private String id;
	private String objectId;
	private String objectName;
	private String expect;
	private String errorCode;
	private LogResult result;
	public static TaskBuilder builder(){
		return new TaskBuilder();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getExpect() {
		return expect;
	}
	public void setExpect(String expect) {
		this.expect = expect;
	}
	
	public LogResult getResult() {
		return result;
	}
	public void setResult(LogResult result) {
		this.result = result;
	}


	public static  class TaskBuilder implements Serializable{
		private Task task;
		public TaskBuilder() {
			this.task = new Task();
			this.task.id = UUID.randomUUID().toString();
		}
		public TaskBuilder objectId(String objectId){
			this.task.objectId=objectId;
			return this;
		}
		public TaskBuilder objectName(String objectName){
			this.task.objectName=objectName;
			return this;
		}
		public TaskBuilder expect(String expect){
			this.task.expect=expect;
			return this;
		}
		public TaskBuilder taskId(String taskId){
			this.task.id=taskId;
			return this;
		}
		public TaskBuilder errorCode(String errorCode){
			this.task.errorCode=errorCode;
			return this;
		}
		public Task build(){
			return this.task;
		}
	}

	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
