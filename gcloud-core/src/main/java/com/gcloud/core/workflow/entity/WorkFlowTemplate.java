package com.gcloud.core.workflow.entity;

import java.io.Serializable;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name="gc_work_flow_template")
public class WorkFlowTemplate implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ID
	private Integer id;
	private String flowTypeCode;//任务流类型代码
	private Integer stepId; //步骤ID
	private String execCommand;//flow command class类名 或 flowTypeCode
	private String stepType;//command、flow；flow时代表是工作流嵌套
	private String fromIds; //上一级ID,逗号隔开
	private String yToIds; //下一级ID,逗号隔开
	private String nToIds; //步骤为非必要条件且步骤执行失败时，下一步id
	private boolean necessary; //是否是必要条件，0 | 1；步骤执行失败时，1则执行rollback，0则执行nToIds
	private boolean async;//0 同步，1异步；一般stepType为flow时都为异步
	private String fromRelation;//父记录的关系（from_ids为多条记录时有效），FROM_ALL_DONE、FROM_ONE_DONE,默认FROM_ONE_DONE
	private String repeatType;
	private boolean visible;//用于判断步骤是否需要展示在页面上
	private boolean rollbackAsync;//0 同步，1异步；
	private boolean rollbackSkip;//是否跳过回滚的代码逻辑
	//实例流程回滚失败后并且实例流程配置成回滚失败不继续回滚时，改配置为true时，该步骤依然回滚
	private boolean rollbackFailContinue;
	private String stepDesc;//步骤描述，0~128 varchar
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFlowTypeCode() {
		return flowTypeCode;
	}
	public void setFlowTypeCode(String flowTypeCode) {
		this.flowTypeCode = flowTypeCode;
	}
	public Integer getStepId() {
		return stepId;
	}
	public void setStepId(Integer stepId) {
		this.stepId = stepId;
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
	public String getFromRelation() {
		return fromRelation;
	}
	public void setFromRelation(String fromRelation) {
		this.fromRelation = fromRelation;
	}
	public String getRepeatType() {
		return repeatType;
	}
	public void setRepeatType(String repeatType) {
		this.repeatType = repeatType;
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
	
}
