package com.gcloud.core.simpleflow;

public abstract class FlowDoneHandler<T> {
	public abstract  void handle(T data);
}
