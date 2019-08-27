package com.gcloud.controller.provider;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gcloud.common.util.RegexUtil;
import com.gcloud.controller.network.enums.Ethertype;
import com.gcloud.controller.network.model.AllocateEipAddressResponse;
import com.gcloud.controller.network.model.AuthorizeSecurityGroupParams;
import com.gcloud.core.exception.GCloudException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.network.AttachInterfaceType;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.NetFloatingIP;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.NetworkUpdate;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.QosBandwidthLimitRule;
import org.openstack4j.model.network.QosDirection;
import org.openstack4j.model.network.QosPolicy;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.RouterInterface;
import org.openstack4j.model.network.SecurityGroup;
import org.openstack4j.model.network.SecurityGroupRule;
import org.openstack4j.model.network.SecurityGroupUpdate;
import org.openstack4j.model.network.Subnet;
import org.openstack4j.model.network.builder.NetFloatingIPBuilder;
import org.openstack4j.model.network.builder.NetSecurityGroupBuilder;
import org.openstack4j.model.network.builder.NetSecurityGroupRuleBuilder;
import org.openstack4j.model.network.builder.PortBuilder;
import org.openstack4j.model.network.builder.RouterBuilder;
import org.openstack4j.model.network.builder.SubnetBuilder;
import org.openstack4j.model.network.ext.HealthMonitorV2;
import org.openstack4j.model.network.ext.LbMethod;
import org.openstack4j.model.network.ext.LbPoolV2;
import org.openstack4j.model.network.ext.ListenerProtocol;
import org.openstack4j.model.network.ext.ListenerV2;
import org.openstack4j.model.network.ext.LoadBalancerV2;
import org.openstack4j.model.network.options.PortListOptions;
import org.openstack4j.openstack.OSFactory;
import org.openstack4j.openstack.networking.domain.ext.ListItem;
import org.openstack4j.openstack.networking.domain.ext.NeutronLoadBalancerV2.LoadBalancerV2ConcreteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class NeutronProviderProxy extends OpenstackProviderProxy{
	
	@Autowired
	//NeutronProvider provider;
	public void setProvider(NeutronProvider provider) {
		// TODO Auto-generated method stub
		super.setProvider(provider);
	}
	
	
	public static void main(String[] args){
		OSClientV3 client=OSFactory.builderV3().endpoint("http://192.168.215.56:35357/v3")
				.credentials("admin", "Lz31rAr7ysSsp3XvujHjArWSPIpq6wZBDpOlyyKk", Identifier.byName("Default"))
				.scopeToProject(Identifier.byName("admin"), Identifier.byName("Default"))
				.authenticate();
//		System.out.println(client.networking().securitygroup().get("6fe6a75a-9ec0-4ec6-b9b0-d1ac00d87cc6"));


//		Network network = Builders.network()
//                .name("test_luoping")
//                .build();
//		client.networking().network().create(network);
//		
//		
//		Object obj = client.networking().network().list();
//		System.out.println(JSONObject.toJSONString(obj, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue));


		
//		SubnetBuilder builder = Builders.subnet().networkId("fe75fb82-ceca-41a3-a34b-23d96f3bcbbc").name("ha_sub");
//		builder.ipVersion(IPVersionType.V4).cidr("192.161.4.0/24");
//
//		Object obj = client.networking().subnet().create(builder.build());
//
//		System.out.println(JSONObject.toJSONString(obj, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue));
//

//		LoadBalancerV2ConcreteBuilder builder=new LoadBalancerV2ConcreteBuilder();
////		LoadBalancerV2 lb=builder.name("dfg").subnetId("7fb26392-5e9f-4474-86fd-44ed59277ae3").adminStateUp(true).build();
////		lb=client.networking().lbaasV2().loadbalancer().create(lb);'
//		
//		Object obj = client.networking().lbaasV2().loadbalancer().list();
////		
//		System.out.println(JSONObject.toJSONString(obj, SerializerFeature.PrettyFormat));
		
		
//		
//		Object obj2 = client.networking().lbaasV2().lbPool().list();
////		
//		System.out.println(JSONObject.toJSONString(obj2, SerializerFeature.PrettyFormat));
//		
		client.networking().lbaasV2().listener().delete("36d806e1-91d2-4604-95d2-1257af1ff517");
		client.networking().lbaasV2().listener().delete("9cd9a32a-931e-4cc8-98d0-e145bc25f262");
		client.networking().lbaasV2().listener().delete("fb167630-2d63-4c84-99ad-f4853ee8ad1c");
		Object obj3 = client.networking().lbaasV2().listener().list();
////		
		System.out.println(JSONObject.toJSONString(obj3, SerializerFeature.PrettyFormat));
		
//		SubnetBuilder builder = Builders.subnet().networkId("418d153d-fed0-4071-93c2-510da7ce40bb").name("test_eip");
//		builder.ipVersion(IPVersionType.V4).cidr("192.168.202.0/24");	
//		Subnet subnet = client.networking().subnet().create(builder.build());
//		Object obj4 = client.networking().network().get("418d153d-fed0-4071-93c2-510da7ce40bb");
//		System.out.println(JSONObject.toJSONString(obj4, SerializerFeature.PrettyFormat));
//		
//		
//		client.networking().subnet().delete("04466d6f-571f-4b82-8626-b3bf159e41bf");
//		client.networking().subnet().delete("1b8dcc25-9634-4509-81a0-67a8ba5e5b2a");
//		client.networking().subnet().delete("1d0d9091-937a-42aa-a814-626d1bf0ff06");
//		client.networking().subnet().delete("22519949-ddf7-441a-8481-b3abedb68afc");
//		client.networking().subnet().delete("253fbc75-298c-4448-9e2b-cfa3c133f54");
//		client.networking().subnet().delete("3c92fc71-4d21-4424-9f89-ba7ab6c1864f");
//		Object obj5 = client.networking().subnet().list();
//		System.out.println(JSONObject.toJSONString(obj5, SerializerFeature.PrettyFormat));
//		
//		client.networking().router().delete("3e0aaf14-e0c6-43bd-837a-1c7146ab60b2");
//		client.networking().router().detachInterface(routerId, subnetId, null);
//		client.networking().router().delete("842f5cd8-5e47-4c7b-ad18-f83851246a81");
//		client.networking().router().delete("c4c71cd6-410c-437c-8cdd-298974a0f5ef");
//		client.networking().router().delete("ca2d42d4-65ce-440e-853d-01c0eb926dbc");
//		Object obj6 = client.networking().router().list();
//		System.out.println(JSONObject.toJSONString(obj6, SerializerFeature.PrettyFormat));
		
//		client.networking().router().attachInterface("ca2d42d4-65ce-440e-853d-01c0eb926dbc", AttachInterfaceType.SUBNET, "253fbc75-298c-4448-9e2b-cfa3c133f54b");
//		client.networking().router().detachInterface("ca2d42d4-65ce-440e-853d-01c0eb926dbc", "253fbc75-298c-4448-9e2b-cfa3c133f54b", null);
//////		
//		Object obj6 = client.networking().subnet().get("0c44601e-705f-4f33-9158-9fc99d258b91");
//		System.out.println(JSONObject.toJSONString(obj6, SerializerFeature.PrettyFormat));
//		
//		Object obj7 = client.networking().port().list();
//		System.out.println(JSONObject.toJSONString(obj7, SerializerFeature.PrettyFormat));
		
//		Map<String, String> filter = new HashMap<String, String>();
//		PortListOptions popt = PortListOptions.create();
//		popt.networkId("3855d115-d549-4b4f-94e6-7c5bf144914e");
//		Object obj8 = (List<Port>)client.networking().port().list(popt);
//		System.out.println(JSONObject.toJSONString(obj8, SerializerFeature.PrettyFormat));
		
//		SubnetBuilder buillder = Builders.subnet();
//		buillder.name("test02_edit");
//		client.networking().subnet().update("065709f0-9b00-45d7-bdeb-bdd07368632f",buillder.build());
		
//		client.networking().router().detachInterface("842f5cd8-5e47-4c7b-ad18-f83851246a81", "859d6936-4a40-4e64-8a49-835d8474a901", null);
		
//		client.networking().router().attachInterface("842f5cd8-5e47-4c7b-ad18-f83851246a81", AttachInterfaceType.SUBNET, "3988c0c7-faf7-4e33-82ab-5f254bdb2a62");
//		
////		client.networking().lbaasV2().loadbalancer().delete("00c88cd3-c313-4284-aac3-91d31fe58060");
////		client.networking().lbaasV2().loadbalancer().delete("5d364b44-64bd-4ec0-b657-4455585034e3");
////		client.networking().lbaasV2().loadbalancer().delete("776c43cf-95c6-43a8-9f6d-796afaa17bea");
////		client.networking().lbaasV2().loadbalancer().delete("ff6b57c6-e512-45fd-bb43-8bd19bbaf911");
//		System.out.println(JSONObject.toJSONString(obj3, SerializerFeature.PrettyFormat));
//		Network build = Builders.network().name("test_eip").isRouterExternal(true).isShared(true).adminStateUp(true).build();
//		Network network = client.networking().network().create(build);
		
//		client.networking().network().delete("5abf2b7a-904e-4099-8e1f-78e3d3faf051");
//		client.networking().network().delete("5b8e10f6-7ea5-434e-81c5-4670e53229af");
//		client.networking().network().delete("77941a82-3535-47a8-a4b9-db7c2199a033");
//		client.networking().network().delete("8d42ad3b-ea96-4405-bf65-0806d0f683af");
//		client.networking().network().delete("aba2ba94-e393-4234-b09c-dd8a41618fb1");
//		client.networking().network().delete("aedd07c5-5e76-43e7-a681-07b17857c098");
//		client.networking().network().delete("b1aad3ec-bc3e-494e-946f-5945ef41ddaa");
//		client.networking().network().delete("f0fb59ed-2e1f-45a0-9c5a-782aaedf35da");
//		client.networking().network().delete("f88a6c35-a494-4378-93d2-b63d909353d1");
//		client.networking().network().delete("2e4015b4-9523-4aa8-bc62-abeb8366d386");
//		client.networking().network().delete("40f46218-7f72-4813-8034-a1f79843c1a8");
//		client.networking().network().delete("53110c32-60fb-45e5-bc04-a3be12088404");
//		client.networking().subnet().delete("6acf9a58-0f34-4a96-baa2-80f959dba14d");
//		client.networking().subnet().delete("b25ce51e-e4f7-4a0f-bf53-3ec4ea204729");
//		client.networking().subnet().delete("94bf366c-1a6c-4364-8b98-99bd28e5533b");
//		client.networking().subnet().delete("9cb74162-4dc7-454f-bf1c-7b385563ef9c");
//		client.networking().subnet().delete("5473dd93-9e05-4be3-9bfe-38a491c38d5e");
		
//		Object obj8 = client.networking().floatingip().list();
//		client.networking().floatingip().delete("080dc717-d3c8-4e53-a24b-375acf5cd2a8");
//		client.networking().floatingip().delete("5a01eecb-4881-49c0-983a-9d074c068118");
//		client.networking().floatingip().delete("62124fec-2cf1-48cd-a76c-d647743dfcb1");
//		client.networking().floatingip().delete("a435ab9e-d245-4022-a04b-809a1ae0884f");
//		client.networking().floatingip().delete("fe3f0cc5-10b7-4d65-90a5-a4bec50c4e25");
//		System.out.println(JSONObject.toJSONString(obj8, SerializerFeature.PrettyFormat));
		/*Map<String, String> pars = new HashMap<String, String>();
		pars.put("security_group_id", "6fe6a75a-9ec0-4ec6-b9b0-d1ac00d87cc6");
		pars.put("direction", "egress");
		
		
		System.out.println(client.networking().securityrule().list(pars));*/
	}
	
	public Network createExternalNetwork(String name){
		
		Network network = Builders.network()
                .name(name)
                .isRouterExternal(true)
                .isShared(true)
                .adminStateUp(true)
                .build();
		return createNetwork(network);
	}
	
	public Network createNetwork(String name){
		
		Network network = Builders.network()
                .name(name).adminStateUp(true)
                .build();
		return createNetwork(network);
	}
	
	public Network createNetwork(Network network){
		
		network= getClient().networking().network().create(network);
		return network;
	}
	
	public void deleteNetwork(String networkId) {
		getClient().networking().network().delete(networkId);
	}
	
	public Network getExternalNetwork(String id){
		return getClient().networking().network().get(id);
	}
	
	public void removeExternalNetwork(String id){
		getClient().networking().network().delete(id);
	}
	
	public void updateExternalNetwork(String id,String name){
		NetworkUpdate updater=Builders.networkUpdate().name(name).build();
		getClient().networking().network().update(id, updater);
	}
	
	public Subnet createSubnet(String networkId, String name, String crid){
		SubnetBuilder builder = Builders.subnet().networkId(networkId).name(name);
		builder.ipVersion(IPVersionType.V4).cidr(crid).enableDHCP(true);

		Subnet subnet = getClient().networking().subnet().create(builder.build());
		return subnet;
	}
	public void deleteSubnet(String id){
		getClient().networking().subnet().delete(id);
	}
	
	public Subnet getSubnet(String subnetId){
		return getClient().networking().subnet().get(subnetId);
	}
	
	public void modifySubnetAttribute(String subnetId,String subnetName) {
		
//		Subnet subnet = getClient().networking().subnet().get(subnetId);
//		subnet.setName(subnetName);
//		if (subnet != null) {
//			getClient().networking().subnet().update(subnet);
//		}
		
		SubnetBuilder buillder = Builders.subnet();
		buillder.name(subnetName);
		Subnet subnet = getClient().networking().subnet().get(subnetId);
		if (subnet != null) {
			getClient().networking().subnet().update(subnetId, buillder.build());
		}
	}
	
	public Port createPort(String name, String securityGroup, String subnetId, String primaryIp){
        PortBuilder portBuilder = Builders.port();

        Subnet subnet = getSubnet(subnetId);
        if (subnet == null) {
            throw new GCloudException("subnet is null");
        }

        if(StringUtils.isNotBlank(name)){
            portBuilder.name(name);
        }
        if(StringUtils.isNotBlank(securityGroup)){
            portBuilder.securityGroup(securityGroup);
        }
        portBuilder.networkId(subnet.getNetworkId());
        if(StringUtils.isNotBlank(primaryIp)){
            portBuilder.fixedIp(primaryIp, subnetId);
        }else{
            portBuilder.fixedIp(null, subnetId);
        }

		return createPort(portBuilder.build());
	}

	public Port createPort(Port createPort){
        Port port = null;
        try{
            port = getClient().networking().port().create(createPort);
        }catch (Exception ex){
            log.error("创建端口失败, " + ex, ex);
            throw new GCloudException("::创建端口失败");
        }

        if (port == null) {
            log.error("创建端口失败，port 为 null");
            throw new GCloudException("::创建端口失败");
        }

		return port;
	}
	
	public void deletePort(String id){
        ActionResponse response = null;
        try{
            response = getClient().networking().port().delete(id);
        }catch(Exception ex){
            log.error("删除端口失败" + ex, ex);
            throw new GCloudException("::删除端口失败");
        }

        if(response == null){
            log.error(String.format("删除端口失败,response为空,portId=%s", id));
            throw new GCloudException("::删除端口失败");
        }

        if (!response.isSuccess()) {
            log.error(String.format("删除端口失败,message=%s,code=%s,portId=%s", response.getFault(), response.getCode(), id));
            throw new GCloudException("::删除端口失败");
        }

	}
	
	public String createSecurityGroup(String name,String description){
		NetSecurityGroupBuilder builder = Builders.securityGroup().name(name).description(description);
		SecurityGroup securityGroup = getClient().networking().securitygroup().create(builder.build());
		return securityGroup.getId();
	}
	
	public void removeSecurityCroup(String id){
		getClient().networking().securitygroup().delete(id);
	}
	
	public SecurityGroup getSecurityCroup(String id){
		return getClient().networking().securitygroup().get(id);
	}
	
	public Router createRouter(String name){
		Router router = null;

		try {
			router = getClient().networking().router().create(Builders.router().name(name).build());
		} catch (Exception ex) {
			log.error("::创建路由器失败" + ex, ex);
			throw new GCloudException("::创建路由器失败");
		}

		if (router == null) {
			log.error("创建路由器失败，router 为 null");
			throw new GCloudException("::创建路由器失败");
		}
		
		return router;
	}
	
	public void deleteRouter(String routerId){
		ActionResponse response = null;
        try{
            response = getClient().networking().router().delete(routerId);
        }catch(Exception ex){
            log.error("删除路由失败" + ex, ex);
            throw new GCloudException("0020403::删除路由失败");
        }

        if(response == null){
            log.error(String.format("删除路由失败,response为空,routerId=%s", routerId));
            throw new GCloudException("0020404::删除端口失败");
        }

        if (!response.isSuccess()) {
            log.error(String.format("删除路由失败,message=%s,code=%s,routerId=%s", response.getFault(), response.getCode(), routerId));
            throw new GCloudException("0020405::删除路由失败");
        }
	}
	
	public void updateRouter(String routerId, String routerName){
		RouterBuilder routerBuilder = Builders.router().id(routerId).name(routerName);
		getClient().networking().router().update(routerBuilder.build());
	}
	
	public Router getRouter(String routerId) {
		return getClient().networking().router().get(routerId);
	}
	
	public void setVRouterGateway(String routerId, String vpcId){
		Router router = getRouter(routerId);
		// 更新路由
		RouterBuilder routerBuilder = Builders.router().from(router).externalGateway(vpcId);
		getClient().networking().router().update(routerBuilder.build());
	}
	
	public void cleanVRouterGateway(String routerId){
		Router router = getRouter(routerId);
		RouterBuilder builder = Builders.router().from(router).clearExternalGateway();
		getClient().networking().router().update(builder.build());
	}
	
	public RouterInterface attachSubnetRouter(String routerId, String subnetId){
		return getClient().networking().router().attachInterface(routerId, AttachInterfaceType.SUBNET, subnetId);
	}
	
	public void detachSubnetRouter(String routerId, String subnetId){
		detachSubnetRouter(routerId, subnetId, null);
	}
	
	public void detachSubnetRouter(String routerId, String subnetId, String portId){
		getClient().networking().router().detachInterface(routerId, subnetId, portId);
	}
	
	public void allocate(){
		
	}
	
	public void deallocate(){
		
	}
	
	public Port getPort(String portId){
		return getClient().networking().port().get(portId);
	}
	
	public Port updatePort(Port port){
		return getClient().networking().port().update(port);
	}
	
	public void modifySecurityGroupAttribute(String securityGroupId, String securityGroupName, String description) {
		SecurityGroupUpdate updateBuilder = Builders.securityGroupUpdate().name(securityGroupName).description(description).build();
		getClient().networking().securitygroup().update(securityGroupId, updateBuilder);
	}
	
	public String authorizeSecurityGroup(AuthorizeSecurityGroupParams params) {
		NetSecurityGroupRuleBuilder builder = Builders.securityGroupRule().direction(params.getDirection()).securityGroupId(params.getSecurityGroupId()).ethertype(Ethertype.IPv4.getValue());

		if (params.getIpProtocol().equals("all")) {
			builder.protocol(null);
		} else {
			builder.protocol(params.getIpProtocol());
		}

		if (params.getDirection().equals("egress")) {
			if(com.gcloud.common.util.StringUtils.isNotBlank(params.getDestGroupId())) {
				builder.remoteGroupId(params.getDestGroupId());
			}
			if(com.gcloud.common.util.StringUtils.isNotBlank(params.getDestCidrIp())) {
				builder.remoteIpPrefix(params.getDestCidrIp());
			}
		} else {
			if(com.gcloud.common.util.StringUtils.isNotBlank(params.getSourceGroupId())) {
				builder.remoteGroupId(params.getSourceGroupId());
			}
			if(com.gcloud.common.util.StringUtils.isNotBlank(params.getSourceCidrIp())) {
				builder.remoteIpPrefix(params.getSourceCidrIp());
			}
		}

		if (params.getPortRange() != null) {
			String[] ports = params.getPortRange().split("/");
			if (!"-1".equals(ports[0])) {
				if(RegexUtil.isValidPort(ports[0]) && RegexUtil.isValidPort(ports[1])) {
					builder.portRangeMin(Integer.parseInt(ports[0]));
					builder.portRangeMax(Integer.parseInt(ports[1]));
				} else {
					log.error("ValidatePortRange.Wrong::端口范围参数范围不对或格式不对");
					throw new GCloudException("ValidatePortRange.Wrong::端口范围参数范围不对或格式不对");
				}
					
			}
		}

		SecurityGroupRule rule = getClient().networking().securityrule().create(builder.build());
		
		return rule.getId();
	}
	
	public void revokeSecurityGroup(String securityGroupRuleId) {
		getClient().networking().securityrule().delete(securityGroupRuleId);
	}
	
	/*public DescribeSecurityGroupAttributeResponse describeSecurityGroupAttribute(String securityGroupId, String direction, String regionId) {
		SecurityGroup sGroup = getClient().networking().securitygroup().get(securityGroupId);

		DescribeSecurityGroupAttributeResponse response = new DescribeSecurityGroupAttributeResponse();
		PermissionTypes permissionTypes = new PermissionTypes();

		response.setDescription(sGroup.getDescription());
		response.setRegionId(regionId);
		response.setSecurityGroupId(sGroup.getId());
		response.setSecurityGroupName(sGroup.getName());
		
		Map<String, String> pars = new HashMap<String, String>();
		pars.put("security_group_id", securityGroupId);
		if(null != direction) {
			pars.put("direction", direction);
		}
		
		List<SecurityGroupRule> rules = (List<SecurityGroupRule>)getClient().networking().securityrule().list(pars);

		for (SecurityGroupRule rule : rules) {
			PermissionType type = new PermissionType();
			type.setSecurityGroupRuleId(rule.getId());
			type.setIpProtocol(rule.getProtocol());
			type.setDirection(rule.getDirection());

			if (rule.getDirection() != null) {
				if(rule.getDirection().equals(SecurityDirection.EGRESS.getValue())) {
					type.setDestGroupId(rule.getRemoteGroupId());
					type.setDestCidrIp(rule.getRemoteIpPrefix());
				} else if(rule.getDirection().equals(SecurityDirection.INGRESS.getValue())) {
					type.setSourceGroupId(rule.getRemoteGroupId());
					type.setSourceCidrIp(rule.getRemoteIpPrefix());
				}
			}

			if (rule.getPortRangeMin() != null && rule.getPortRangeMax() != null) {
				type.setPortRange(rule.getPortRangeMin() + "/" + rule.getPortRangeMax());
			} else {
				type.setPortRange("-1/-1");
			}

			permissionTypes.getPermission().add(type);
		}

		response.setPermissions(permissionTypes);
		
		return response;
	}*/

	public QosPolicy createQosPolicy(QosPolicy qosPolicy){
        QosPolicy newPolicy = null;
        try{
            newPolicy = getClient().networking().qosPolicies().create(qosPolicy);
        }catch (Exception ex){
            log.error("::创建qos策略失败", ex);
            throw new GCloudException("::创建qos策略失败");
        }

        if (newPolicy == null) {
            log.error("创建qos协议失败，new policy 为 null");
            throw new GCloudException("::创建qos策略失败");
        }

        return newPolicy;

    }

	public void deleteQosPolicy(String id){
		ActionResponse response = null;
		try{
			response = getClient().networking().qosPolicies().delete(id);
		}catch(Exception ex){
			log.error("删除qos协议失败", ex);
			throw new GCloudException("::删除qos协议失败");
		}

		if(response == null){
			log.error(String.format("删除qos协议失败,response为空,portId=%s", id));
			throw new GCloudException("::删除qos协议失败");
		}

		if (!response.isSuccess()) {
			log.error(String.format("删除qos协议失败,message=%s,code=%s,portId=%s", response.getFault(), response.getCode(), id));
			throw new GCloudException("::删除qos协议失败");
		}

	}

    public QosBandwidthLimitRule createQosBandwidthLimitRule(String policyId, Integer maxKbps, Integer maxBurstKbps, QosDirection qosDirection){

        QosBandwidthLimitRule rule = null;
        try{
            rule = getClient().networking().qosBandwidthLimitRulus().create(policyId, maxKbps, maxBurstKbps, qosDirection);
        }catch (Exception ex){
            log.error("::创建qos带宽规则失败", ex);
            throw new GCloudException("::创建qos带宽规则失败");
        }

        if (rule == null) {
            log.error("创建qos带宽规则失败，rule 为 null");
            throw new GCloudException("::创建qos带宽规则失败");
        }

        return rule;

    }

    public QosBandwidthLimitRule updateQosBandwidthLimitRule(String policyId, String ruleId, Integer maxKbps, Integer maxBurstKbps, QosDirection qosDirection){

        QosBandwidthLimitRule rule = null;
        try{
            rule = getClient().networking().qosBandwidthLimitRulus().update(policyId, ruleId, maxKbps, maxBurstKbps, qosDirection);
        }catch (Exception ex){
            log.error("::更新qos带宽规则失败", ex);
            throw new GCloudException("::更新qos带宽规则失败");
        }

        if (rule == null) {
            log.error("更新qos 带宽规则失败，rule 为 null");
            throw new GCloudException("::更新qos带宽规则失败");
        }

        return rule;

    }

	public void deleteQosBandwidthLimitRule(String policyId, String ruleId){
		ActionResponse response = null;
		try{
			response = getClient().networking().qosBandwidthLimitRulus().delete(policyId, ruleId);
		}catch(Exception ex){
			log.error("删除qos带宽限制规则失败", ex);
			throw new GCloudException("::删除qos带宽限制规则失败");
		}

		if(response == null){
			log.error(String.format("删除qos带宽限制规则失败,response为空,policyId=%s,ruleId=%s", policyId, ruleId));
			throw new GCloudException("::删除qos带宽限制规则失败");
		}

		if (!response.isSuccess()) {
			log.error(String.format("删除qos带宽限制规则失败,message=%s,code=%s,policyId=%s,ruleId=%s", response.getFault(), response.getCode(), policyId, ruleId));
			throw new GCloudException("::删除qos带宽限制规则失败");
		}

	}

	public AllocateEipAddressResponse allocateEipAddress(String networkId) {
		NetFloatingIPBuilder builder = Builders.netFloatingIP().floatingNetworkId(networkId);
		NetFloatingIP floatingIP = getClient().networking().floatingip().create(builder.build());
		AllocateEipAddressResponse response = new AllocateEipAddressResponse();
		response.setAllocationId(floatingIP.getId());
		response.setEipAddress(floatingIP.getFloatingIpAddress());
		response.setRouterId(floatingIP.getRouterId());
		response.setPortId(floatingIP.getPortId());
		response.setStatus(floatingIP.getStatus());
		
		return response;
	}
	
	public NetFloatingIP associateEipAddress(String allocationId, String netcardId) {
		NetFloatingIP floatingIP = getClient().networking().floatingip().associateToPort(allocationId, netcardId);
		return floatingIP;
	}
	
	public NetFloatingIP unAssociateEipAddress(String allocationId) {
		NetFloatingIP floatingIP = getClient().networking().floatingip().disassociateFromPort(allocationId);
		return floatingIP;
	}
	
	public void releaseEipAddress(String allocationId) {
		getClient().networking().floatingip().delete(allocationId);
	}
	
	public LoadBalancerV2  createloadBalancer(String loadBalancerName,String vSwitchId) {
		LoadBalancerV2ConcreteBuilder builder=new LoadBalancerV2ConcreteBuilder();
		LoadBalancerV2 lb=builder.name(loadBalancerName).subnetId(vSwitchId).adminStateUp(true).build();
		lb=getClient().networking().lbaasV2().loadbalancer().create(lb);
		if(lb==null){
			throw new GCloudException("创建负载均衡器失败");
		}
		return lb;
	}
	
	public  void   deleteLoadBalancer(String loadBalancerId) {
		ActionResponse resp = getClient().networking().lbaasV2().loadbalancer().delete(loadBalancerId);
		if (!resp.isSuccess()) {
			throw new GCloudException("删除负载均衡器失败:" + resp.getFault());
		}
		
	}
	
	public  LoadBalancerV2  getLoadBalancer(String loadBalancerId) {
		
		return  getClient().networking().lbaasV2().loadbalancer().get(loadBalancerId);
	}
	
	public void updateBalancerName(String loadBalancerId, String balancerName) {
		LoadBalancerV2 lber=getClient().networking().lbaasV2()
				.loadbalancer().update(loadBalancerId, Builders.loadBalancerV2Update().name(balancerName).build());
	}

	public List<LoadBalancerV2> listLoadBalancer(Map<String, String> filter) {
		List<LoadBalancerV2> lbs = (List<LoadBalancerV2>)getClient().networking().lbaasV2().loadbalancer().list(filter);
		return lbs;
	}

	public List<NetFloatingIP> listFloatingIps(Map<String, String> filter) {
		List<NetFloatingIP> fips = (List<NetFloatingIP>)getClient().networking().floatingip().list(filter);
		return fips;
	}

	public List<Network> listNetwork(Map<String, String> filter) {
		List<Network> networks = (List<Network>)getClient().networking().network().list(filter);
		return networks;
	}

	public List<SecurityGroup> listSecurityGroup(Map<String, String> filter) {
		List<SecurityGroup> sg = (List<SecurityGroup>)getClient().networking().securitygroup().list(filter);
		return sg;
	}

	public List<Port> listPort(Map<String, String> filter) {
		PortListOptions popt = PortListOptions.create();
		if (filter != null) {
			Set<String> set = filter.keySet();
			for (String key : set) {
			    String val = filter.get(key);
			    if (val == null || val.length() == 0) continue;
			    switch (key) {
					case "device_id": popt.deviceId(val); break;
					case "device_owner": popt.deviceOwner(val); break;
					case "network_id": popt.networkId(val); break;
					case "name": popt.name(val); break;
					case "tenant_id": popt.tenantId(val); break;
					case "mac_address": popt.macAddress(val); break;
					default: log.warn("::neutron端口列表，未知过滤参数:" + key); break;
				}
			}
		}
		List<Port> ports = (List<Port>)listPort(popt);
		return ports;
	}

	public List<? extends Port> listPort(PortListOptions options){
		return getClient().networking().port().list(options);
	}

	public List<Subnet> listSubnet(Map<String, String> filter) {
		List<Subnet> subnets = (List<Subnet>)getClient().networking().subnet().list();
		return subnets;
	}

	public List<Router> listRouter() {
		List<Router> routers = (List<Router>)getClient().networking().router().list();
		return routers;
	}
	
	public ListenerV2 createLoadBalancerHTTPListener(String loadBalancerId, String vServerGroupId, Integer listenerPort) {

		ListenerV2 listener = getClient().networking().lbaasV2().listener()
                .create(Builders.listenerV2()
                   .protocol(ListenerProtocol.HTTP)
                   .protocolPort(listenerPort)
                   .loadBalancerId(loadBalancerId)
                   .defaultPoolId(vServerGroupId)
                   .adminStateUp(true)
                   .build());
		if(listener == null) {
			throw new GCloudException("::创建HTTP监听器失败");
		}
		return listener;
		
	}
	
	public ListenerV2 createLoadBalancerHTTPSListener(String loadBalancerId, String vServerGroupId, Integer listenerPort, String ServerCertificateId) {

		ListenerV2 listener = getClient().networking().lbaasV2().listener()
                .create(Builders.listenerV2()
                   .protocol(ListenerProtocol.HTTPS)
                   .protocolPort(listenerPort)
                   .loadBalancerId(loadBalancerId)
                   .defaultPoolId(vServerGroupId)
                   .adminStateUp(true)
                   .build());
		if(listener == null) {
			throw new GCloudException("::创建HTTPS监听器失败");
		}
		return listener;
		
	}
	
	public ListenerV2 createLoadBalancerTCPListener(String loadBalancerId, String vServerGroupId, Integer listenerPort) {

		ListenerV2 listener = getClient().networking().lbaasV2().listener()
				.create(Builders.listenerV2()
						.protocol(ListenerProtocol.TCP)
						.protocolPort(listenerPort)
						.loadBalancerId(loadBalancerId)
						.defaultPoolId(vServerGroupId)
						.adminStateUp(true)
						.build());
	
		if(listener == null) {
			throw new GCloudException("::创建TCP监听器失败");
		}
		return listener;
		
	}
	
	public void deleteLoadBalancerListener(String listenerId) {
		
		ListenerV2 listener = getClient().networking().lbaasV2().listener().get(listenerId);
		String poolId = listener.getDefaultPoolId();
		String monitorId = null;
		
		boolean hasMonitor = false;
		List<? extends HealthMonitorV2> healthMonitor = getClient().networking().lbaasV2().healthMonitor().list();
		
		//查询pool中对应的healthMonitor
		for (HealthMonitorV2 healthMonitorV2 : healthMonitor) {
			List<ListItem> pools = healthMonitorV2.getPools();
			for (ListItem pool : pools) {
				if(pool.getId().equals(poolId)) {
					hasMonitor = true;
					monitorId = healthMonitorV2.getId();
					break;
				}
			}
			if(hasMonitor) {
				break;
			}
		}
		
		if(hasMonitor) {
			if(StringUtils.isNotEmpty(monitorId)) {
				ActionResponse monitorResponse = getClient().networking().lbaasV2().healthMonitor().delete(monitorId);
				if(!monitorResponse.isSuccess()) {
					throw new GCloudException("::删除健康检查失败" + monitorResponse.getFault());
				}
			}
		}
		
		ActionResponse resp = getClient().networking().lbaasV2().listener().delete(listenerId);
		if (!resp.isSuccess()) {
			throw new GCloudException("::删除监听器失败" + resp.getFault());
		}
		log.debug("DeleteListener end...");
		
	}
	
	public void setLoadBalancerHTTPListenerAttribute(String listenerId, String vServerGroupId) {
		
		ListenerV2 listener = getClient().networking().lbaasV2().listener().get(listenerId);
		if (StringUtils.isNotBlank(vServerGroupId)) {	
			if (listener != null && !StringUtils.equals(listener.getDefaultPoolId(), vServerGroupId)) {
				ListenerV2 updated = getClient().networking().lbaasV2().listener()
						.update(listenerId, Builders.listenerV2Update()
								.defaultPoolId(vServerGroupId)
								.adminStateUp(true)
								.build());
				
				if (updated == null) {
					throw new GCloudException("::更新监听器失败");
				}
			}
		}
		else {
			throw new GCloudException("::后端服务器组id不能为空");
		}
		
		log.debug("UpdateListener end...");
	}
	
	public void setLoadBalancerHTTPSListenerAttribute(String listenerId, String vServerGroupId, String serverCertificateId) {
		ListenerV2 listener = getClient().networking().lbaasV2().listener().get(listenerId);
		if (StringUtils.isNotBlank(vServerGroupId)) {	
			if (listener != null && !StringUtils.equals(listener.getDefaultPoolId(), vServerGroupId)) {
				ListenerV2 updated = getClient().networking().lbaasV2().listener()
						.update(listenerId, Builders.listenerV2Update()
								.defaultPoolId(vServerGroupId)
								.adminStateUp(true)
								.build());
				
				if (updated == null) {
					throw new GCloudException("::更新监听器失败");
				}
			}
		}
		else {
			throw new GCloudException("后端服务器组id不能为空");
		}
		
		log.debug("UpdateListener end...");
	}
	
	public void setLoadBalancerTCPListenerAttribute(String listenerId, String vServerGroupId) {

		ListenerV2 listener = getClient().networking().lbaasV2().listener().get(listenerId);
		if (StringUtils.isNotBlank(vServerGroupId)) {
			if (listener != null && !StringUtils.equals(listener.getDefaultPoolId(), vServerGroupId)) {
				ListenerV2 updated = getClient().networking().lbaasV2().listener()
						.update(listenerId, Builders.listenerV2Update()
								.defaultPoolId(vServerGroupId)
								.adminStateUp(true)
								.build());
				
				if (updated == null) {
					throw new GCloudException("::更新监听器失败");
				}
			}
		}
		else {
			throw new GCloudException("后端服务器组id不能为空");
		}
		
		log.debug("UpdateTCPListener end...");
	}
	
	public ListenerV2 describeLoadBalancerHTTPListenerAttribute(String listenerId) {
	
		ListenerV2 listener = getClient().networking().lbaasV2().listener().get(listenerId);
		
		if (listener == null) {
			throw new GCloudException("获取HTTP监听器详情失败");
		}
		
		log.info("DescribeHTTPListener end...");
		
		return listener;
	}
	
	public ListenerV2 describeLoadBalancerHTTPSListenerAttribute(String listenerId) {
		
		ListenerV2 listener = getClient().networking().lbaasV2().listener().get(listenerId);
		
		if (listener == null) {
			throw new GCloudException("::获取HTTP监听器详情失败");
		}
		
		log.info("DescribeHTTPSListener end...");
		
		return listener;
	}
	
	public ListenerV2 describeLoadBalancerTCPListenerAttribute(String listenerId) {
		
		ListenerV2 listener = getClient().networking().lbaasV2().listener().get(listenerId);
		
		if (listener == null) {
			throw new GCloudException("::获取TCP监听器详情失败");
		}
		
		log.info("DescribeTCPListener end...");
		
		return listener;
	}
	
	public LbPoolV2 describeSchedulerAttribute(String resourceId, String protocol) {
		
		LbPoolV2 pool = getClient().networking().lbaasV2().lbPool().get(resourceId);
		if (pool == null) {
			throw new GCloudException("::获取Scheduler详情失败");
		}
		log.debug("DescribeSchedulerAttribute end...");
		
		return pool;
	}
	
	public void setSchedulerAttribute(String resourceId, String protocol, String scheduler) {

		if (StringUtils.isNotBlank(scheduler) && StringUtils.isNotBlank(resourceId)) {
			getClient().networking().lbaasV2().lbPool()
				.update(resourceId, Builders.lbPoolV2Update()
						.adminStateUp(true)
						.lbMethod(LbMethod.forValue(scheduler))
						.build());
		}
		
		log.debug("SetSchedulerAttribute end...");
	}
	
	
}
