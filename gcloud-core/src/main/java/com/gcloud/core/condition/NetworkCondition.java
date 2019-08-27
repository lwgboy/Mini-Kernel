package com.gcloud.core.condition;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class NetworkCondition extends SpringBootCondition{
	
	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		// TODO Auto-generated method stub
		//String kvmServicePkg="com.gcloud.compute.service.kvm";
		Map<String, Object> attributes = metadata
				.getAnnotationAttributes(ConditionalNetwork.class.getName());
		if(attributes==null)
			return ConditionOutcome.noMatch("");
		PropertyResolver resolver=context.getEnvironment();
		String hypervisorConfig =resolver.getProperty("gcloud.controller.component.network");
		String hypervisor=(String) attributes.get("component");
		if(hypervisor.equalsIgnoreCase(hypervisorConfig))
			return ConditionOutcome.match();
		return ConditionOutcome.noMatch("");
	}

}
