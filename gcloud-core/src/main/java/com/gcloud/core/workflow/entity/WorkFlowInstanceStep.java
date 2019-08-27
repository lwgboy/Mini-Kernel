package com.gcloud.core.workflow.entity;

import java.io.Serializable;
import java.util.Date;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name="gc_work_flow_instance_step")
public class WorkFlowInstanceStep implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ID
	private Long id;
	private Long flowId;
	private Integer templateStepId;
	private String taskId;
	private String execCommand;
	private String stepType;
	private String reqJson;
	private String resJson;
	private String fromIds;
	private String yToIds;
	private String nToIds;
	private Date startTime;
	private Date updateTime;
	private String state;
	private boolean necessary;
	private boolean async;
	private boolean rollbackAsync;//0 同步，1异步；
	private boolean rollbackSkip;//是否跳过回滚的代码逻辑
	private String rollbackTaskId;
	private String fromRelation;//父记录的关系（from_ids为多条记录时有效），FROM_ALL_DONE、FROM_ONE_DONE,默认FROM_ONE_DONE
	private int timeOut;
	private boolean visible;//用于判断步骤是否需要展示在页面上
	
	private String repeatType;
	private Integer repeatIndex;
	
	//实例流程回滚失败后并且实例流程配置成回滚失败不继续回滚时，改配置为true时，该步骤依然回滚
	private boolean rollbackFailContinue;
	private String topestFlowTaskId;
	private String stepDesc;
	private Date rollbackStartTime;
	private Date rollbackUpdateTime;
	
	public static String ID = "id";
    public static String STATE = "state";
    public static String UPDATE_TIME = "updateTime";
    public static String START_TIME = "startTime";
    public static String TASK_ID = "taskId";
    public static String REQ_JSON = "reqJson";
    public static String RES_JSON = "resJson";
    public static String ROLLBACK_TASK_ID = "rollbackTaskId";
    public static String TIME_OUT = "timeOut";
    public static String Y_TO_IDS = "yToIds";
    public static String N_TO_IDS = "nToIds";
    public static String FROM_IDS = "fromIds";
    public static String REPEAT_INDEX = "repeatIndex";
    public static String ROLLBACK_UPDATE_TIME = "rollbackUpdateTime";
    public static String ROLLBACK_START_TIME = "rollbackStartTime";
    
    public String updateId(Long id) {
        this.setId(id);
        return ID;
    }
    
    public String updateFromIds(String fromIds) {
    	this.setFromIds(fromIds);
    	return FROM_IDS;
    }
    
    public String updateRepeatIndex(Integer repeatIndex) {
    	this.setRepeatIndex(repeatIndex);
    	return REPEAT_INDEX;
    }
    
    public String updateyToIds(String yToIds) {
    	this.setyToIds(yToIds);
    	return Y_TO_IDS;
    }
    
    public String updatenToIds(String nToIds) {
    	this.setnToIds(nToIds);
    	return N_TO_IDS;
    }
    
    public String updateState(String state) {
    	this.setState(state);
    	return STATE;
    }
    
    public String updateUpdateTime(Date updateTime) {
    	this.setUpdateTime(updateTime);;
    	return UPDATE_TIME;
    }
    
    public String updateStartTime(Date startTime) {
    	this.setStartTime(startTime);;
    	return START_TIME;
    }
    
    public String updateTaskId(String taskId) {
    	this.setTaskId(taskId);
    	return TASK_ID;
    }
    
    public String updateRollbackTaskId(String rollbackTaskId) {
    	this.setRollbackTaskId(rollbackTaskId);
    	return ROLLBACK_TASK_ID;
    }
    
    public String updateReqJson(String reqJson) {
    	this.setReqJson(reqJson);
    	return REQ_JSON;
    }
    
    public String updateResJson(String resJson) {
    	this.setResJson(resJson);
    	return RES_JSON;
    }
    
    public String updateTimeOut(int timeOut) {
    	this.setTimeOut(timeOut);
    	return TIME_OUT;
    }
    
    public String updateRollbackUpdateTime(Date updateTime) {
    	this.setRollbackUpdateTime(updateTime);;
    	return ROLLBACK_UPDATE_TIME;
    }
    
    public String updateRollbackStartTime(Date startTime) {
    	this.setRollbackStartTime(startTime);;
    	return ROLLBACK_START_TIME;
    }
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getFlowId() {
		return flowId;
	}
	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
	public Integer getTemplateStepId() {
		return templateStepId;
	}
	public void setTemplateStepId(Integer templateStepId) {
		this.templateStepId = templateStepId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getExecCommand() {
		return execCommand;
	}
	public void setExecCommand(String execCommand) {
		this.execCommand = execCommand;
	}
	public String getStepType() {
		return stepType;
	}
	public void setStepType(String stepType) {
		this.stepType = stepType;
	}
	public String getReqJson() {
		return reqJson;
	}
	public void setReqJson(String reqJson) {
		this.reqJson = reqJson;
	}
	public String getResJson() {
		return resJson;
	}
	public void setResJson(String resJson) {
		this.resJson = resJson;
	}
	public String getFromIds() {
		return fromIds;
	}
	public void setFromIds(String fromIds) {
		this.fromIds = fromIds;
	}
	public String getyToIds() {
		return yToIds;
	}
	public void setyToIds(String yToIds) {
		this.yToIds = yToIds;
	}
	public String getnToIds() {
		return nToIds;
	}
	public void setnToIds(String nToIds) {
		this.nToIds = nToIds;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public boolean isNecessary() {
		return necessary;
	}
	public void setNecessary(boolean necessary) {
		this.necessary = necessary;
	}
	public boolean isAsync() {
		return async;
	}
	public void setAsync(boolean async) {
		this.async = async;
	}
	public String getRollbackTaskId() {
		return rollbackTaskId;
	}
	public void setRollbackTaskId(String rollbackTaskId) {
		this.rollbackTaskId = rollbackTaskId;
	}
	public String getFromRelation() {
		return fromRelation;
	}
	public void setFromRelation(String fromRelation) {
		this.fromRelation = fromRelation;
	}
	public int getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
	public String getRepeatType() {
		return repeatType;
	}
	public void setRepeatType(String repeatType) {
		this.repeatType = repeatType;
	}
	public Integer getRepeatIndex() {
		return repeatIndex;
	}
	public void setRepeatIndex(Integer repeatIndex) {
		this.repeatIndex = repeatIndex;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public boolean isRollbackAsync() {
		return rollbackAsync;
	}
	public void setRollbackAsync(boolean rollbackAsync) {
		this.rollbackAsync = rollbackAsync;
	}
	public boolean isRollbackSkip() {
		return rollbackSkip;
	}
	public void setRollbackSkip(boolean rollbackSkip) {
		this.rollbackSkip = rollbackSkip;
	}

	public String getTopestFlowTaskId() {
		return topestFlowTaskId;
	}

	public void setTopestFlowTaskId(String topestFlowTaskId) {
		this.topestFlowTaskId = topestFlowTaskId;
	}

	public boolean isRollbackFailContinue() {
		return rollbackFailContinue;
	}

	public void setRollbackFailContinue(boolean rollbackFailContinue) {
		this.rollbackFailContinue = rollbackFailContinue;
	}

	public String getStepDesc() {
		return stepDesc;
	}

	public void setStepDesc(String stepDesc) {
		this.stepDesc = stepDesc;
	}

	public Date getRollbackStartTime() {
		return rollbackStartTime;
	}

	public void setRollbackStartTime(Date rollbackStartTime) {
		this.rollbackStartTime = rollbackStartTime;
	}

	public Date getRollbackUpdateTime() {
		return rollbackUpdateTime;
	}

	public void setRollbackUpdateTime(Date rollbackUpdateTime) {
		this.rollbackUpdateTime = rollbackUpdateTime;
	}
	
}
