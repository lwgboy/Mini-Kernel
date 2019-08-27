package com.gcloud.header.identity.tenant;

import java.util.List;

import com.gcloud.header.PageReplyMessage;

public class DescribeTenantReplyMsg extends PageReplyMessage<TenantModel>{
	private List<TenantModel> datas;
	
	@Override
	public void setList(List<TenantModel> list) {
		setDatas(list);
	}

	public List<TenantModel> getDatas() {
		return datas;
	}

	public void setDatas(List<TenantModel> datas) {
		this.datas = datas;
	}
}
