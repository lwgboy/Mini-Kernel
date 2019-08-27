package com.gcloud.core.simpleflow;


public abstract class Flow<T> {
	private String name;
	private boolean rollbackCurrentFlow = false;
	public Flow(){
		
	}
	public Flow(String name){
		this.name=name;
	}

	public Flow(boolean rollbackCurrentFlow) {
		this.rollbackCurrentFlow = rollbackCurrentFlow;
	}

	public Flow(String name, boolean rollbackCurrentFlow) {
		this.name = name;
		this.rollbackCurrentFlow = rollbackCurrentFlow;
	}

	public abstract void run(SimpleFlowChain chain, T data);
	public abstract void rollback(SimpleFlowChain chain,T data);
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRollbackCurrentFlow() {
		return rollbackCurrentFlow;
	}

	public void setRollbackCurrentFlow(boolean rollbackCurrentFlow) {
		this.rollbackCurrentFlow = rollbackCurrentFlow;
	}
}
