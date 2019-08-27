package com.gcloud.boot.config;

import java.util.ArrayList;
import java.util.List;

public class ComponentPackages {
	private final static ComponentPackages INSTANCE = new ComponentPackages();
	List<String> controllerExcludes=new ArrayList<>();
	List<String> apiExcludes=new ArrayList<>();
	List<String> identityExcludes=new ArrayList<>();
	List<String> computeExcludes=new ArrayList<>();
	List<String> networkExcludes=new ArrayList<>();
	List<String> excludes=new ArrayList<>();
	List<String> storageExcludes=new ArrayList<>();
	List<String> imageExcludes=new ArrayList<>();
	
	private ComponentPackages() {
		excludes.add("com.gcloud.api");
		excludes.add("com.gcloud.identity");
		excludes.add("com.gcloud.controller");
		excludes.add("com.gcloud.compute");
		excludes.add("com.gcloud.core.quartz");
		excludes.add("com.gcloud.core.workflow");
		excludes.add("com.gcloud.network");
		excludes.add("com.gcloud.storage");
		excludes.add("com.gcloud.image");
		
		controllerExcludes.add("com.gcloud.api");
		controllerExcludes.add("com.gcloud.identity");
		controllerExcludes.add("com.gcloud.compute");
		controllerExcludes.add("com.gcloud.network");
		controllerExcludes.add("com.gcloud.storage");
		controllerExcludes.add("com.gcloud.image");
		
		apiExcludes.add("com.gcloud.controller");
		apiExcludes.add("com.gcloud.compute");
		apiExcludes.add("com.gcloud.identity");
		apiExcludes.add("com.gcloud.network");
		apiExcludes.add("com.gcloud.storage");
		apiExcludes.add("com.gcloud.image");
		
		identityExcludes.add("com.gcloud.controller");
		identityExcludes.add("com.gcloud.compute");
		identityExcludes.add("com.gcloud.api");
		identityExcludes.add("com.gcloud.network");
		identityExcludes.add("com.gcloud.storage");
		identityExcludes.add("com.gcloud.image");
		
		computeExcludes.add("com.gcloud.controller");
		computeExcludes.add("com.gcloud.api");
		computeExcludes.add("com.gcloud.identity");
		computeExcludes.add("com.gcloud.core.quartz");
		computeExcludes.add("com.gcloud.core.workflow");
		computeExcludes.add("com.gcloud.network");
		computeExcludes.add("com.gcloud.storage");
		computeExcludes.add("com.gcloud.image");

		networkExcludes.add("com.gcloud.controller");
		networkExcludes.add("com.gcloud.api");
		networkExcludes.add("com.gcloud.identity");
		networkExcludes.add("com.gcloud.core.quartz");
		networkExcludes.add("com.gcloud.core.workflow");
		networkExcludes.add("com.gcloud.compute");
		networkExcludes.add("com.gcloud.storage");
		networkExcludes.add("com.gcloud.image");

		storageExcludes.add("com.gcloud.controller");
		storageExcludes.add("com.gcloud.api");
		storageExcludes.add("com.gcloud.identity");
		storageExcludes.add("com.gcloud.core.quartz");
		storageExcludes.add("com.gcloud.core.workflow");
		storageExcludes.add("com.gcloud.compute");
		storageExcludes.add("com.gcloud.network");
		storageExcludes.add("com.gcloud.image");

		imageExcludes.add("com.gcloud.controller");
		imageExcludes.add("com.gcloud.api");
		imageExcludes.add("com.gcloud.identity");
		imageExcludes.add("com.gcloud.core.quartz");
		imageExcludes.add("com.gcloud.core.workflow");
		imageExcludes.add("com.gcloud.compute");
		imageExcludes.add("com.gcloud.storage");
		imageExcludes.add("com.gcloud.network");
    }

    public static ComponentPackages getInstance() {
        return INSTANCE;
    }
    
    public void init(List<String> componentIncludes){
    	//List<String> componentIncludes= ActiveProfiles.getInstance().getIncludes();
    	if(componentIncludes==null||componentIncludes.size()==0){
    		excludes=new ArrayList<String>();
    	}else{
    		for(String component:componentIncludes){
        		if(component.equals("controller")){
        			excludes.retainAll(controllerExcludes);
        		}else if(component.equals("identity")){
        			excludes.retainAll(identityExcludes);
        		}else if(component.equals("compute-node")){
        			excludes.retainAll(computeExcludes);
        		}else if(component.equals("api")) {
        			excludes.retainAll(apiExcludes);
        		}else if(component.equals("network-node")){
        			excludes.retainAll(networkExcludes);
				}else if(component.equals("storage-node")){
					excludes.retainAll(storageExcludes);
				}else if(component.equals("image-node")){
					excludes.retainAll(imageExcludes);
				}
        	}
    	}
    }
    public List<String> getExcludePackages(){
    	return excludes;
    }
}
