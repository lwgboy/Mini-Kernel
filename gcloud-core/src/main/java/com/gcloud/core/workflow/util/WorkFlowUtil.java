package com.gcloud.core.workflow.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.core.workflow.entity.FlowCommandValueTemplate;
import com.gcloud.core.workflow.entity.WorkFlowInstance;
import com.gcloud.core.workflow.entity.WorkFlowInstanceStep;
import com.gcloud.core.workflow.enums.FlowStepStatus;
import com.gcloud.core.workflow.enums.ParamType;
import com.gcloud.core.workflow.mng.IFlowCommandValueTemplateMng;
import com.gcloud.core.workflow.mng.IWorkFlowInstanceMng;
import com.gcloud.core.workflow.mng.IWorkFlowInstanceStepMng;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class WorkFlowUtil {
	private final static String REPEAT_PARAMS_FIELD = "repeatParams";
	private final static String REPEAT_TYPE_SERIAL = "serial";
	public final static int ERRORCODE_LEN = 250;
	
	/**
	 * @param workFlowStepId work_flow_instance_Step表中的自增ID
	 * @return
	 */
	public static JSONObject initStepReqParams(Long workFlowStepId)
	{
		IWorkFlowInstanceStepMng stepmng = (IWorkFlowInstanceStepMng)SpringUtil.getBean("workFlowInstanceStepMng");
		IFlowCommandValueTemplateMng paramVMng = (IFlowCommandValueTemplateMng)SpringUtil.getBean("flowCommandValueTemplateMng");
		IWorkFlowInstanceMng mng = (IWorkFlowInstanceMng)SpringUtil.getBean("workFlowInstanceMng");
		
		WorkFlowInstanceStep step = stepmng.findById(workFlowStepId);
		
		WorkFlowInstance instance = mng.findById(step.getFlowId());
		
		JSONObject pars = new JSONObject();
		
		List<FlowCommandValueTemplate> parsTemplate = paramVMng.getTemplatesByStepId(instance.getFlowTypeCode(), step.getTemplateStepId());
		for(FlowCommandValueTemplate tem:parsTemplate)
		{
			if(tem.getFromStepId() != 0)
			{
				WorkFlowInstanceStep paramSourceStep = stepmng.findByTemplateStepId(tem.getFromStepId(), step.getFlowId());
				if(null == paramSourceStep) {
					continue;
				}
				if(tem.getFromParamType().equals(ParamType.RES.getType()))
				{
					if(StringUtils.isBlank(paramSourceStep.getResJson()))
					{
						continue;
					} else if(StringUtils.isNotBlank(paramSourceStep.getRepeatType()) && paramSourceStep.getRepeatType().equals(REPEAT_TYPE_SERIAL)) {
						JSONArray repeatResArr = stepmng.getRepeatStepRes(step.getFlowId(),tem.getFromStepId());
						packageParamsArr(tem, repeatResArr, pars);
					} else {
//						Object json = new JSONTokener(paramSourceStep.getResJson()).nextValue();
						if(paramSourceStep.getResJson().startsWith("{")){
							JSONObject resObj = (JSONObject)JSONObject.parse(paramSourceStep.getResJson());
							packageParams(tem, resObj, pars);
						}else {
							JSONArray resArr = (JSONArray)JSONObject.parse(paramSourceStep.getResJson());
							packageParamsArr(tem, resArr, pars);
						}
					}
				}
				else
				{
					if(StringUtils.isBlank(paramSourceStep.getReqJson()))
					{
						continue;
					}
					//req 请求入参暂不考虑JSONArray情况
					JSONObject reqObj = (JSONObject)JSONObject.parse(paramSourceStep.getReqJson());
					packageParams(tem, reqObj, pars);
				}
			}
			else
			{
				JSONObject paramsObj = (JSONObject)JSONObject.parse(instance.getParamsJson());
				if(null != instance.getBatchParams() && tem.getFromFieldName().equals("batchParams")) {
					pars.put(tem.getFieldName(), (JSONObject)JSONObject.parse(instance.getBatchParams()));
				} else {
					packageParams(tem, paramsObj, pars);
				}
			}
		}
		
		return pars;
	}
	
	public static List<JSONObject> getFlowTaskRes(Long flowTaskId) {
		//flow_task参数返回，返回批量流程最后步骤的返回数据list
		List<JSONObject> result = new ArrayList<JSONObject>();
		IWorkFlowInstanceStepMng stepmng = (IWorkFlowInstanceStepMng)SpringUtil.getBean("workFlowInstanceStepMng");
		List<String> resStrs = stepmng.getLastStepRess(flowTaskId);
		for(String resStr:resStrs) {
			/*JSONObject item = JSONObject.parseObject(resStr);
			result.add(item);*/
			if(resStr.startsWith("{")){
				result.add(JSONObject.parseObject(resStr));
			}else {
				JSONArray resArr = (JSONArray)JSONObject.parse(resStr);
				List<JSONObject> lists = resArr.parseArray(resStr, JSONObject.class);
				result.addAll(lists);
			}
		}
		
		return result;
	}
	
	public static <E> List<E> getFlowTaskFirstStepRes(Long flowTaskId, Class<E> clazz) {
		//flow_task参数返回，返回批量流程第一步步骤的返回数据list
		List<E> result = new ArrayList<E>();
		IWorkFlowInstanceStepMng stepmng = (IWorkFlowInstanceStepMng)SpringUtil.getBean("workFlowInstanceStepMng");
		List<String> resStrs = stepmng.getFirstStepRess(flowTaskId);
		if(resStrs.size() > 0) {
			for(String res :resStrs) {
				if(res.startsWith("{")){
					result.add(JSONObject.parseObject(res, clazz));
				}else {
					JSONArray resArr = (JSONArray)JSONObject.parse(res);
					List<E> lists = resArr.parseArray(res, clazz);
					result.addAll(lists);
				}
			}
		}
		return result;
	}
	
	/**暂时没有考虑动态批量并行command的情况，因为flow_task类型的步骤本身就执行并行批量
	 * @param repeatStep
	 * @return
	 */
	public static WorkFlowInstanceStep editWorkFlowInstanceStep(WorkFlowInstanceStep repeatStep) {
		IWorkFlowInstanceStepMng stepmng = (IWorkFlowInstanceStepMng)SpringUtil.getBean("workFlowInstanceStepMng");
		JSONObject params = initStepReqParams(repeatStep.getId());
		
		WorkFlowInstanceStep result = null;
		JSONArray jsonArr = null;
		try {
	        jsonArr = JSONArray.parseArray(params.get(REPEAT_PARAMS_FIELD).toString());
	    } catch (Exception e) {
	    }
		if(null != jsonArr) {
			int repeatSize = jsonArr.size();
			if(repeatSize > 1) {
				IWorkFlowInstanceMng instanceMng = (IWorkFlowInstanceMng)SpringUtil.getBean("workFlowInstanceMng");
				WorkFlowInstance instance = instanceMng.findById(repeatStep.getFlowId());
				
				Integer repeatIdFrom = Integer.parseInt(repeatStep.getTemplateStepId() + "0000");
				for(int i=0;i<repeatSize;i++) {
					if(i != repeatSize-1) {
						WorkFlowInstanceStep step = new WorkFlowInstanceStep();
						step.setExecCommand(repeatStep.getExecCommand());
						step.setFromIds(i==0?repeatStep.getFromIds():(repeatIdFrom + i -1) + "");
						step.setNecessary(repeatStep.isNecessary());
						step.setState(FlowStepStatus.NOT_EXECUTE.name());
						step.setFlowId(repeatStep.getFlowId());
						step.setTemplateStepId(repeatIdFrom + i);
						step.setyToIds((i != repeatSize-2)?((step.getTemplateStepId() + 1) + ""):(repeatStep.getTemplateStepId()+""));
						step.setnToIds(repeatStep.getnToIds());
						step.setStepType(repeatStep.getStepType());
						step.setAsync(repeatStep.isAsync());
						step.setFromRelation(repeatStep.getFromRelation());
						step.setRepeatIndex(i);
						params.put(REPEAT_PARAMS_FIELD, jsonArr.get(i));
						step.setReqJson(params.toJSONString());
						step.setRepeatType(repeatStep.getRepeatType());
						step.setTopestFlowTaskId(instance.getTopestFlowTaskId());
						step.setRollbackFailContinue(repeatStep.isRollbackFailContinue());
						step.setStepDesc(repeatStep.getStepDesc());
						
						Long id = stepmng.save(step);
						
						if(i == 0 ) {
							result = stepmng.findById(id);
							//改变上一步的yToIds\nToIds
							if(StringUtils.isNotBlank(repeatStep.getFromIds())) {
								String[] fromStepIds = repeatStep.getFromIds().split(",");
								for(String fromStepId:fromStepIds) {
									WorkFlowInstanceStep fromStep = stepmng.findByTemplateStepId(Integer.parseInt(fromStepId), repeatStep.getFlowId());
									List<String> updateFields = new ArrayList<String>();
									if(!StringUtils.isBlank(fromStep.getyToIds())) {
										String replaceYtoId = replaceStepId(fromStep.getyToIds(), repeatStep.getTemplateStepId(), step.getTemplateStepId());
										updateFields.add(fromStep.updateyToIds(replaceYtoId));
									}
									if(!StringUtils.isBlank(fromStep.getnToIds())) {
										String replaceNtoId = replaceStepId(fromStep.getnToIds(), repeatStep.getTemplateStepId(), step.getTemplateStepId());
										updateFields.add(fromStep.updatenToIds(replaceNtoId));
									}
									stepmng.update(fromStep, updateFields);
								}
							}
						}
					} else {
						//update
						List<String> updateFields = new ArrayList<String>();
						updateFields.add(repeatStep.updateFromIds((repeatIdFrom + repeatSize - 2) + ""));
						updateFields.add(repeatStep.updateRepeatIndex(repeatSize-1));
						params.put(REPEAT_PARAMS_FIELD, jsonArr.get(i));
						updateFields.add(repeatStep.updateReqJson(params.toJSONString()));
						stepmng.update(repeatStep, updateFields);
					}
				}
			} else {
				List<String> updateFields = new ArrayList<String>();
				updateFields.add(repeatStep.updateRepeatIndex(1));
				if(jsonArr.size() == 1) {
					params.put(REPEAT_PARAMS_FIELD, jsonArr.get(0));
				} else {
					params.put(REPEAT_PARAMS_FIELD, null);
				}
				updateFields.add(repeatStep.updateReqJson(params.toString()));
				stepmng.update(repeatStep, updateFields);
				
			}
			
		} else {
			List<String> updateFields = new ArrayList<String>();
			updateFields.add(repeatStep.updateRepeatIndex(1));
			updateFields.add(repeatStep.updateReqJson(params.toString()));
			stepmng.update(repeatStep, updateFields);
		}
		if(null == result) {
			return repeatStep;
		} else {
			return result;
		}
	}
	
	private static void packageParamsArr(FlowCommandValueTemplate tem, JSONArray fromDataArr, JSONObject pars) {
		try {
			JSONArray result = new JSONArray();
			for(int i=0;i<fromDataArr.size();i++){
				JSONObject fromData = fromDataArr.getJSONObject(i);
				if(StringUtils.isNotBlank(tem.getFromFieldName())) {
					if(fromData.containsKey(tem.getFromFieldName()))
					{
						if(StringUtils.isBlank(tem.getFieldName())) {
							log.error("FlowCommandValueTemplate id[" + tem.getId() + "]配置疑似错误,fieldName为空");
	//						result.add(fromData.get(tem.getFromFieldName()).toString());
						} else {
							result.add(fromData.get(tem.getFromFieldName()));
						}
					}
				} else {
					if(StringUtils.isBlank(tem.getFieldName())) {
						log.error("FlowCommandValueTemplate id[" + tem.getId() + "]配置疑似错误,fieldName为空");
	//					packageAllFields(fromData.toString(), pars);
					} else {
						result.add(fromData);
					}
				}
			}
			if(StringUtils.isNotBlank(tem.getFieldName())) {
				pars.put(tem.getFieldName(), result);
			}
			
		} catch(Exception e) {
			log.error("FlowCommandValueTemplate id[" + tem.getId() + "]配置错误,封装数据失败,fromData:" + fromDataArr.toJSONString());
		}
	}
	
	private static void packageParams(FlowCommandValueTemplate tem, JSONObject fromData, JSONObject pars) {
		try {
			if(StringUtils.isNotBlank(tem.getFromFieldName())) {
				if(fromData.containsKey(tem.getFromFieldName()))
				{
					if(StringUtils.isBlank(tem.getFieldName())) {
						packageAllFields(fromData.get(tem.getFromFieldName()).toString(), pars);
					} else {
						pars.put(tem.getFieldName(), fromData.get(tem.getFromFieldName()));
					}
				}
			} else {
				if(StringUtils.isBlank(tem.getFieldName())) {
					packageAllFields(fromData.toString(), pars);
				} else {
					pars.put(tem.getFieldName(), fromData);
				}
			}
		} catch(Exception e) {
			log.error("FlowCommandValueTemplate id[" + tem.getId() + "]配置错误,封装数据失败,fromData:" + fromData.toJSONString());
		}
	}
	
	private static void packageAllFields(String jsonStr, JSONObject pars) {
		JSONObject  tmpJson = JSONObject.parseObject(jsonStr);
		Map<String, Object> temMap = tmpJson;
		for (Map.Entry<String, Object> entry : temMap.entrySet()) { 
			pars.put(entry.getKey(), entry.getValue()); 
		}
	}
	
	private static String replaceStepId(String ids, Integer oldId, Integer newId) {
		String[] toIds = ids.split(",");
		String result = "";
		for(String id:toIds) {
			if(id.equals(oldId + "")) {
				result += newId + ",";
			} else {
				result += id + ",";
			}
		}
		return result.substring(0, result.length() - 1);
	}
	
	public static String getSubStr(String strs, int length) {
		int strLen = null== strs?0:strs.length();
		if(strLen > length) {
			return strs.substring(0, length-3) + "...";
		} else {
			return strs;
		}
	}
	
}
