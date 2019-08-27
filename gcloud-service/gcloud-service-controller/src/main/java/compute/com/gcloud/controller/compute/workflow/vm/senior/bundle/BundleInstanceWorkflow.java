package com.gcloud.controller.compute.workflow.vm.senior.bundle;

import com.gcloud.controller.compute.workflow.model.senior.BundleInstanceWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/12/3.
 */
@Component
@Scope("prototype")
@Slf4j
public class BundleInstanceWorkflow extends BaseWorkFlows {

    @Override
    public String getFlowTypeCode() {
        return "bundleInstanceWorkflow";
    }

    @Override
    public Object preProcess() {
        return null;
    }

    @Override
    public void process() {

    }

    @Override
    protected Class<?> getReqParamClass() {
        return BundleInstanceWorkflowReq.class;
    }
}
