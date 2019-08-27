package com.gcloud.controller.compute.workflow.vm.trash;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.workflow.model.trash.ForceDetachAndDeleteDiskWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/12/4.
 */
@Component
@Scope("prototype")
@Slf4j
public class ForceDetachAndDeleteDiskWorkflow extends BaseWorkFlows {

    @Override
    public String getFlowTypeCode() {
        return "forceDetachAndDeleteDiskWorkflow";
    }

    @Override
    public Object preProcess() {
    	return null;
    }

    @Override
    public void process() {

    }

    @Override
    public boolean judgeExecute() {
        ForceDetachAndDeleteDiskWorkflowReq req = (ForceDetachAndDeleteDiskWorkflowReq)getReqParams();
        return req != null && req.getRepeatParams() != null && StringUtils.isNotBlank(req.getRepeatParams().getDiskId());
    }

    @Override
    protected Class<?> getReqParamClass() {
        return ForceDetachAndDeleteDiskWorkflowReq.class;
    }
}
