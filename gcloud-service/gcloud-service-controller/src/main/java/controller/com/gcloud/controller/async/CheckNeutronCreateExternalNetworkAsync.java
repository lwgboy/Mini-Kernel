package com.gcloud.controller.async;

import java.util.ArrayList;
import java.util.List;

import org.openstack4j.model.network.State;

import com.gcloud.controller.network.dao.NetworkDao;
import com.gcloud.controller.network.entity.Network;
import com.gcloud.controller.provider.NeutronProviderProxy;
import com.gcloud.core.async.AsyncResult;
import com.gcloud.core.async.AsyncStatus;
import com.gcloud.core.service.SpringUtil;

public class CheckNeutronCreateExternalNetworkAsync extends LogAsync{
	
	private String networkId;
	private String status;
	
	
	public String getNetworkId() {
		return networkId;
	}
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public long timeout() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public AsyncResult execute() {
		// TODO Auto-generated method stub
		AsyncStatus asyncStatus = null;
		NeutronProviderProxy proxy = SpringUtil.getBean(NeutronProviderProxy.class);
		Network entity = getNetworkById(getNetworkId());
		org.openstack4j.model.network.Network network = proxy.getExternalNetwork(entity.getProviderRefId());
		if(network.getStatus().equals(State.ACTIVE)) {
			asyncStatus = AsyncStatus.SUCCEED;
			this.status = network.getStatus().name();
		}else if(network.getStatus().equals(State.ERROR)) {
			asyncStatus = AsyncStatus.FAILED;
			this.status = network.getStatus().name();
		}else {
			asyncStatus =  AsyncStatus.RUNNING;
			this.status = network.getStatus().name();
		}
		return new AsyncResult(asyncStatus);

	}
	
	private void handler() {
		NetworkDao  dao = SpringUtil.getBean(NetworkDao.class);
		Network network = getNetworkById(getNetworkId());
		if (network != null) {
			List<String> updatedField = new ArrayList<String>();
			updatedField.add(network.updateStatus(getStatus()));
			dao.update(network);
		}
				
	}
    public void successHandler(){
        this.handler();
    }
    public void failHandler(){
    	this.handler();
    }
    
    public Network getNetworkById(String networkId) {
    	NetworkDao  dao = SpringUtil.getBean(NetworkDao.class);
    	return dao.getById(getNetworkId());
    }

}
