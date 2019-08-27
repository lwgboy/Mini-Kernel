package com.gcloud.header;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gcloud.header.log.model.Task;

public class GMessage implements Serializable {
    @JsonIgnore
    private String msgId;
    @JsonIgnore
    private String serviceId;
    @JsonIgnore
    private String taskId;
    @JsonIgnore
    private String objectId;
    @JsonIgnore
    private String objectName;
    @JsonIgnore
    private String taskExpect;
    @JsonIgnore
    private String remark; //备注信息，如记录操作原因
    @JsonIgnore
    List<Task> tasks;


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTaskId() {
        /*if (taskId == null) {
            taskId = UUID.randomUUID().toString();
        }*/
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public String getTaskExpect() {
        return taskExpect;
    }

    public void setTaskExpect(String taskExpect) {
        this.taskExpect = taskExpect;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Task> getTasks() {
        if (null == tasks) {
            tasks = new ArrayList<Task>();
        }
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public <T extends GMessage> T deriveMsg(Class<T> clazz){

        T obj = null;
        try{
           obj = clazz.newInstance();
        }catch (Exception ex){

        }
        if(obj != null){
            ((GMessage)obj).setTaskId(this.getTaskId());
        }

        return obj;
    }
}
