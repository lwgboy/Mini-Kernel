package com.gcloud.core.simpleflow;

public abstract class NoRollbackFlow<T> extends Flow<T>{
	public NoRollbackFlow(){
		
	}
	public NoRollbackFlow(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void rollback(SimpleFlowChain chain,T data){
		chain.rollback();
	}
}
