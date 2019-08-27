package com.gcloud.core.workflow.core.handler;

import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import com.gcloud.core.workflow.entity.FlowTask;
import com.gcloud.core.workflow.mng.IFlowTaskMng;
import com.gcloud.core.workflow.model.WorkflowFirstStepResException;
import com.gcloud.core.workflow.util.WorkFlowUtil;
import com.gcloud.header.GMessage;
import com.gcloud.header.ReplyMessage;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.apache.commons.lang.StringUtils;
@Slf4j
public abstract class BaseWorkFlowHandler<T extends GMessage,REPY extends ReplyMessage> extends MessageHandler<T,REPY> {

	@Override
	public REPY handle(T msg) throws GCloudException {
		preProcess(msg);
		try {
			BaseWorkFlows flows = (BaseWorkFlows)SpringUtil.getBean(getWorkflowClass());
			flows.setTaskId(msg.getTaskId());//同步操作的taskId与工作流的taskId
			flows.execute(BeanUtil.convertBeanToMap(initParams(msg)));
		} catch (GCloudException ge) {
			throw ge;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new GCloudException("::系统异常，请联系管理员");
		}
		return process(msg);
	}
	
	/**任务流前置处理，如数据库校验等的逻辑可以放在这里
	 * @param msg
	 * @return
	 * @throws GCloudException
	 */
	public abstract Object preProcess(T msg) throws GCloudException;

	public abstract REPY process(T msg) throws GCloudException;

	public abstract Class getWorkflowClass();
	
	public Object initParams(T msg) {
		return msg;
	}

	public <E> List<E> getFlowTaskFirstStepRes(String taskId, Class<E> clazz) throws GCloudException{
		IFlowTaskMng flowTaskMng = (IFlowTaskMng)SpringUtil.getBean("flowTaskMng");
		FlowTask flowTask = flowTaskMng.findUnique("taskId", taskId);
		return WorkFlowUtil.getFlowTaskFirstStepRes(flowTask.getId(), clazz);
	}
	
	public <E> E getFlowTaskFirstStepFirstRes(String taskId, Class<E> clazz) throws GCloudException{
		List<E> response = getFlowTaskFirstStepRes(taskId, clazz);
		if(response != null && response.size() > 0) {
			if(response.get(0) instanceof WorkflowFirstStepResException) {
				WorkflowFirstStepResException error = (WorkflowFirstStepResException)response.get(0);
				if(!StringUtils.isBlank(error.getErrorMsg())) {
					throw new GCloudException(error.getErrorMsg());
				}
			}
			
			return response.get(0);
		}
		return null;
	}

}
