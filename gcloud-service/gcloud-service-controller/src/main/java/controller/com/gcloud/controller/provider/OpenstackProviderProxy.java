package com.gcloud.controller.provider;

import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.openstack.OSFactory;

import java.util.Date;

public abstract class OpenstackProviderProxy {
	private Provider provider;
	public void setProvider(Provider provider){
		this.provider=provider;
	}
	private Token token;
	public OSClientV3 getClient(){
		if(token==null||token.getExpires().before(new Date())){
			OSClientV3 client=OSFactory.builderV3().endpoint(provider.getAuthUrl())
					.credentials(provider.getUsername(), provider.getPassword(), Identifier.byName("Default"))

					.scopeToProject(Identifier.byName( provider.getProject()), Identifier.byName("Default"))
					.authenticate();
			this.token=client.getToken();
			return client;
		}else{
			return OSFactory.clientFromToken(token);
		}
	}
	
	public String getUrl() {
	    return this.provider.getUrl();
	}
	
}
