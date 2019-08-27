package com.gcloud.core.simpleflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.gcloud.core.exception.GCloudException;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class SimpleFlowChain<T, Resl>{
	private Resl result;
	private String name;
	private T data ;
	private List<Flow> flows = new ArrayList<Flow>();
    private Stack<Flow> rollBackFlows = new Stack<Flow>();
    private Iterator<Flow> it;
    private Flow currentFlow;
    private FlowErrorHandler errorHandler;
    private FlowDoneHandler doneHandler;
    private FlowFinallyHandler finallyHandler;
    private String errorCode;
    public SimpleFlowChain(){
    	
    }
    public SimpleFlowChain(String name){
    	this.name="simple flow "+ name;
    }

    
    public SimpleFlowChain name(String name){
    	this.name="simple flow "+ name;
    	return this;
    }
    
    public SimpleFlowChain then(Flow flow) {
        flows.add(flow);
        return this;
    }
    
    public void next(){
    	rollBackFlows.push(currentFlow);
    	Flow flow=getNextFlow();
    	if(flow==null){
    		if(errorCode==null){
    			callDoneHandler();
    		}else{
    			callErrorHandler();
    		}
    	}else{
    		runFlow(flow);
    	}
    }
    
    public SimpleFlowChain done(FlowDoneHandler handler){
    	doneHandler=handler;
    	return this;
    }
    public SimpleFlowChain error(FlowErrorHandler handler) {
        errorHandler = handler;
        return this;
    }
    public SimpleFlowChain flowFinally(FlowFinallyHandler handler) {
        finallyHandler = handler;
        return this;
    }
    public void rollback(){
    	if (rollBackFlows.empty()) {
            callErrorHandler();
            return;
        }
    	Flow flow = rollBackFlows.pop();
        //currentRollbackFlow = flow;
        rollbackFlow(flow);
    }
    
    public void start(){
    	if (flows.isEmpty()) {
            callDoneHandler();
            return;
        }
    	it = flows.iterator();
        Flow flow = getNextFlow();
        if (flow == null) {
            callDoneHandler();
        } else {
            runFlow(flow);
        }
    }
    
    private void rollbackFlow(Flow flow) {
        try {
            log.debug(String.format("start to rollback chain %s, flow %s",name, flow.getName()));
            flow.rollback(this, data);
        } catch (Throwable t) {
        	log.error(String.format("rollback error, chain %s, name:%s", name, flow.getName()), t);
            rollback();
        }
    }
    
    private Flow getNextFlow(){
    	if(it.hasNext()){
    		return it.next();
    	}
    	return null;
    }
    
    public void fail(String errorCode) {
        this.errorCode=errorCode;
        if(currentFlow.isRollbackCurrentFlow()){
            rollBackFlows.push(currentFlow);
        }
        rollback();
    }
    
    private void runFlow(Flow flow) {
    	try{
    		currentFlow =flow;
    		flow.run(this, data);
    	}catch (GCloudException e) {
			// TODO: handle exceptiong
    		log.error(String.format("error in chain:%s flow:%s", this.name,flow.getName()));
    		log.error(e.getMessage(), e);
    		fail(e.getMessage());
		}catch (Exception e) {
			// TODO: handle exception
			log.error(String.format("error in chain:%s flow:%s", this.name,flow.getName()));
			log.error(e.getMessage(), e);
    		fail("系统异常");
		}
    }
    
    private void callDoneHandler(){
    	if (doneHandler != null) {
            try {
                doneHandler.handle(this.data);
            } catch (Throwable t) {
                log.error(String.format("done handler error, name:%s", name), t);
            }
        }
    	callFinallyHandler();
    }
    private void callErrorHandler(){
    	 if (errorHandler != null) {
             try {
                 errorHandler.handle( this.data);
             } catch (Throwable t) {
            	 log.error(String.format("error handler error, name:%s", name), t);
             }
         }
    	 callFinallyHandler();
    }
    private void callFinallyHandler() {
        if (finallyHandler != null) {
            try {
                finallyHandler.handle();
            } catch (Throwable t) {
            	 log.error(String.format("finally handle error, name:%s", name), t);
            }
        }
    }
	public Resl getResult() {
		return result;
	}
	public void setResult(Resl result) {
		this.result = result;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

    public SimpleFlowChain data(T data) {
        this.data = data;
        return this;
    }

    public T data(){
        return this.data;
    }
}
