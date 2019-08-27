package com.gcloud.header.identity.user;

import java.util.List;

import com.gcloud.header.PageReplyMessage;

public class DescribeUserReplyMsg extends PageReplyMessage<UserModel>{
	private List<UserModel> datas;
	
	@Override
	public void setList(List<UserModel> list) {
		setDatas(list);
	}

	public List<UserModel> getDatas() {
		return datas;
	}

	public void setDatas(List<UserModel> datas) {
		this.datas = datas;
	}
}
