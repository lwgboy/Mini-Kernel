package com.gcloud.header;

import java.util.List;


public abstract class ListReplyMessage<E> extends ApiReplyMessage {
public abstract void setList(List<E> list);
	
	public void init(List<E> list){
		setList(list);
	}
}
