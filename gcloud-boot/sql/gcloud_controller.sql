DROP DATABASE IF EXISTS `gcloud_controller`;
CREATE DATABASE `gcloud_controller`
    CHARACTER SET 'utf8'
    COLLATE 'utf8_general_ci';
USE gcloud_controller;

SET FOREIGN_KEY_CHECKS=0;

-- --------------------------------------------------------------------------------------------------------
-- --------------------------------------------- 任务流机制 sql -----------------------------------------------
-- --------------------------------------------------------------------------------------------------------


DROP TABLE IF EXISTS `gc_work_flow_type`;
CREATE TABLE `gc_work_flow_type` (
  `flow_type_code` char(64) NOT NULL,
  `flow_type_name` varchar(50) DEFAULT NULL,
  `description` varchar(64) DEFAULT NULL,
  `fault_rollback_continue` tinyint(1) NOT NULL COMMENT '0 不继续，1继续；rollback出错后是否继续rollback剩余步骤',
  PRIMARY KEY (`flow_type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_work_flow_template`;
CREATE TABLE `gc_work_flow_template` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID，自增',
  `flow_type_code` char(64) NOT NULL COMMENT '任务流类型代码',
  `step_id` int(10) DEFAULT NULL  COMMENT '步骤ID',
  `exec_command` char(64) NOT NULL COMMENT 'flow command class类名 或 flowTypeCode',
  `step_type` char(16) NOT NULL COMMENT 'command、flow；flow时代表是工作流嵌套',
  `from_ids` char(64) DEFAULT NULL COMMENT '上一级ID,逗号隔开',
  `y_to_ids` char(64) DEFAULT NULL COMMENT '下一级ID,逗号隔开',
  `n_to_ids` char(64) DEFAULT NULL COMMENT '步骤为非必要条件且步骤执行失败时，下一步id',
  `necessary` tinyint(1) unsigned zerofill NOT NULL DEFAULT '0' COMMENT '是否是必要条件，0 | 1；步骤执行失败时，1则执行rollback，0则执行nToIds',
  `async` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0 同步，1异步；一般stepType为flow时都为异步',
  `rollback_async` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0 同步，1异步；',
  `rollback_skip` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0 不跳过，1跳过；是否跳过rollback代码逻辑',
  `rollback_fail_continue` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0 流程回滚失败后不回滚，1 流程回滚失败后回滚；',
  `from_relation` char(16) NOT NULL COMMENT '父记录的关系（from_ids为多条记录时有效），FROM_ALL_DONE、FROM_ONE_DONE,默认FROM_ONE_DONE',
  `repeat_type` char(16) default NULL COMMENT 'serial、parral',
  `visible` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否展示，0 | 1',
  `step_desc` varchar(128) DEFAULT NULL COMMENT '步骤描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_work_flow_command_params_template`;
CREATE TABLE `gc_work_flow_command_params_template` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID，自增',
  `exec_command` char(64) NOT NULL COMMENT 'command class类名',
  `param_type` char(10) NOT NULL COMMENT '参数类型，req 或 res',
  `field_name` char(32) NOT NULL COMMENT '字段名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_work_flow_command_value_template`;
CREATE TABLE `gc_work_flow_command_value_template` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID，自增',
  `flow_type_code` char(64) NOT NULL COMMENT '任务流类型code',
  `step_id` int(10) NOT NULL COMMENT '任务流模板stepID',
  `field_name` char(32) NOT NULL COMMENT '字段名称',
  `from_step_id` int(10) NOT NULL COMMENT '任务流模板stepID,0代表work_flow_instance paramsJson中获取',
  `from_param_type` char(10) NOT NULL COMMENT 'req、res',
  `from_field_name` char(32) NOT NULL COMMENT '根据这个字段获取',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_work_flow_task`;
CREATE TABLE `gc_work_flow_task` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID，自增',
  `task_id` char(64) NOT NULL COMMENT '总流程任务的任务id',
  `flow_type_code` char(64) NOT NULL COMMENT '任务流类型code',
  `state` char(32) DEFAULT NULL COMMENT 'waiting、executing、success、fail',
  `batch_size` int(10) DEFAULT 1,
  `user_id` varchar(64) DEFAULT NULL COMMENT '用户ID',
  `region_id` varchar(64) DEFAULT NULL COMMENT '所属区域id',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `parent_flow_id` bigint(20) DEFAULT NULL COMMENT '父工作流id',
  `parent_flow_step_id` int(10) DEFAULT NULL COMMENT '父工作流中的stepid',
  `need_feedback_log` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否需要feedback日志，0 | 1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_work_flow_batch`;
CREATE TABLE `gc_work_flow_batch` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID，自增',
  `ptask_id` char(64) NOT NULL COMMENT '父任务id',
  `flow_id` int(10) NOT NULL ,
  `state` char(32) DEFAULT NULL COMMENT 'waiting、executing、success、fail',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_work_flow_instance`;
CREATE TABLE `gc_work_flow_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID，自增',
  `flow_type_code` char(64) NOT NULL,
  `state` char(32) DEFAULT NULL COMMENT 'executing、success、failure',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `task_id` char(64) NOT NULL,
  `params_json` text DEFAULT NULL COMMENT '参数',
  `error_code` varchar(256) DEFAULT NULL,
  `parent_flow_id` bigint(20) DEFAULT NULL COMMENT '父工作流id',
  `parent_flow_step_id` int(10) DEFAULT NULL COMMENT '父工作流中的stepid',
  `batch_params` text DEFAULT NULL,
  `topest_flow_task_id` char(64) DEFAULT NULL COMMENT '最顶层任务id，根据这个字段判断是否在同一个任务流程',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_work_flow_instance_step`;
CREATE TABLE `gc_work_flow_instance_step` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID，自增',
  `flow_id` bigint(20) NOT NULL COMMENT '任务流实例ID',
  `template_step_id` int(10) NOT NULL COMMENT '模板表中的stepId',
  `task_id` char(64) DEFAULT NULL,
  `exec_command` char(64) NOT NULL COMMENT 'flow command class类名 或 flowTypeCode',
  `step_type` char(16) NOT NULL COMMENT 'command、flow；flow时代表是工作流嵌套',
  `req_json` text DEFAULT NULL COMMENT 'command请求参数json',
  `res_json` text DEFAULT NULL COMMENT '返回的结果json',
  `from_ids` char(64) DEFAULT NULL COMMENT '上一级ID,逗号隔开，跟step_id对应',
  `y_to_ids` char(64) DEFAULT NULL COMMENT '下一级ID,逗号隔开',
  `n_to_ids` char(64) DEFAULT NULL COMMENT '下一级ID,逗号隔开',
  `state` char(32) DEFAULT NULL COMMENT 'waiting、executing、success、failure、rollbacking、rollbacked',
  `necessary` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否是必要条件，0 | 1；步骤执行失败时，1则执行rollback，0则执行nToIds',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `rollback_start_time` datetime DEFAULT NULL COMMENT '回滚开始时间',
  `rollback_update_time` datetime DEFAULT NULL COMMENT '回滚修改时间',
  `rollback_task_id` char(64) DEFAULT NULL  COMMENT '回滚command的任务ID',
  `async` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0同步/1异步',
  `rollback_async` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0同步/1异步',
  `rollback_skip` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0 不跳过，1跳过；是否跳过rollback代码逻辑',
  `rollback_fail_continue` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0 流程回滚失败后不回滚，1 流程回滚失败后回滚；',
  `from_relation` char(16) NOT NULL COMMENT '父记录的关系（from_ids为多条记录时有效），FROM_ALL_DONE、FROM_ONE_DONE,默认FROM_ONE_DONE',
  `time_out` int(10) DEFAULT 180 COMMENT '超时时间，单位秒，command时有效',
  `repeat_type` char(16) default NULL COMMENT 'serial、parral',
  `repeat_index` int(10) default NULL,
  `visible` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否展示，0 | 1',
  `topest_flow_task_id` char(64) DEFAULT NULL COMMENT '最顶层任务id，根据这个字段判断是否在同一个任务流程',
  `step_desc` varchar(128) DEFAULT NULL COMMENT '步骤描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 获取任务流步骤优先级，因为mysql存储过程不支持递归，故用sub的形式调用。暂时不支持单步骤并行形式，只支持单步和分支。
delimiter ~
DROP FUNCTION IF EXISTS getPrioritySub~

CREATE FUNCTION getPrioritySub (inID INT, flowId INT) RETURNS VARCHAR(255) DETERMINISTIC
begin
  DECLARE gParentID VARCHAR(255) DEFAULT '';
  DECLARE gPriority VARCHAR(255) DEFAULT '';
  SET gPriority = inID;
  SELECT from_ids INTO gParentID FROM gc_work_flow_instance_step WHERE template_step_id = inID and flow_id=flowId;
  WHILE gParentID is not null DO  /*NULL为根*/
    SET gPriority = CONCAT(gParentID, '.', gPriority);
    SELECT from_ids INTO gParentID FROM gc_work_flow_instance_step WHERE template_step_id = gParentID and flow_id=flowId;
  END WHILE;
  RETURN gPriority;
end~

delimiter ;

delimiter ~
DROP FUNCTION IF EXISTS getPriority~

CREATE FUNCTION getPriority (inID INT, flowId INT) RETURNS VARCHAR(255) DETERMINISTIC
begin
  DECLARE gParentID VARCHAR(255) DEFAULT '';
  DECLARE gPriority VARCHAR(255) DEFAULT '';
  DECLARE commaLocate tinyint;
  DECLARE firstId VARCHAR(255) DEFAULT '';
  DECLARE secondId VARCHAR(255) DEFAULT '';
  DECLARE firstPriority VARCHAR(255) DEFAULT '';
  DECLARE secondPriority VARCHAR(255) DEFAULT '';
  DECLARE firstPriorityLevel tinyint;
  DECLARE secondPriorityLevel tinyint;
  DECLARE gNewParentID VARCHAR(255) DEFAULT '';
  SET commaLocate = 0;
  SET gPriority = inID;
  SELECT from_ids INTO gParentID FROM gc_work_flow_instance_step WHERE template_step_id = inID and flow_id=flowId;
  WHILE gParentID is not null DO  /*NULL为根*/
    SET commaLocate = locate(',', gParentID);
    IF commaLocate >0 THEN
      SET firstId = SUBSTRING(gParentID,1,locate(',', gParentID)-1);
      SET secondId = SUBSTRING(gParentID,locate(',', gParentID)+1);
	    SET firstPriority = getPrioritySub(firstId, flowId);
      SET secondPriority = getPrioritySub(secondId, flowId);
      SET firstPriorityLevel = LENGTH(firstPriority)- LENGTH(REPLACE(firstPriority,'.',''));
      SET secondPriorityLevel = LENGTH(secondPriority)- LENGTH(REPLACE(secondPriority,'.',''));
      IF firstPriorityLevel = secondPriorityLevel THEN
        SET gPriority = CONCAT(gParentID, '.', gPriority);
      ELSE
        IF firstPriorityLevel > secondPriorityLevel THEN
          SET gNewParentID = CONCAT(secondId, ".",firstId);
				ELSE
				  SET gNewParentID = CONCAT(firstId, ".", secondId);
				end IF;
        SET gPriority = CONCAT(gNewParentID, '.', gPriority);
      END IF;
      SELECT from_ids INTO gParentID FROM gc_work_flow_instance_step WHERE template_step_id = firstId and flow_id=flowId;
    ELSE
      SET gPriority = CONCAT(gParentID, '.', gPriority);
      SELECT from_ids INTO gParentID FROM gc_work_flow_instance_step WHERE template_step_id = gParentID and flow_id=flowId;
    END IF;
  END WHILE;
  RETURN gPriority;
end~

delimiter ;


-- --------------------------------------------------------------------------------------------------------
-- --------------------------------------------- network sql ----------------------------------------------
-- --------------------------------------------------------------------------------------------------------

-- ----------------------------
-- Table structure for gc_routers
-- ----------------------------
DROP TABLE IF EXISTS `gc_routers`;
CREATE TABLE `gc_routers` (
  `id` varchar(64) NOT NULL COMMENT '资源Uuid',
  `name` varchar(255) DEFAULT NULL COMMENT '名字',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `user_id` varchar(32) DEFAULT NULL COMMENT '所有者用户ID',
  `external_gateway_network_id` varchar(50) DEFAULT NULL COMMENT '外网网关',
  `status` varchar(16) DEFAULT NULL COMMENT '状态',
  `region_id` varchar(64) DEFAULT NULL COMMENT '地域Id',
  `provider` int(10) DEFAULT null,
  `provider_ref_id` varchar(40) DEFAULT null,
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_router_ports`;
CREATE TABLE `gc_router_ports` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `router_id` varchar(36) DEFAULT NULL,
  `port_id` varchar(36) DEFAULT NULL,
  `port_type` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for gc_subnets
-- ----------------------------
DROP TABLE IF EXISTS `gc_subnets`;
CREATE TABLE `gc_subnets` (
  `id` varchar(64) NOT NULL COMMENT '资源Uuid',
  `name` varchar(255) DEFAULT NULL COMMENT '名字',
  `cidr` varchar(64) NOT NULL COMMENT '路由ID',
  `network_id` varchar(64) DEFAULT NULL COMMENT '内网ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `user_id` varchar(32) DEFAULT NULL COMMENT '所有者用户ID',
  `provider` int(10) DEFAULT null,
  `provider_ref_id` varchar(40) DEFAULT null,
  `zone_id` varchar(36) DEFAULT NULL,
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for gc_security_groups
-- ----------------------------
DROP TABLE IF EXISTS `gc_security_groups`;
CREATE TABLE `gc_security_groups` (
  `id` varchar(64) NOT NULL COMMENT '安全组资源id',
  `name` varchar(255) DEFAULT NULL COMMENT '名字',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `user_id` varchar(32) DEFAULT NULL COMMENT '所有者用户ID',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `provider` int(10) DEFAULT null,
  `provider_ref_id` varchar(40) DEFAULT null,
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  `is_default` tinyint(1) DEFAULT NULL COMMENT '是否默认路由',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `gc_security_group_rules`
-- ----------------------------
DROP TABLE IF EXISTS `gc_security_group_rules`;
CREATE TABLE `gc_security_group_rules` (
  `id` varchar(64) NOT NULL COMMENT 'ID',
  `security_group_id` varchar(64) NOT NULL COMMENT '安全组ID',
  `protocol` varchar(64) DEFAULT NULL COMMENT '协议类型,取值：tcp ，udp ，icmp，all，为all类型时，PortRange范围-1/-1',
  `port_range` varchar(64) DEFAULT NULL COMMENT '端口范围,IP协议相关的端口号范围如1/80，IpProtocol不是all则不为空',
  `remote_ip_prefix` varchar(64) DEFAULT NULL COMMENT '目标网段',
  `remote_group_id` varchar(64) DEFAULT NULL COMMENT '目标安全组',
  `direction` varchar(32) DEFAULT NULL COMMENT '方向',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `ethertype` varchar(64) DEFAULT NULL COMMENT '以太网类型，IPV4，IPV6',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  `user_id` varchar(32) DEFAULT NULL COMMENT '所有者用户ID',
  `provider` int(10) DEFAULT null,
  `provider_ref_id` varchar(40) DEFAULT null,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for gc_floating_ips 弹性公网ip资源表
-- ----------------------------
DROP TABLE IF EXISTS `gc_floating_ips`;
CREATE TABLE `gc_floating_ips` (
  `id` varchar(64) NOT NULL COMMENT '弹性公网uuid',
  `floating_network_id` varchar(64) DEFAULT NULL COMMENT '外网网络id',
  `floating_port_id` varchar(64) DEFAULT NULL COMMENT '外网网卡id',
  `floating_ip_address` varchar(32) DEFAULT NULL COMMENT 'ip',
  `router_id` varchar(64) DEFAULT NULL COMMENT '路由id',
  `instance_id` varchar(64) DEFAULT NULL COMMENT '实例id',
  `fixed_port_id` varchar(64) DEFAULT NULL COMMENT '虚拟机网卡id',
  `instance_type` varchar(32) DEFAULT NULL COMMENT '实例类型',
  `status` varchar(16) DEFAULT NULL COMMENT '状态',
  `region_id` varchar(64) NOT NULL COMMENT '区域ID',
  `user_id` varchar(64) DEFAULT NULL COMMENT '拥有者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT  NULL COMMENT '更新时间',
  `provider` int(10) DEFAULT null,
  `provider_ref_id` varchar(40) DEFAULT null,
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  `bw_qos_policy_id` varchar(64) DEFAULT NULL COMMENT '带宽qos策略',
  `bandwidth` bigint(10) DEFAULT NULL COMMENT '带宽',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for gc_networks
-- ----------------------------
DROP TABLE IF EXISTS `gc_networks`;
CREATE TABLE `gc_networks` (
  `id` varchar(64) NOT NULL COMMENT '资源Uuid',
  `name` varchar(255) DEFAULT NULL COMMENT '名字',
  `region_id` varchar(64) NOT NULL COMMENT '区域ID',
  `status` varchar(16) DEFAULT NULL COMMENT '状态',
  `provider` int(10) DEFAULT null,
  `provider_ref_id` varchar(40) DEFAULT null,
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `type` tinyint(1) NOT NULL COMMENT '0、内部网络；1、外部网络；',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- --------------------------------------------------------------------------------------------------------
-- --------------------------------------------- log sql --------------------------------------------------
-- --------------------------------------------------------------------------------------------------------

-- ------------------------------------
-- 用户操作日志
-- ------------------------------------
DROP TABLE IF EXISTS `gc_log`;
CREATE TABLE `gc_log` (
	`id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '日志ID，自增' ,
	`user_id` varchar(50) DEFAULT NULL COMMENT '用户ID，关联用户表' ,
	`user_name` varchar(50) DEFAULT NULL ,
	`fun_name` varchar(50) DEFAULT NULL COMMENT '功能ID，关联功能表' ,
	`ip` varchar(20) DEFAULT NULL COMMENT '操作人IP' ,
	`object_id` char(50) DEFAULT NULL COMMENT '操作对象ID，比如虚拟机ID/用户ID等' ,
	`object_name` varchar(100) NULL DEFAULT NULL ,
	`result` tinyint(4) NOT NULL DEFAULT 0 COMMENT '操作结果，命令执行成功与否,0 未知；1 成功；2 失败；3 进行中' ,
	`start_time` datetime NULL DEFAULT NULL COMMENT '开始时间' ,
	`end_time` datetime NULL DEFAULT NULL COMMENT '结束时间' ,
	`task_id` char(50) DEFAULT NULL COMMENT '任务ID' ,
	`command` char(50) DEFAULT NULL COMMENT '消息名' ,
	`percent` int(11) NULL DEFAULT 0 COMMENT '任务进度，0-100' ,
	`final_result` varchar(64) DEFAULT NULL COMMENT '最终结果' ,
	`task_expect` varchar(64) DEFAULT NULL COMMENT '期望结果' ,
	`timeout` int(11) NULL DEFAULT NULL ,
	`error_code` varchar(256) DEFAULT NULL COMMENT '错误代码' ,
	`remark` varchar(128) DEFAULT NULL COMMENT '备注，如记录操作原因' ,
	`description` text COMMENT '描述，其他相关描述',
	PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- --------------------------------------------------------------------------------------------------------
-- --------------------------------------------- compute sql ----------------------------------------------
-- --------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `gc_compute_node_connect_logs`;
CREATE TABLE `gc_compute_node_connect_logs` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `hostname` varchar(50) DEFAULT NULL,
  `node_ip` varchar(20) DEFAULT NULL,
  `log_code` varchar(50) DEFAULT NULL,
  `connect_type` varchar(20) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `log_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_compute_nodes`;
CREATE TABLE `gc_compute_nodes` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `hostname` varchar(128) NOT NULL COMMENT '节点全名',
  `node_ip` varchar(20) DEFAULT NULL COMMENT '节点IP',
  `memory_total` int(11) DEFAULT NULL COMMENT '节点内存总量（单位MB）',
  `disk_total` double(11,5) DEFAULT NULL COMMENT '节点磁盘总量（单位GB）',
  `cpu_total` int(11) DEFAULT NULL COMMENT '节点CPU总量',
  `hypervisor_type` varchar(40) DEFAULT NULL COMMENT '虚拟化类型',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `energy_node` int(11) DEFAULT NULL COMMENT '是否节能节点\n0:否,1:是',
  `state` int(11) DEFAULT NULL COMMENT '状态\n0:没有注册,1已经注册',
  `ft_node` tinyint(1) DEFAULT NULL COMMENT '是否为容错节点,1为是，0为不是',
  `cloud_platform` varchar(50) DEFAULT NULL COMMENT '云平台',
  `type` varchar(128) DEFAULT NULL COMMENT '节点类型。',
  `physical_cpu` int(11) DEFAULT NULL,
  `zone_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='节点表';

DROP TABLE IF EXISTS `gc_instances`;
CREATE TABLE `gc_instances` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `alias` varchar(300) DEFAULT NULL COMMENT '云服务器别名',
  `user_id` varchar(128) DEFAULT NULL COMMENT '云服务器所有者名称',
  `image_id` varchar(36) DEFAULT NULL COMMENT '镜像ID',
  `core` int(11) DEFAULT NULL COMMENT 'CPU核数',
  `memory` int(11) DEFAULT NULL COMMENT '内存大小(MB)',
  `disk` int(11) DEFAULT NULL COMMENT '硬盘大小(GB)',
  `hostname` varchar(128) DEFAULT NULL COMMENT '节点名称',
  `state` varchar(32) DEFAULT NULL COMMENT '状态',
  `last_state` varchar(32) DEFAULT NULL COMMENT '上一次状态',
  `task_state` varchar(32) DEFAULT NULL COMMENT '任务状态',
  `architecture` varchar(20) DEFAULT NULL COMMENT '系统架构',
  `platform` varchar(20) DEFAULT NULL COMMENT '操作系统',
  `create_type` varchar(20) DEFAULT NULL COMMENT '创建方式',
  `create_source_id` varchar(64) DEFAULT NULL COMMENT '创建来源',
  `ip` varchar(128) DEFAULT NULL COMMENT 'IP',
  `floatingip` varchar(128) DEFAULT NULL COMMENT '浮动Ip',
  `storage_type` varchar(50) DEFAULT NULL COMMENT '存储类型',
  `instance_type` varchar(36) DEFAULT NULL COMMENT '实力类型',
  `launch_time` datetime DEFAULT NULL COMMENT '启动时间',
  `start_time` datetime DEFAULT NULL COMMENT '开机时间',
  `trashed` varchar(25) DEFAULT NULL COMMENT '回收站状态',
  `autostart` tinyint(1) DEFAULT NULL COMMENT '随物理机启动',
  `iso_id` varchar(36) DEFAULT NULL COMMENT '光盘镜像id',
  `use_department` varchar(255) DEFAULT NULL COMMENT '使用部门',
  `is_ha` tinyint(1) DEFAULT NULL COMMENT '是否支持故障恢复机制,1为支持，0为不支持',
  `is_ft` tinyint(1) DEFAULT NULL COMMENT '是否支持容错恢复机制,1为支持，0为不支持',
  `is_lock` tinyint(1) DEFAULT NULL COMMENT '是否锁定虚拟机,1为锁定，0为解锁',
  `pool_id` varchar(36) DEFAULT NULL COMMENT '存储池Id',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `app_name` varchar(50) DEFAULT NULL COMMENT '云服务器标识',
  `app_type` varchar(255) DEFAULT NULL COMMENT 'dict_data表里面appType的值',
  `app_id` varchar(36) DEFAULT NULL,
  `creator` varchar(36) DEFAULT NULL COMMENT '创建者，会随实例文件位置变化',
  `userdata` text,
  `restore_type` tinyint(1) DEFAULT '0' COMMENT '还原模式',
  `ie_cache` tinyint(1) DEFAULT '0' COMMENT '上网缓存',
  `auto_login` tinyint(1) DEFAULT '0' COMMENT '自动登录',
  `vm_disable_type` int(11) DEFAULT NULL COMMENT '故障类型',
  `image_pool_id` varchar(36) DEFAULT NULL COMMENT '镜像存储池',
  `image_storage_type` varchar(50) DEFAULT NULL COMMENT '镜像存储类型',
  `usb_redir` tinyint(1) DEFAULT NULL,
  `remote_port` varchar(10) DEFAULT NULL,
  `cpu_core` int(11) DEFAULT NULL COMMENT 'cpu虚拟核数',
  `cpu_thread` int(11) DEFAULT NULL COMMENT 'cpu线程数',
  `cpu_socket` int(11) DEFAULT NULL COMMENT 'cpu物理核数数',
  `usb_type` tinyint(1) DEFAULT '3' COMMENT 'usb属性',
  `in_task` tinyint(1) DEFAULT NULL,
  `step_state` varchar(32) DEFAULT NULL,
  `zone_id` varchar(36) DEFAULT NULL,
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='虚拟机表';


DROP TABLE IF EXISTS `gc_instance_types`;
CREATE TABLE `gc_instance_types` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `created_time` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `memory_mb` int(11) NOT NULL,
  `vcpus` int(11) NOT NULL,
  `deleted` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_instance_types0name0deleted` (`name`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='实例类型';

-- 添加可用区和实例类型的关联表
DROP TABLE IF EXISTS `gc_zone_instance_types`;
CREATE TABLE `gc_zone_instance_types` (
	`id` int(16) AUTO_INCREMENT NOT NULL,
	`zone_id` VARCHAR(36) NOT NULL,
	`instance_type_id` VARCHAR(36) NOT NULL,
	PRIMARY KEY(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------------------------------------------------------
-- --------------------------------------------- neutron network sql --------------------------------------
-- --------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `gc_ipallocations`;
CREATE TABLE `gc_ipallocations` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `port_id` varchar(36) DEFAULT NULL,
  `ip_address` varchar(64) NOT NULL,
  `subnet_id` varchar(36) NOT NULL,
  `network_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_ports`;
CREATE TABLE `gc_ports` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `network_id` varchar(36) NOT NULL,
  `mac_address` varchar(32) NOT NULL,
  `status` varchar(16) NOT NULL,
  `device_id` varchar(255) DEFAULT NULL,
  `device_owner` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `suf_id` varchar(20) DEFAULT NULL,
  `br_name` varchar(20) DEFAULT NULL,
  `aft_name` varchar(20) DEFAULT NULL,
  `pre_name` varchar(20) DEFAULT NULL,
  `ovs_bridge_id` varchar(200) DEFAULT NULL,
  `no_arp_limit` tinyint(1) DEFAULT NULL,
  `provider` int(10) DEFAULT null,
  `provider_ref_id` varchar(40) DEFAULT null,
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_qos_bandwidth_limit_rules`;
CREATE TABLE `gc_qos_bandwidth_limit_rules` (
  `id` varchar(36) NOT NULL,
  `qos_policy_id` varchar(36) NOT NULL,
  `max_kbps` int(11) DEFAULT NULL,
  `max_burst_kbps` int(11) DEFAULT NULL,
  `direction` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_qos_policies`;
CREATE TABLE `gc_qos_policies` (
  `id` varchar(36) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_qos_port_policy_bindings`;
CREATE TABLE `gc_qos_port_policy_bindings` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `policy_id` varchar(36) DEFAULT NULL,
  `port_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_security_group_port_bindings`;
CREATE TABLE `gc_security_group_port_bindings` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `port_id` varchar(36) DEFAULT NULL,
  `security_group_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------------------------------------------------------
-- --------------------------------------------- slb ------------------------------------------
-- --------------------------------------------------------------------------------------------------------

-- ----------------------------
-- Table structure for gc_slb
-- ----------------------------
DROP TABLE IF EXISTS `gc_slb`;
CREATE TABLE `gc_slb` (
  `id` varchar(64) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `region_id` varchar(64) NOT NULL,
  `vip_subnet_id` varchar(64) NOT NULL,
  `status` varchar(255) NOT NULL, 
  `create_time` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `provider` int(10) DEFAULT null,
  `provider_ref_id` varchar(40) DEFAULT null,
  `user_id` varchar(64) DEFAULT NULL,
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  `vip_port_id` varchar(36) null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for gc_slb_listener
-- ----------------------------
DROP TABLE IF EXISTS `gc_slb_listener`;
CREATE TABLE `gc_slb_listener`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `listener_port` int(4) NOT NULL COMMENT '监听端口',
  `listener_protocol` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '监听器协议',
  `loadbalancer_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '负载均衡实例id',
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `vserver_group_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '后端服务器组ID',
  `vserver_group_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后端服务器组名称',
  `server_certificate_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务器证书ID',
  `provider` tinyint(1) NOT NULL COMMENT '多实现类型',
  `provider_ref_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '多实现对应ID',
  `updated_at` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for gc_slb_scheduler
-- ----------------------------
DROP TABLE IF EXISTS `gc_slb_scheduler`;
CREATE TABLE `gc_slb_scheduler`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `vserver_group_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '后端服务器组ID',
  `listener_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联监听器ID',
  `protocol` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '监听器协议',
  `scheduler` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调度策略名称',
  `provider` tinyint(1) NOT NULL COMMENT '多实现类型',
  `provider_ref_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '多实现对应ID',
  `updated_at` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;


DROP TABLE IF EXISTS `gc_slb_vservergroup`;
CREATE TABLE `gc_slb_vservergroup` (
  `id` varchar(64) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  `load_balancer_id` varchar(64) DEFAULT NULL,
  `protocol` varchar(64) DEFAULT NULL,
  `provider` tinyint(1) DEFAULT NULL,
  `provider_ref_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- --------------------------------------------------------------------------------------------------------
-- --------------------------------------------- cinder disk sql ------------------------------------------
-- --------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `gc_snapshots`;
CREATE TABLE `gc_snapshots` (
  `id` varchar(36) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `volume_id` varchar(36) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `volume_size` int(11) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `display_description` varchar(255) DEFAULT NULL,
  `storage_type` varchar(20) NOT NULL,
  `pool_name` varchar(100) NOT NULL,
  `provider` int(10) DEFAULT null,
  `provider_ref_id` varchar(40) DEFAULT null,
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_volume_attachments`;
CREATE TABLE `gc_volume_attachments` (
  `id` varchar(36) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `volume_id` varchar(36) NOT NULL,
  `attached_host` varchar(255) DEFAULT NULL,
  `instance_uuid` varchar(36) DEFAULT NULL,
  `mountpoint` varchar(255) DEFAULT NULL,
  `attach_mode` varchar(36) DEFAULT NULL,
  `attach_status` varchar(255) DEFAULT NULL,
  `hostname` varchar(50) DEFAULT NULL,
  `provider` int(10) DEFAULT null,
  `provider_ref_id` varchar(40) DEFAULT null,
  PRIMARY KEY (`id`),
  KEY `idx_volume_attachment_01` (`volume_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_volume_types`;
CREATE TABLE `gc_volume_types` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `storage_type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_volumes`;
CREATE TABLE `gc_volumes` (
  `id` varchar(36) NOT NULL,
  `status` varchar(32) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `disk_type` varchar(20) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `snapshot_id` varchar(36) DEFAULT NULL,
  `category` varchar(36) DEFAULT NULL,
  `size` int(10) DEFAULT NULL,
  `bootable` tinyint(1) DEFAULT NULL,
  `image_ref` varchar(36) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  `storage_type` varchar(20) NOT NULL,
  `pool_id` varchar(36) DEFAULT NULL,
  `pool_name` varchar(100) NOT NULL,
  `provider` int(10) DEFAULT null,
  `provider_ref_id` varchar(40) DEFAULT null,
  `zone_id` varchar(36) DEFAULT NULL,
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `gc_storage_pools` (
  `id` varchar(36) NOT NULL,
  `display_name` varchar(255) NOT NULL,
  `storage_type` varchar(20) NOT NULL,
  `pool_name` varchar(200) NOT NULL,
  `provider` int(10) DEFAULT NULL,
  `provider_ref_id` varchar(40) DEFAULT NULL,
  `hostname` varchar(36) DEFAULT NULL,
  `driver` varchar(36) DEFAULT NULL,
  `connect_protocol` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UN_01` (`storage_type`,`pool_name`,`provider`,`hostname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_disk_categories`;
CREATE TABLE `gc_disk_categories` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  `min_size` int(10) DEFAULT NULL,
  `max_size` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `gc_disk_category_pools` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disk_category_id` varchar(36) DEFAULT NULL,
  `zone_id` varchar(36) DEFAULT NULL,
  `storage_pool_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `gc_storage_pool_zones` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `storage_pool_id` varchar(36) DEFAULT NULL,
  `zone_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- --------------------------------------------------------------------------------------------------------
-- --------------------------------------------- glance image sql -----------------------------------------
-- --------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `gc_image_properties`;
CREATE TABLE `gc_image_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image_id` varchar(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_images`;
CREATE TABLE `gc_images` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `size` bigint(20) DEFAULT NULL,
  `status` varchar(30) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `owner` varchar(255) DEFAULT NULL,
  `min_disk` int(11) NOT NULL,
  `provider` int(10) DEFAULT null,
  `provider_ref_id` varchar(40) DEFAULT null,
  `owner_type` varchar(50) DEFAULT null,
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  `disable` tinyint(1) NOT NULL COMMENT '是否禁用，1禁用0可用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------------------------------------------------------
-- --------------------------------------------- gcloud ovs sql ---------------------------------------------
-- --------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS `gc_ovs_bridges`;
CREATE TABLE `gc_ovs_bridges` (
  `id` varchar(36) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `bridge` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `hostname` varchar(200) DEFAULT NULL,
  `provider` int(10) DEFAULT null,
  `provider_ref_id` varchar(40) DEFAULT null,
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `gc_ovs_bridges_usage`;
CREATE TABLE `gc_ovs_bridges_usage` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `bridge_id` varchar(36) DEFAULT NULL,
  `ref_type` varchar(20) DEFAULT NULL,
  `ref_id` varchar(36) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------------------------------------------------------
-- --------------------------------------------- gcloud zone sql ---------------------------------------------
-- --------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS `gc_zones`;
CREATE TABLE `gc_zones` (
  `id` varchar(36) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- --------------------------------------------------------------------------------------------------------
-- --------------------------------------------- workflow sql ---------------------------------------------
-- --------------------------------------------------------------------------------------------------------

-- 创建虚拟机

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'CreateInstanceWorkflow', 1, 'createInstanceFlowInitCommand', 'command', null, '2', null, 1, 0, 0, 0, 1, 'FROM_ONE_DONE', null, 1),
(null, 'CreateInstanceWorkflow', 2, 'checkCreateVmNodeEnvFlowCommand', 'command', '1', '3', null, 1, 1, 1, 0, 1, 'FROM_ONE_DONE', null, 1),
(null, 'CreateInstanceWorkflow', 3, 'checkAndDistributeImageFlowCommand', 'command', '2', '4', null, 1, 1, 0, 0, 1, 'FROM_ONE_DONE', null, 1),
(null, 'CreateInstanceWorkflow', 4, 'createSystemDiskFlowCommand', 'command', '3', '5', null, 1, 1, 1, 0, 1, 'FROM_ONE_DONE', null, 1),
(null, 'CreateInstanceWorkflow', 5, 'connectDiskFlowCommand', 'command', '4', '6', null, 1, 0, 1, 0, 1, 'FROM_ONE_DONE', null, 1),
(null, 'CreateInstanceWorkflow', 6, 'attachSystemDiskFlowCommand', 'command', '5', '7', null, 1, 0, 0, 0, 1, 'FROM_ONE_DONE', null, 1),
(null, 'CreateInstanceWorkflow', 7, 'buildVmConfigFlowCommand', 'command', '6', '9', null, 1, 1, 1, 0, 1, 'FROM_ONE_DONE', null, 1),
(null, 'CreateInstanceWorkflow', 8, 'createDomainFlowCommand', 'command', '10', '12', null, 1, 1, 1, 0, 1, 'FROM_ONE_DONE', null, 1),
(null, 'CreateInstanceWorkflow', 9, 'createAttachDataVolumeWorkflow', 'task_flow', '7', '10', null, 1, 1, 0, 0, 1, 'FROM_ONE_DONE', 'serial', 1),
(null, 'CreateInstanceWorkflow', 10, 'createAttachNetcardWorkflow', 'task_flow', '9', '8', null, 1, 1, 0, 0, 1, 'FROM_ONE_DONE', null, 1),
(null, 'CreateInstanceWorkflow', 11, 'createInstanceFlowDoneCommand', 'command', '13', null, null, 1, 0, 0, 0, 1, 'FROM_ONE_DONE', null, 0),
(null, 'CreateInstanceWorkflow', '12', 'modifyInstancePasswordFlowCommand', 'command', '8', '13', NULL, '1', '1', '0', '1', '0', 'FROM_ONE_DONE', NULL, '1'),
(null, 'CreateInstanceWorkflow', '13', 'renameInstanceFlowCommand', 'command', '12', '11', NULL, '1', '1', '0', '1', '0', 'FROM_ONE_DONE', NULL, '1');

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'CreateAttachDataVolumeWorkflow', 1, 'createDataDiskFlowCommand', 'command', null, '2', null, 1, 1, 0, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'CreateAttachDataVolumeWorkflow', 2, 'attachDataDiskWorkflow', 'task_flow', '1', null, null, 1, 1, 0, 0, 0, 'FROM_ONE_DONE', null, 1);

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'CreateAttachNetcardWorkflow', 1, 'createPortFlowCommand', 'command', null, '2', null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'CreateAttachNetcardWorkflow', 2, 'attachPortWorkflow', 'task_flow', '1', null, null, 1, 1, 0, 0, 0, 'FROM_ONE_DONE', null, 1);


INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'CreateInstanceWorkflow', 1, '', 0, '', ''),
(null, 'CreateInstanceWorkflow', 2, 'createUser', 1, 'res', 'createUser'),
(null, 'CreateInstanceWorkflow', 2, 'createHost', 1, 'res', 'createHost'),
(null, 'CreateInstanceWorkflow', 2, 'instanceId', 1, 'res', 'instanceId'),
(null, 'CreateInstanceWorkflow', 3, 'imageId', 0, '', 'imageId'),
(null, 'CreateInstanceWorkflow', 3, 'zoneId', 0, '', 'zoneId'),
(null, 'CreateInstanceWorkflow', 3, 'systemDiskCategory', 0, '', 'systemDiskCategory'),
(null, 'CreateInstanceWorkflow', 3, 'createHost', 1, 'res', 'createHost'),
(null, 'CreateInstanceWorkflow', 4, 'createHost', 1, 'res', 'createHost'),
(null, 'CreateInstanceWorkflow', 4, 'dataDisk', 1, 'res', 'systemDisk'),
(null, 'CreateInstanceWorkflow', 4, 'createUser', 1, 'res', 'createUser'),
(null, 'CreateInstanceWorkflow', 5, 'volumeId', 4, 'res', 'volumeId'),
(null, 'CreateInstanceWorkflow', 5, 'createHost', 1, 'res', 'createHost'),
(null, 'CreateInstanceWorkflow', 5, 'diskCategory', 0, '', 'systemDiskCategory'),
(null, 'CreateInstanceWorkflow', 5, 'storageType', 1, 'res', 'storageType'),
(null, 'CreateInstanceWorkflow', 6, 'createHost', 1, 'res', 'createHost'),
(null, 'CreateInstanceWorkflow', 6, 'volumeId', 4, 'res', 'volumeId'),
(null, 'CreateInstanceWorkflow', 6, 'size', 4, 'res', 'size'),
(null, 'CreateInstanceWorkflow', 6, 'category', 4, 'res', 'category'),
(null, 'CreateInstanceWorkflow', 6, 'instanceId', 1, 'res', 'instanceId'),
(null, 'CreateInstanceWorkflow', 6, 'storageType', 1, 'res', 'storageType'),
(null, 'CreateInstanceWorkflow', 7, 'createHost', 1, 'res', 'createHost'),
(null, 'CreateInstanceWorkflow', 7, 'core', 1, 'res', 'cpu'),
(null, 'CreateInstanceWorkflow', 7, 'cpuSocket', 1, 'res', 'cpu'),
(null, 'CreateInstanceWorkflow', 7, 'memory', 1, 'res', 'memory'),
(null, 'CreateInstanceWorkflow', 7, 'disk', 1, 'res', 'systemDiskSize'),
(null, 'CreateInstanceWorkflow', 7, 'storageType', 1, 'res', 'storageType'),
(null, 'CreateInstanceWorkflow', 7, 'createUser', 1, 'res', 'createUser'),
(null, 'CreateInstanceWorkflow', 7, 'instanceId', 1, 'res', 'instanceId'),
(null, 'CreateInstanceWorkflow', 7, 'imageInfo', 1, 'res', 'imageInfo'),
(null, 'CreateInstanceWorkflow', 7, 'vmSysDisk', 6, 'res', 'volumeDetail'),
(null, 'CreateInstanceWorkflow', 7, 'zxAuth', 0, '', 'zxAuth'),
(null, 'CreateInstanceWorkflow', 8, 'createHost', 1, 'res', 'createHost'),
(null, 'CreateInstanceWorkflow', 8, 'instanceId', 1, 'res', 'instanceId'),
(null, 'CreateInstanceWorkflow', 8, 'createUser', 1, 'res', 'createUser'),
(null, 'CreateInstanceWorkflow', 9, 'instanceId', 1, 'res', 'instanceId'),
(null, 'CreateInstanceWorkflow', 9, 'repeatParams', 1, 'res', 'dataDisk'),
(null, 'CreateInstanceWorkflow', 9, 'createUser', 1, 'res', 'createUser'),
(null, 'CreateInstanceWorkflow', 9, 'instanceId', 1, 'res', 'instanceId'),
(null, 'CreateInstanceWorkflow', 9, 'subnetId', 0, '', 'subnetId'),
(null, 'CreateInstanceWorkflow', 10, 'createUser', 1, 'res', 'createUser'),
(null, 'CreateInstanceWorkflow', 10, 'ipAddress', 0, '', 'ipAddress'),
(null, 'CreateInstanceWorkflow', 11, 'instanceId', 1, 'res', 'instanceId'),
(null,'CreateInstanceWorkflow',12,'instanceId',1,'res','instanceId'),
(null,'CreateInstanceWorkflow',12,'password',0,'','password'),
(null,'CreateInstanceWorkflow',12,'createHost',1,'res','createHost'),
(null,'CreateInstanceWorkflow',13,'instanceId',1,'res','instanceId'),
(null,'CreateInstanceWorkflow',13,'hostName',0,'','hostName'),
(null,'CreateInstanceWorkflow',13,'createHost',1,'res','createHost');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'CreateAttachDataVolumeWorkflow', 1, 'dataDisk', 0, '', 'repeatParams'),
(null, 'CreateAttachDataVolumeWorkflow', 1, 'createUser', 0, '', 'createUser'),
(null, 'CreateAttachDataVolumeWorkflow', 2, 'volumeId', 1, 'res', 'volumeId'),
(null, 'CreateAttachDataVolumeWorkflow', 2, 'instanceId', 0, '', 'instanceId'),
(null, 'CreateAttachDataVolumeWorkflow', 2, 'createUser', 0, '', 'createUser');


INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'CreateAttachNetcardWorkflow', 1, '', 0, '', ''),
(null, 'CreateAttachNetcardWorkflow', 2, 'instanceId', 0, '', 'instanceId'),
(null, 'CreateAttachNetcardWorkflow', 2, 'ovsBridgeId', 0, '', 'ovsBridgeId'),
(null, 'CreateAttachNetcardWorkflow', 2, 'networkInterfaceId', 1, 'res', 'portId');


-- 挂载网卡
INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'AttachPortWorkflow', 1, 'attachPortInitFlowCommand', 'command', null, '2', null, 1, 0, 0, 0, 1, 'FROM_ONE_DONE', null, 1),
(null, 'AttachPortWorkflow', 2, 'configNetEnvFlowCommand', 'command', '1', '3', null, 1, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'AttachPortWorkflow', 3, 'configNetFileFlowCommand', 'command', '2', '4', null, 1, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'AttachPortWorkflow', 4, 'attachPortFlowCommand', 'command', '3', '5', null, 1, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'AttachPortWorkflow', 5, 'attachPortDoneFlowCommand', 'command', '4', null, null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 1);

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'AttachPortWorkflow', 2, 'networkDetail', 1, 'res', 'networkDetail'),
(null, 'AttachPortWorkflow', 2, 'instanceId', 1, 'res', 'instanceId'),
(null, 'AttachPortWorkflow', 3, 'networkDetail', 1, 'res', 'networkDetail'),
(null, 'AttachPortWorkflow', 3, 'instanceId', 1, 'res', 'instanceId'),
(null, 'AttachPortWorkflow', 4, 'networkDetail', 1, 'res', 'networkDetail'),
(null, 'AttachPortWorkflow', 4, 'instanceId', 1, 'res', 'instanceId'),
(null, 'AttachPortWorkflow', 5, 'inTask', 0, '', 'inTask'),
(null, 'AttachPortWorkflow', 5, 'networkDetail', 1, 'res', 'networkDetail'),
(null, 'AttachPortWorkflow', 5, 'instanceId', 1, 'res', 'instanceId'),
(null, 'AttachPortWorkflow', 1, '', 0, '', '');

-- 卸载网卡
INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'DetachPortWorkflow', 1, 'detachPortInitFlowCommand', 'command', null, '2', null, 1, 0, 0, 0, 1, 'FROM_ONE_DONE', null, 1),
(null, 'DetachPortWorkflow', 2, 'detachPortFlowCommand', 'command', '1', '3', null, 1, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'DetachPortWorkflow', 3, 'cleanNetConfigFileFlowCommand', 'command', '2', '4', null, 1, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'DetachPortWorkflow', 4, 'cleanNetEnvConfigFlowCommand', 'command', '3', '5', null, 1, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'DetachPortWorkflow', 5, 'detachPortDoneFlowCommand', 'command', '4', null, null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 1);

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'DetachPortWorkflow', 1, '', 0, '', ''),
(null, 'DetachPortWorkflow', 2, 'networkDetail', 1, 'res', 'networkDetail'),
(null, 'DetachPortWorkflow', 2, 'instanceId', 1, 'res', 'instanceId'),
(null, 'DetachPortWorkflow', 3, 'networkDetail', 1, 'res', 'networkDetail'),
(null, 'DetachPortWorkflow', 3, 'instanceId', 1, 'res', 'instanceId'),
(null, 'DetachPortWorkflow', 4, 'networkDetail', 1, 'res', 'networkDetail'),
(null, 'DetachPortWorkflow', 4, 'instanceId', 1, 'res', 'instanceId'),
(null, 'DetachPortWorkflow', 5, 'inTask', 0, '', 'inTask'),
(null, 'DetachPortWorkflow', 5, 'networkDetail', 1, 'res', 'networkDetail'),
(null, 'DetachPortWorkflow', 5, 'instanceId', 1, 'res', 'instanceId');

-- 挂载磁盘

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'AttachDataDiskWorkflow', 1, 'attachDataDiskInitFlowCommand', 'command', null, '2', null, 1, 0, 0, 0, 1, 'FROM_ONE_DONE', null, 1),
(null, 'AttachDataDiskWorkflow', 2, 'configDataDiskFileFlowCommand', 'command', '1', '3', null, 1, 1, 0, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'AttachDataDiskWorkflow', 3, 'attachDataDiskFlowCommand', 'command', '2', '4', null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'AttachDataDiskWorkflow', 4, 'attachDataDiskDoneFlowCommand', 'command', '3', null, null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 1);


INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'AttachDataDiskWorkflow', 1, '', 0, '', ''),
(null, 'AttachDataDiskWorkflow', 2, 'instanceId', 0, '', 'instanceId'),
(null, 'AttachDataDiskWorkflow', 2, 'volumeDetail', 1, 'res', 'volumeDetail'),
(null, 'AttachDataDiskWorkflow', 2, 'vmHostName', 1, 'res', 'vmHostName'),
(null, 'AttachDataDiskWorkflow', 2, 'volumeDetail', 1, 'res', 'volumeDetail'),
(null, 'AttachDataDiskWorkflow', 3, 'instanceId', 1, 'res', 'instanceId'),
(null, 'AttachDataDiskWorkflow', 3, 'volumeDetail', 1, 'res', 'volumeDetail'),
(null, 'AttachDataDiskWorkflow', 3, 'vmHostName', 1, 'res', 'vmHostName'),
(null, 'AttachDataDiskWorkflow', 4, 'inTask', 0, '', 'inTask'),
(null, 'AttachDataDiskWorkflow', 4, 'instanceId', 0, '', 'instanceId');

-- 卸载磁盘

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'DetachDataDiskWorkflow', 1, 'detachDataDiskInitFlowCommand', 'command', null, '3', null, 1, 0, 0, 0, 1, 'FROM_ONE_DONE', null, 1),
(null, 'DetachDataDiskWorkflow', 2, 'detachDataDiskFlowCommand', 'command', '3', '4', null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'DetachDataDiskWorkflow', 3, 'cleanDataDiskConfigFileFlowCommand', 'command', '1', '2', null, 1, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'DetachDataDiskWorkflow', 4, 'detachDataDiskDoneFlowCommand', 'command', '2', null, null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 1);

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'DetachDataDiskWorkflow', 1, '', 0, '', ''),
(null, 'DetachDataDiskWorkflow', 2, 'instanceId', 0, '', 'instanceId'),
(null, 'DetachDataDiskWorkflow', 2, 'volumeId', 1, 'res', 'volumeId'),
(null, 'DetachDataDiskWorkflow', 2, 'vmHostName', 1, 'res', 'vmHostName'),
(null, 'DetachDataDiskWorkflow', 2, 'attachmentId', 1, 'res', 'attachmentId'),
(null, 'DetachDataDiskWorkflow', 3, 'instanceId', 1, 'res', 'instanceId'),
(null, 'DetachDataDiskWorkflow', 3, 'volumeDetail', 1, 'res', 'volumeDetail'),
(null, 'DetachDataDiskWorkflow', 3, 'vmHostName', 1, 'res', 'vmHostName'),
(null, 'DetachDataDiskWorkflow', 4, 'inTask', 0, '', 'inTask'),
(null, 'DetachDataDiskWorkflow', 4, 'instanceId', 0, '', 'instanceId');


-- 虚拟机打包

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'BundleInstanceWorkflow', 1, 'bundleInstanceInitFlowCommand', 'command', null, '3', null, 1, 0, 0, 0, 1, 'FROM_ONE_DONE', null, 0),
(null, 'BundleInstanceWorkflow', 3, 'convertInstanceFlowCommand', 'command', '1', '4', null, 1, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'BundleInstanceWorkflow', 4, 'importImageFlowCommand', 'command', '3', '8', null, 1, 1, 0, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'BundleInstanceWorkflow', 8, 'deleteBundleFlowCommand', 'command', '4', '7', null, 1, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'BundleInstanceWorkflow', 7, 'bundleInstanceDoneFlowCommand', 'command', '8', null, null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0);

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'BundleInstanceWorkflow', 1, '', 0, '', ''),
(null, 'BundleInstanceWorkflow', 3, 'instanceId', 0, 'res', 'instanceId'),
(null, 'BundleInstanceWorkflow', 3, 'targetFormat', 1, 'res', 'targetFormat'),
(null, 'BundleInstanceWorkflow', 3, 'nodeIp', 1, 'res', 'nodeIp'),
(null, 'BundleInstanceWorkflow', 3, 'volumeDetail', 1, 'res', 'volumeDetail'),
(null, 'BundleInstanceWorkflow', 4, 'imageId', 1, 'res', 'imageId'),
(null, 'BundleInstanceWorkflow', 4, 'currentUser', 0, '', 'currentUser'),
(null, 'BundleInstanceWorkflow', 4, 'imageName', 0, '', 'imageName'),
(null, 'BundleInstanceWorkflow', 4, 'filePath', 1, 'res', 'imageFilePath'),
(null, 'BundleInstanceWorkflow', 4, 'osType', 1, 'res', 'osType'),
(null, 'BundleInstanceWorkflow', 4, 'architecture', 1, 'res', 'architecture'),
(null, 'BundleInstanceWorkflow', 4, 'description', 1, 'res', 'description'),
(null, 'BundleInstanceWorkflow', 7, 'inTask', 0, '', 'inTask'),
(null, 'BundleInstanceWorkflow', 7, 'instanceId', 0, '', 'instanceId'),
(null, 'BundleInstanceWorkflow', 8, 'instanceId', 0, 'res', 'instanceId'),
(null, 'BundleInstanceWorkflow', 8, 'nodeIp', 1, 'res', 'nodeIp');


-- 虚拟机修改配置

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'ModifyInstanceSpecWorkflow', 1, 'modifyInstanceSpecInitFlowCommand', 'command', null, '3', null, 1, 0, 0, 0, 1, 'FROM_ONE_DONE', null, 1),
(null, 'ModifyInstanceSpecWorkflow', 3, 'configInstanceResourceFlowCommand', 'command', '1', '5', null, 1, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'ModifyInstanceSpecWorkflow', 5, 'modifyInstanceSpecDoneFlowCommand', 'command', '5', null, null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 1);

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'ModifyInstanceSpecWorkflow', 1, '', 0, '', ''),
(null, 'ModifyInstanceSpecWorkflow', 3, 'instanceId', 0, '', 'instanceId'),
(null, 'ModifyInstanceSpecWorkflow', 3, 'cpu', 1, 'res', 'cpu'),
(null, 'ModifyInstanceSpecWorkflow', 3, 'memory', 1, 'res', 'memory'),
(null, 'ModifyInstanceSpecWorkflow', 3, 'hostName', 1, 'res', 'hostName'),
(null, 'ModifyInstanceSpecWorkflow', 3, 'orgCpu', 1, 'res', 'orgCpu'),
(null, 'ModifyInstanceSpecWorkflow', 3, 'orgMemory', 1, 'res', 'orgMemory'),
(null, 'ModifyInstanceSpecWorkflow', 5, 'instanceId', 0, '', 'instanceId'),
(null, 'ModifyInstanceSpecWorkflow', 5, 'instanceType', 0, '', 'instanceType'),
(null, 'ModifyInstanceSpecWorkflow', 5, 'cpu', 1, 'res', 'cpu'),
(null, 'ModifyInstanceSpecWorkflow', 5, 'memory', 1, 'res', 'memory');


-- 删除虚拟机

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'DeleteInstanceWorkflow', 1, 'deleteInstanceInitFlowCommand', 'command', null, '2', null, 1, 0, 0, 0, 1, 'FROM_ONE_DONE', null, 1),
(null, 'DeleteInstanceWorkflow', 2, 'destroyInstanceIfExistFlowCommand', 'command', '1', '3', null, 1, 1, 0, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'DeleteInstanceWorkflow', 3, 'forceDetachAndDeleteDiskWorkflow', 'task_flow', '2', '4', null, 1, 1, 0, 0, 0, 'FROM_ONE_DONE', 'serial', 1),
(null, 'DeleteInstanceWorkflow', 4, 'forceDetachAndDeleteNetcardWorkflow', 'task_flow', '3', '5', null, 1, 1, 0, 0, 0, 'FROM_ONE_DONE', 'serial', 1),
(null, 'DeleteInstanceWorkflow', 5, 'cleanInstanceFileFlowCommand', 'command', '4', '6', null, 1, 1, 0, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'DeleteInstanceWorkflow', 6, 'cleanInstanceInfoFlowCommand', 'command', '5', '7', null, 1, 1, 0, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'DeleteInstanceWorkflow', 7, 'deleteInstanceDoneFlowCommand', 'command', '6', null, null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 1);

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'ForceDetachAndDeleteNetcardWorkflow', 1, 'forceDetachAndDeleteNetcardInitFlowCommand', 'command', null, '2', null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'ForceDetachAndDeleteNetcardWorkflow', 2, 'forceDetachPortFlowCommand', 'command', '1', '3', null, 1, 1, 0, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'ForceDetachAndDeleteNetcardWorkflow', 3, 'forceCleanNetConfigFileFlowCommand', 'command', '2', '4', null, 1, 1, 0, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'ForceDetachAndDeleteNetcardWorkflow', 4, 'forceCleanNetEnvConfigFlowCommand', 'command', '3', '5', null, 1, 1, 0, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'ForceDetachAndDeleteNetcardWorkflow', 5, 'forceDetachNetcardDoneFlowCommand', 'command', '4', '6', null, 1, 0, 0, 1, 0, 'FROM_ONE_DONE', null, 1),
(null, 'ForceDetachAndDeleteNetcardWorkflow', 6, 'deleteNetcardFlowCommand', 'command', '5', null, null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 1);

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'ForceDetachAndDeleteDiskWorkflow', 1, 'forceDetachAndDeleteDiskInitFlowCommand', 'command', null, '2', null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 1),
(null, 'ForceDetachAndDeleteDiskWorkflow', 2, 'forceCleanDiskConfigFileFlowCommand', 'command', '1', '3', null, 1, 1, 1, 1, 0, 'FROM_ONE_DONE', null, 1),
(null, 'ForceDetachAndDeleteDiskWorkflow', 3, 'detachDataDiskFlowCommand', 'command', '2', '4', null, 1, 0, 0, 1, 0, 'FROM_ONE_DONE', null, 1),
(null, 'ForceDetachAndDeleteDiskWorkflow', 4, 'deleteDiskWorkflow', 'task_flow', '3', null, null, 1, 1, 1, 1, 0, 'FROM_ONE_DONE', null, 1);

-- 删除磁盘work flow
INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'DeleteDiskWorkflow', 1, 'deleteDiskInitFlowCommand', 'command', null, '2', null, 1, 0, 0, 1, 0, 'FROM_ONE_DONE', null, 1),
(null, 'DeleteDiskWorkflow', 2, 'deleteSnapshotFlowCommand', 'command', '1', '3', null, 1, 1, 0, 1, 0, 'FROM_ONE_DONE', 'serial', 1),
(null, 'DeleteDiskWorkflow', 3, 'deleteDiskFlowCommand', 'command', '2', null, null, 1, 1, 0, 1, 0, 'FROM_ONE_DONE', null, 1);


INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'DeleteDiskWorkflow', 1, '', 0, '', ''),
(null, 'DeleteDiskWorkflow', 2, 'repeatParams', 1, 'res', 'snapshotIds'),
(null, 'DeleteDiskWorkflow', 3, 'volumeId', 0, '', 'volumeId');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'DeleteInstanceWorkflow', 1, '', 0, '', ''),
(null, 'DeleteInstanceWorkflow', 2, 'instanceId', 0, '', 'instanceId'),
(null, 'DeleteInstanceWorkflow', 3, 'instanceId', 0, '', 'instanceId'),
(null, 'DeleteInstanceWorkflow', 3, 'repeatParams', 1, 'res', 'disks'),
(null, 'DeleteInstanceWorkflow', 4, 'instanceId', 0, '', 'instanceId'),
(null, 'DeleteInstanceWorkflow', 4, 'repeatParams', 1, 'res', 'netcards'),
(null, 'DeleteInstanceWorkflow', 5, 'instanceId', 0, '', 'instanceId'),
(null, 'DeleteInstanceWorkflow', 6, 'instanceId', 0, '', 'instanceId'),
(null, 'DeleteInstanceWorkflow', 7, 'instanceId', 0, '', 'instanceId');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'ForceDetachAndDeleteNetcardWorkflow', 1, '', 0, '', ''),
(null, 'ForceDetachAndDeleteNetcardWorkflow', 1, 'netcard', 0, '', 'repeatParams'),
(null, 'ForceDetachAndDeleteNetcardWorkflow', 2, 'networkDetail', 1, 'res', 'networkDetail'),
(null, 'ForceDetachAndDeleteNetcardWorkflow', 2, 'instanceId', 1, 'res', 'instanceId'),
(null, 'ForceDetachAndDeleteNetcardWorkflow', 3, 'networkDetail', 1, 'res', 'networkDetail'),
(null, 'ForceDetachAndDeleteNetcardWorkflow', 3, 'instanceId', 1, 'res', 'instanceId'),
(null, 'ForceDetachAndDeleteNetcardWorkflow', 4, 'networkDetail', 1, 'res', 'networkDetail'),
(null, 'ForceDetachAndDeleteNetcardWorkflow', 4, 'instanceId', 1, 'res', 'instanceId'),
(null, 'ForceDetachAndDeleteNetcardWorkflow', 5, 'netcardId', 1, 'res', 'netcardId'),
(null, 'ForceDetachAndDeleteNetcardWorkflow', 6, 'netcardId', 1, 'res', 'netcardId');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'ForceDetachAndDeleteDiskWorkflow', 1, '', 0, '', ''),
(null, 'ForceDetachAndDeleteDiskWorkflow', 1, 'disk', 0, '', 'repeatParams'),
(null, 'ForceDetachAndDeleteDiskWorkflow', 2, 'instanceId', 1, 'res', 'instanceId'),
(null, 'ForceDetachAndDeleteDiskWorkflow', 2, 'volumeId', 1, 'res', 'diskId'),
(null, 'ForceDetachAndDeleteDiskWorkflow', 2, 'node', 1, 'res', 'node'),
(null, 'ForceDetachAndDeleteDiskWorkflow', 3, 'instanceId', 1, 'res', 'instanceId'),
(null, 'ForceDetachAndDeleteDiskWorkflow', 3, 'volumeId', 1, 'res', 'diskId'),
(null, 'ForceDetachAndDeleteDiskWorkflow', 3, 'attachmentId', 1, 'res', 'attachmentId'),
(null, 'ForceDetachAndDeleteDiskWorkflow', 4, 'volumeId', 1, 'res', 'diskId');


-- 安全集群
-- 安全集群--创建安全集群
INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'CreateSecurityClusterWorkflow', 1, 'createSecurityClusterInitFlowCommand', 'command', null, '2', null, 1, 0, 0, 0, 1, 'FROM_ONE_DONE', null, 0),
(null, 'CreateSecurityClusterWorkflow', 2, 'createSecurityClusterOvsBridgesWorkflow', 'task_flow', '1', '3', null, 1, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'CreateSecurityClusterWorkflow', 3, 'createSecurityClusterObjectsWorkflow', 'task_flow', '2', '4', null, 1, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'CreateSecurityClusterWorkflow', 4, 'createSecurityClusterDoneFlowCommand', 'command', '3', null, null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0);


INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'CreateSecurityClusterOvsBridgesWorkflow', 1, 'createSecurityClusterOvsBridgeWorkflow', 'task_flow', null, null, null, 1, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0);

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'CreateSecurityClusterOvsBridgeWorkflow', 1, 'createSecurityClusterOvsBridgeFlowCommand', 'command', null, '2', null, 1, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'CreateSecurityClusterOvsBridgeWorkflow', 2, 'createSecurityClusterOvsBridgeDoneFlowCommand', 'command', '1', null, null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0);


INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'CreateSecurityClusterObjectsWorkflow', 1, 'createSecurityClusterObjectWorkflow', 'task_flow', null, null, null, 1, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0);

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'CreateSecurityClusterObjectWorkflow', 1, 'createSecurityClusterObjectInitFlowCommand', 'command', null, '2', null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'CreateSecurityClusterObjectWorkflow', 2, 'createSecurityClusterVmWorkflow', 'task_flow', '1', '3', null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'CreateSecurityClusterObjectWorkflow', 3, 'createSecurityClusterDcWorkflow', 'task_flow', '2', null, null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0);

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'CreateSecurityClusterVmWorkflow', 1, 'createSecurityClusterVmInitFlowCommand', 'command', null, '2', null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'CreateSecurityClusterVmWorkflow', 2, 'createInstanceWorkflow', 'task_flow', '1', '3', null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'CreateSecurityClusterVmWorkflow', 3, 'securityInstanceCreateDoneFlowCommand', 'command', '2', '4', null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'CreateSecurityClusterVmWorkflow', 4, 'securityClusterCreateAttachPortWorkflow', 'task_flow', '3', '5', null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', 'serial', 0),
(null, 'CreateSecurityClusterVmWorkflow', 5, 'createSecurityClusterVmDoneFlowCommand', 'command', '4', null, null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0);

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'SecurityClusterCreateAttachPortWorkflow', 1, 'securityClusterCreateAttachInitFlowCommand', 'command', null, '2', null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'SecurityClusterCreateAttachPortWorkflow', 2, 'createAttachNetcardWorkflow', 'task_flow', '1', null, null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0);


INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'CreateSecurityClusterWorkflow', 1, '', 0, '', ''),
(null, 'CreateSecurityClusterWorkflow', 2, 'bridgeInfos', 1, 'res', 'bridgeInfos'),
(null, 'CreateSecurityClusterWorkflow', 3, 'components', 1, 'res', 'components'),
(null, 'CreateSecurityClusterWorkflow', 4, 'clusterId', 1, 'res', 'clusterId');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'CreateSecurityClusterOvsBridgesWorkflow', 1, 'bridgeInfos', 0, '', 'bridgeInfos');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'CreateSecurityClusterOvsBridgeWorkflow', 1, 'bridgeInfo', 0, '', 'batchParams'),
(null, 'CreateSecurityClusterOvsBridgeWorkflow', 2, 'ovsBridgeId', 1, 'res', 'ovsBridgeId'),
(null, 'CreateSecurityClusterOvsBridgeWorkflow', 2, 'securityOvsBridgeId', 1, 'res', 'securityOvsBridgeId');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'CreateSecurityClusterObjectsWorkflow', 1, 'components', 0, '', 'components');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'CreateSecurityClusterObjectWorkflow', 1, 'component', 0, '', 'batchParams'),
(null, 'CreateSecurityClusterObjectWorkflow', 2, 'component', 1, 'res', 'component'),
(null, 'CreateSecurityClusterObjectWorkflow', 3, 'component', 1, 'res', 'component');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'CreateSecurityClusterVmWorkflow', 1, '', 0, '', ''),
(null, 'CreateSecurityClusterVmWorkflow', 2, 'imageId', 1, 'res', 'imageId'),
(null, 'CreateSecurityClusterVmWorkflow', 2, 'instanceType', 1, 'res', 'instanceType'),
(null, 'CreateSecurityClusterVmWorkflow', 2, 'instanceName', 1, 'res', 'instanceName'),
(null, 'CreateSecurityClusterVmWorkflow', 2, 'createHost', 1, 'res', 'createHost'),
(null, 'CreateSecurityClusterVmWorkflow', 2, 'systemDiskSize', 1, 'res', 'systemDiskSize'),
(null, 'CreateSecurityClusterVmWorkflow', 2, 'systemDiskCategory', 1, 'res', 'systemDiskCategory'),
(null, 'CreateSecurityClusterVmWorkflow', 2, 'dataDisk', 1, 'res', 'dataDisks'),
(null, 'CreateSecurityClusterVmWorkflow', 2, 'zxAuth', 1, 'res', 'zxAuth'),
(null, 'CreateSecurityClusterVmWorkflow', 2, 'zoneId', 1, 'res', 'zoneId'),
(null, 'CreateSecurityClusterVmWorkflow', 2, 'currentUser', 1, 'res', 'currentUser'),
(null, 'CreateSecurityClusterVmWorkflow', 3, 'instanceInfo', 2, 'res', ''),
(null, 'CreateSecurityClusterVmWorkflow', 3, 'componentId', 1, 'res', 'componentId'),
(null, 'CreateSecurityClusterVmWorkflow', 4, 'repeatParams', 1, 'res', 'netcardInfos'),
(null, 'CreateSecurityClusterVmWorkflow', 4, 'instanceId', 3, 'res', 'instanceId'),
(null, 'CreateSecurityClusterVmWorkflow', 4, 'currentUser', 1, 'res', 'currentUser');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'SecurityClusterCreateAttachPortWorkflow', 1, 'netcardInfo', 0, '', 'repeatParams'),
(null, 'SecurityClusterCreateAttachPortWorkflow', 1, 'instanceId', 0, '', 'instanceId'),
(null, 'SecurityClusterCreateAttachPortWorkflow', 2, 'instanceId', 1, 'res', 'instanceId'),
(null, 'SecurityClusterCreateAttachPortWorkflow', 2, 'subnetId', 1, 'res', 'subnetId'),
(null, 'SecurityClusterCreateAttachPortWorkflow', 2, 'ipAddress', 1, 'res', 'ipAddress'),
(null, 'SecurityClusterCreateAttachPortWorkflow', 2, 'securityGroupId', 1, 'res', 'securityGroupId'),
(null, 'SecurityClusterCreateAttachPortWorkflow', 2, 'createUser', 0, '', 'currentUser'),
(null, 'SecurityClusterCreateAttachPortWorkflow', 2, 'ovsBridgeId', 1, 'res', 'ovsBridgeId');



-- 安全集群--删除安全集群
INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'DeleteSecurityClusterWorkflow', 1, 'deleteSecurityClusterInitFlowCommand', 'command', null, '2', null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'DeleteSecurityClusterWorkflow', 2, 'deleteSecurityClusterObjectsWorkflow', 'task_flow', '1', '3', null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'DeleteSecurityClusterWorkflow', 3, 'deleteSecurityClusterOvsBridgesWorkflow', 'task_flow', '2', '4', null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'DeleteSecurityClusterWorkflow', 4, 'deleteSecurityClusterDoneFlowCommand', 'command', '3', null, null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0);

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'DeleteSecurityClusterObjectsWorkflow', 1, 'deleteSecurityClusterObjectWorkflow', 'task_flow', null, null, null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0);

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'DeleteSecurityClusterObjectWorkflow', 1, 'deleteSecurityClusterObjectInitFlowCommand', 'command', null, '2', null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'DeleteSecurityClusterObjectWorkflow', 2, 'deleteSecurityClusterVmWorkflow', 'task_flow', '1', '3', null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'DeleteSecurityClusterObjectWorkflow', 3, 'deleteSecurityClusterDcWorkflow', 'task_flow', '2', null, null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0);

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'DeleteSecurityClusterVmWorkflow', 1, 'deleteSecurityClusterVmInitFlowCommand', 'command', null, '2', null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'DeleteSecurityClusterVmWorkflow', 2, 'deleteInstanceWorkflow', 'task_flow', '1', '3', null, 1, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'DeleteSecurityClusterVmWorkflow', 3, 'deleteSecurityClusterVmDoneFlowCommand', 'command', '2', null, null, 1, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0);

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'DeleteSecurityClusterOvsBridgesWorkflow', 1, 'deleteSecurityClusterOvsBridgeWorkflow', 'task_flow', null, null, null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0);

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'DeleteSecurityClusterOvsBridgeWorkflow', 1, 'deleteSecurityClusterOvsBridgeFlowCommand', 'command', null, '2', null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'DeleteSecurityClusterOvsBridgeWorkflow', 2, 'deleteSecurityClusterOvsBridgeDoneFlowCommand', 'command', '1', null, null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0);



INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'DeleteSecurityClusterWorkflow', 1, 'clusterId', 0, '', 'id'),
(null, 'DeleteSecurityClusterWorkflow', 2, 'components', 1, 'res', 'components'),
(null, 'DeleteSecurityClusterWorkflow', 3, 'ovsBridges', 1, 'res', 'ovsBridges'),
(null, 'DeleteSecurityClusterWorkflow', 4, 'clusterId', 0, '', 'id');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'DeleteSecurityClusterObjectsWorkflow', 1, 'components', 0, '', 'components');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'DeleteSecurityClusterObjectWorkflow', 1, 'component', 0, '', 'batchParams'),
(null, 'DeleteSecurityClusterObjectWorkflow', 2, 'component', 1, 'res', 'component'),
(null, 'DeleteSecurityClusterObjectWorkflow', 3, 'component', 1, 'res', 'component');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'DeleteSecurityClusterVmWorkflow', 1, 'component', 0, '', 'component'),
(null, 'DeleteSecurityClusterVmWorkflow', 2, 'instanceId', 1, 'res', 'instanceId'),
(null, 'DeleteSecurityClusterVmWorkflow', 2, 'inTask', 1, 'res', 'deleteInTask'),
(null, 'DeleteSecurityClusterVmWorkflow', 2, 'clusterOvsBridgeId', 1, 'res', 'clusterOvsBridgeId'),
(null, 'DeleteSecurityClusterVmWorkflow', 2, 'deleteNotExist', 1, 'res', 'deleteNotExist'),
(null, 'DeleteSecurityClusterVmWorkflow', 3, 'componentId', 1, 'res', 'componentId');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'DeleteSecurityClusterOvsBridgesWorkflow', 1, 'ovsBridges', 0, '', 'ovsBridges');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'DeleteSecurityClusterOvsBridgeWorkflow', 1, 'ovsBridgeInfo', 0, '', 'batchParams'),
(null, 'DeleteSecurityClusterOvsBridgeWorkflow', 2, 'clusterOvsBridgeId', 1, 'res', 'clusterOvsBridgeId');


-- 安全集群--启用ha
INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'EnableSecurityClusterHaWorkflow', 1, 'enableSecurityClusterHaInitFlowCommand', 'command', null, '2', null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'EnableSecurityClusterHaWorkflow', 2, 'createSecurityClusterOvsBridgesWorkflow', 'task_flow', '1', '3', null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'EnableSecurityClusterHaWorkflow', 3, 'createSecurityClusterObjectsWorkflow', 'task_flow', '2', '4', null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'EnableSecurityClusterHaWorkflow', 4, 'createAndAttachHaNetcardsWorkflow', 'task_flow', '3', '5', null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'EnableSecurityClusterHaWorkflow', 5, 'enableSecurityClusterHaDoneFlowCommand', 'command', '4', null, null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0);

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'CreateAndAttachHaNetcardsWorkflow', 1, 'createAndAttachHaNetcardWorkflow', 'task_flow', null, null, null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0);

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'CreateAndAttachHaNetcardWorkflow', 1, 'createAndAttachHaNetcardInitFlowCommand', 'command', null, '2', null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'CreateAndAttachHaNetcardWorkflow', 2, 'createAttachNetcardWorkflow', 'task_flow', '1', null, null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0);

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'EnableSecurityClusterHaWorkflow', 1, '', 0, '', ''),
(null, 'EnableSecurityClusterHaWorkflow', 2, 'bridgeInfos', 1, 'res', 'bridgeInfos'),
(null, 'EnableSecurityClusterHaWorkflow', 3, 'components', 1, 'res', 'components'),
(null, 'EnableSecurityClusterHaWorkflow', 4, 'haNetcardInfos', 1, 'res', 'haNetcardInfos'),
(null, 'EnableSecurityClusterHaWorkflow', 4, 'currentUser', 0, '', 'currentUser'),
(null, 'EnableSecurityClusterHaWorkflow', 5, 'clusterId', 1, 'res', 'clusterId');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'CreateAndAttachHaNetcardWorkflow', 1, 'haNetcardInfo', 0, '', 'batchParams'),
(null, 'CreateAndAttachHaNetcardWorkflow', 2, 'instanceId', 1, 'res', 'instanceId'),
(null, 'CreateAndAttachHaNetcardWorkflow', 2, 'subnetId', 1, 'res', 'subnetId'),
(null, 'CreateAndAttachHaNetcardWorkflow', 2, 'ipAddress', 1, 'res', 'ipAddress'),
(null, 'CreateAndAttachHaNetcardWorkflow', 2, 'securityGroupId', 1, 'res', 'securityGroupId'),
(null, 'CreateAndAttachHaNetcardWorkflow', 2, 'ovsBridgeId', 1, 'res', 'ovsBridgeId'),
(null, 'CreateAndAttachHaNetcardWorkflow', 2, 'createUser', 0, '', 'currentUser');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'CreateAndAttachHaNetcardsWorkflow', 1, 'haNetcardInfos', 0, '', 'haNetcardInfos'),
(null, 'CreateAndAttachHaNetcardsWorkflow', 1, 'currentUser', 0, '', 'currentUser');
-- 安全集群--禁用ha
INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'DisableSecurityClusterHaWorkflow', 1, 'disableSecurityClusterHaInitFlowCommand', 'command', null, '2', null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'DisableSecurityClusterHaWorkflow', 2, 'deleteSecurityClusterObjectsWorkflow', 'task_flow', '1', '3', null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'DisableSecurityClusterHaWorkflow', 3, 'deleteSecurityClusterOvsBridgesWorkflow', 'task_flow', '2', '4', null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'DisableSecurityClusterHaWorkflow', 4, 'detachAndDeleteHaNetcardsWorkflow', 'task_flow', '3', '5', null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'DisableSecurityClusterHaWorkflow', 5, 'disableSecurityClusterHaDoneFlowCommand', 'command', '4', null, null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0);

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'DetachAndDeleteHaNetcardsWorkflow', 1, 'detachAndDeleteHaNetcardWorkflow', 'task_flow', null, null, null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0);



INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'DetachAndDeleteHaNetcardWorkflow', 1, 'detachAndDeleteHaNetcardInitFlowCommand', 'command', null, '2', null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'DetachAndDeleteHaNetcardWorkflow', 2, 'detachAndDeleteNetcardWorkflow', 'task_flow', '1', null, null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0);

INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'DetachAndDeleteNetcardWorkflow', 1, 'detachPortWorkflow', 'task_flow', null, '2', null, 0, 1, 1, 0, 0, 'FROM_ONE_DONE', null, 0),
(null, 'DetachAndDeleteNetcardWorkflow', 2, 'deleteNetcardFlowCommand', 'command', '1', null, null, 0, 0, 0, 0, 0, 'FROM_ONE_DONE', null, 0);




INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'DisableSecurityClusterHaWorkflow', 1, 'clusterId', 0, '', 'id'),
(null, 'DisableSecurityClusterHaWorkflow', 2, 'components', 1, 'res', 'components'),
(null, 'DisableSecurityClusterHaWorkflow', 3, 'ovsBridges', 1, 'res', 'ovsBridges'),
(null, 'DisableSecurityClusterHaWorkflow', 4, 'haNetcardInfos', 1, 'res', 'haNetcardInfos'),
(null, 'DisableSecurityClusterHaWorkflow', 5, 'clusterId', 0, '', 'id');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'DetachAndDeleteHaNetcardWorkflow', 1, 'haNetcardInfo', 0, '', 'batchParams'),
(null, 'DetachAndDeleteHaNetcardWorkflow', 2, 'instanceId', 1, 'res', 'instanceId'),
(null, 'DetachAndDeleteHaNetcardWorkflow', 2, 'networkInterfaceId', 1, 'res', 'networkInterfaceId');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'DetachAndDeleteNetcardWorkflow', 1, 'instanceId', 0, '', 'instanceId'),
(null, 'DetachAndDeleteNetcardWorkflow', 1, 'networkInterfaceId', 0, '', 'networkInterfaceId'),
(null, 'DetachAndDeleteNetcardWorkflow', 1, 'inTask', 0, '', 'inTask'),
(null, 'DetachAndDeleteNetcardWorkflow', 2, 'netcardId', 0, '', 'portId');

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'DetachAndDeleteHaNetcardsWorkflow', 1, 'haNetcardInfos', 0, '', 'haNetcardInfos');

-- 删除Gcloud image work flow
INSERT INTO gcloud_controller.gc_work_flow_template (id, flow_type_code, step_id, exec_command, step_type, from_ids, y_to_ids, n_to_ids, necessary, async, rollback_async, rollback_skip, rollback_fail_continue, from_relation, repeat_type, visible)
VALUES
(null, 'DeleteGcloudImageWorkflow', 1, 'deleteGcloudImageFlowCommand', 'command', null, '2', null, 1, 0, 0, 1, 0, 'FROM_ONE_DONE', null, 1),
(null, 'DeleteGcloudImageWorkflow', 2, 'deleteImageCacheFlowCommand', 'command', '1', '3', null, 0, 1, 0, 1, 0, 'FROM_ONE_DONE', 'serial', 1),
(null, 'DeleteGcloudImageWorkflow', 3, 'deleteImageCacheDoneFlowCommand', 'command', '2', null, null, 1, 0, 0, 1, 0, 'FROM_ONE_DONE', null, 1);

INSERT INTO gcloud_controller.gc_work_flow_command_value_template (id, flow_type_code, step_id, field_name, from_step_id, from_param_type, from_field_name)
VALUES
(null, 'DeleteGcloudImageWorkflow', 1, '', 0, '', ''),
(null, 'DeleteGcloudImageWorkflow', 2, 'repeatParams', 1, 'res', 'stores'),
(null, 'DeleteGcloudImageWorkflow', 2, 'imageId', 0, '', 'imageId'),
(null, 'DeleteGcloudImageWorkflow', 3, 'imageId', 0, '', 'imageId');

DROP TABLE IF EXISTS `gc_image_stores`;
CREATE TABLE `gc_image_stores` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID，自增',
  `image_id` char(64) NOT NULL COMMENT '镜像ID',
  `store_target` char(64) NOT NULL COMMENT '存储目标',
  `store_type` char(64) NOT NULL COMMENT '存储类型，node、vg、rbd',
  `status` char(32) NOT NULL COMMENT '镜像分发状态，downloading\deleting\active',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 添加可用区节点关联表 2019-08-05
DROP TABLE IF EXISTS `gc_zone_node`;
CREATE TABLE `gc_zone_node` (
`id` VARCHAR(36) NOT NULL COMMENT 'id' ,
`hostname` varchar(128) NOT NULL COMMENT '节点名称' ,
`zone_id` varchar(36) NOT NULL COMMENT '可用区ID' ,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='可用区节点';

-- 添加节点组表 2019-08-05
DROP TABLE IF EXISTS `gc_group_node`;
CREATE TABLE `gc_group_node` (
`id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id' ,
`hostname` varchar(128) NOT NULL COMMENT '节点名称' ,
`group_id` varchar(36) NOT NULL COMMENT '分组ID' ,
`cluster_group_id` varchar(36) NULL DEFAULT NULL COMMENT '集群组ID' ,
`create_time` datetime NULL DEFAULT NULL COMMENT '入组时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `IDX_NODEGROUP_01` (`hostname`, `group_id`) USING BTREE 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节点组';

-- 添加可用区和磁盘类型的关联表  2019-08-15
DROP TABLE IF EXISTS `gc_zone_disk_categories`;
CREATE TABLE `gc_zone_disk_categories` (
	`id` int(16) AUTO_INCREMENT NOT NULL,
	`zone_id` VARCHAR(36) NOT NULL,
	`disk_category_id` VARCHAR(36) NOT NULL,
	PRIMARY KEY(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;