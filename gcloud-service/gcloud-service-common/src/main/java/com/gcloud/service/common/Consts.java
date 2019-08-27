package com.gcloud.service.common;

public class Consts {

	public static final class RedisKey {

		private static final String GCLOUD_PREFIX = "gcloud";
		private static final String CONTROLLER_PREFIX = "controller";
		private static final String COMPUTE_PREFIX = "compute";

		private static final String CONTROLLER_COMPUTE_PREFIX = String.format("%s:%s:%s:", GCLOUD_PREFIX, CONTROLLER_PREFIX, COMPUTE_PREFIX);

		// 修改时，要修改对于的VmControllerUtil.getHostNameByAliveKey
		public static final String GCLOUD_CONTROLLER_COMPUTE_NODES_COMPUTE_NODE_ALIVE = CONTROLLER_COMPUTE_PREFIX + "nodes:compute_node_alive_{0}";
		// 修改时，要修改对于的VmControllerUtil.getHostNameByHostKey
		public static final String GCLOUD_CONTROLLER_COMPUTE_NODES_LOCK_HOSTBANE = CONTROLLER_COMPUTE_PREFIX + "nodes:lock_hostname_{0}";
		public static final String GCLOUD_CONTROLLER_COMPUTE_NODES_COMPUTE_NODE_HOSTNAME = CONTROLLER_COMPUTE_PREFIX + "nodes:compute_node_hostname_{0}";


		// node_ha
		public static final String GCLOUD_CONTROLLER_COMPUTE_NODE_HA_CACHE = CONTROLLER_COMPUTE_PREFIX + "ha:node_ha_{0}";

		// adopt
		public static final String GCLOUD_CONTROLLER_COMPUTE_ADOPT_SET = CONTROLLER_COMPUTE_PREFIX + "adopt:set_{0}";

		//sync
		//{0} 为hostname
		public static final String GCLOUD_CONTROLLER_COMPUTE_SYNC_INSTANCE_STATE = CONTROLLER_COMPUTE_PREFIX + "sync:instance_{0}";


		//dispatcher
		public static final String GCLOUD_CONTROLLER_COMPUTE_DISPATCHER_LOCK = CONTROLLER_COMPUTE_PREFIX + "dispatcher:dispatch";

	}

	public static final class Time {

		public static final long NODES_REDIS_LOCK_TIMEOUT = 1000L; // 单位s
		public static final long NODES_REDIS_LOCK_GET_LOCK_TIMEOUT = -1 * 1000L; // 单位ms

        public static final long DISPATCHER_REDIS_LOCK_TIMEOUT = 1;
        public static final long DISPATCHER_REDIS_LOCK_GET_LOCK_TIMEOUT = -1 * 1000L;



	}

	public static final class Hypervisor{

		public static final String HYPERVISOR_CONFIG = "gcloud.computeNode.hypervisor";
		public static final String KVM = "kvm";

	}

	public static final String DEFAULT_DISK_DRIVER = "virtio";

}
