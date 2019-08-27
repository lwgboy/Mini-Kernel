package com.gcloud.controller.compute.workflow.model.vm;

/**
 * Created by yaowj on 2018/11/30.
 */
public class JudgeNeedStartupFlowCommandReq {

    private String beginningState;

    public String getBeginningState() {
        return beginningState;
    }

    public void setBeginningState(String beginningState) {
        this.beginningState = beginningState;
    }
}
