
USE gcloud_controller;

-- gcloud多实现    2019-05-21

ALTER TABLE `gc_routers` ADD `provider` int(10) DEFAULT null;
ALTER TABLE `gc_routers` ADD `provider_ref_id` varchar(40) DEFAULT null;
ALTER TABLE `gc_routers` ADD `updated_at` datetime DEFAULT null;

ALTER TABLE `gc_subnets` ADD `provider` int(10) DEFAULT null;
ALTER TABLE `gc_subnets` ADD `provider_ref_id` varchar(40) DEFAULT null;
ALTER TABLE `gc_subnets` ADD `updated_at` datetime DEFAULT null;

ALTER TABLE `gc_security_groups` ADD `provider` int(10) DEFAULT null;
ALTER TABLE `gc_security_groups` ADD `provider_ref_id` varchar(40) DEFAULT null;
ALTER TABLE `gc_security_groups` ADD `updated_at` datetime DEFAULT null;

ALTER TABLE `gc_floating_ips` ADD `provider` int(10) DEFAULT null;
ALTER TABLE `gc_floating_ips` ADD `provider_ref_id` varchar(40) DEFAULT null;
ALTER TABLE `gc_floating_ips` ADD `updated_at` datetime DEFAULT null;

ALTER TABLE `gc_networks` ADD `provider` int(10) DEFAULT null;
ALTER TABLE `gc_networks` ADD `provider_ref_id` varchar(40) DEFAULT null;
ALTER TABLE `gc_networks` ADD `updated_at` datetime DEFAULT null;

ALTER TABLE `gc_ports` ADD `provider` int(10) DEFAULT null;
ALTER TABLE `gc_ports` ADD `provider_ref_id` varchar(40) DEFAULT null;
ALTER TABLE `gc_ports` ADD `updated_at` datetime DEFAULT null;

ALTER TABLE `gc_slb` ADD `provider` int(10) DEFAULT null;
ALTER TABLE `gc_slb` ADD `provider_ref_id` varchar(40) DEFAULT null;
ALTER TABLE `gc_slb` ADD `updated_at` datetime DEFAULT null;

ALTER TABLE `gc_images` ADD `provider` int(10) DEFAULT null;
ALTER TABLE `gc_images` ADD `provider_ref_id` varchar(40) DEFAULT null;
ALTER TABLE `gc_images` ADD `updated_at` datetime DEFAULT null;

ALTER TABLE `gc_snapshots` ADD `provider` int(10) DEFAULT null;
ALTER TABLE `gc_snapshots` ADD `provider_ref_id` varchar(40) DEFAULT null;
ALTER TABLE `gc_snapshots` ADD `updated_at` datetime DEFAULT null;

ALTER TABLE `gc_volumes` ADD `provider` int(10) DEFAULT null;
ALTER TABLE `gc_volumes` ADD `provider_ref_id` varchar(40) DEFAULT null;
ALTER TABLE `gc_volumes` ADD `updated_at` datetime DEFAULT null;

ALTER TABLE `gc_storage_pools` ADD `provider` int(10) DEFAULT null;
ALTER TABLE `gc_storage_pools` ADD `provider_ref_id` varchar(40) DEFAULT null;

ALTER TABLE `gc_ovs_bridges` ADD `provider` int(10) DEFAULT null;
ALTER TABLE `gc_ovs_bridges` ADD `provider_ref_id` varchar(40) DEFAULT null;


-- gcloud磁盘类型    2019-06-05

DROP TABLE IF EXISTS `gc_disk_categories`;
CREATE TABLE `gc_disk_categories` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  `min_size` int(10) DEFAULT null,
  `max_size` int(10) DEFAULT null,
  `zone_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `gc_storage_pools` ADD `category_id` varchar(36) DEFAULT NULL;

ALTER TABLE `gc_volumes` ADD `pool_id` varchar(36) DEFAULT NULL;

-- gcloud可用区    2019-06-05

DROP TABLE IF EXISTS `gc_zones`;
CREATE TABLE `gc_zones` (
  `id` varchar(36) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `gc_subnets` ADD `zone_id` varchar(36) DEFAULT NULL;
ALTER TABLE `gc_compute_nodes` ADD `zone_id` varchar(36) DEFAULT NULL;
ALTER TABLE `gc_instances` ADD `zone_id` varchar(36) DEFAULT NULL;
ALTER TABLE `gc_instance_types` ADD `zone_id` varchar(36) DEFAULT NULL;
ALTER TABLE `gc_volumes` ADD `zone_id` varchar(36) DEFAULT NULL;
ALTER TABLE `gc_storage_pools` ADD `zone_id` varchar(36) DEFAULT NULL;

-- ovs_bridge_id 2019-06-10
alter table gc_ports change custom_ovs_br ovs_bridge_id varchar(200) null;
alter table gc_ovs_bridges_usage modify id bigint(20) auto_increment;

-- 虚拟机表租户ID    2019-06-17
ALTER TABLE `gc_instances` ADD `tenant_id` varchar(64) DEFAULT null;
ALTER TABLE `gc_images` ADD `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID';
ALTER TABLE `gc_volumes` ADD `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID';
ALTER TABLE `gc_snapshots` ADD `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID';
ALTER TABLE `gc_floating_ips` ADD `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID';
ALTER TABLE `gc_networks` ADD `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID';
ALTER TABLE `gc_routers` ADD `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID';
ALTER TABLE `gc_subnets` ADD `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID';
ALTER TABLE `gc_security_groups` ADD `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID';
ALTER TABLE `gc_slb` ADD `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID';
ALTER TABLE `gc_slb` ADD `user_id` varchar(64) DEFAULT NULL;

-- 网卡表租户ID    2019-06-18
ALTER TABLE `gc_ports` ADD `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID';


-- 负载均衡服务器组库表   2019-06-19
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

-- 浮动IP表qos、带宽字段   2019-06-19
ALTER TABLE `gc_floating_ips` ADD `bw_qos_policy_id` varchar(64) DEFAULT NULL COMMENT '带宽qos策略';
ALTER TABLE `gc_floating_ips` ADD `bandwidth` bigint(10) DEFAULT NULL COMMENT '带宽';
ALTER TABLE `gc_floating_ips` DROP column `routerId`;
ALTER TABLE `gc_floating_ips` ADD `router_id` varchar(64) DEFAULT NULL COMMENT '路由id';

-- 安全组规则表   2019-06-21
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

-- 默认安全组
ALTER TABLE gc_security_groups ADD is_default tinyint(1) null;

-- 任务流实例表、任务流实例步骤表添加最顶层任务id  2019-06-25
ALTER TABLE `gc_work_flow_instance` ADD `topest_flow_task_id` char(64) DEFAULT NULL COMMENT '最顶层任务id，根据这个字段判断是否在同一个任务流程';
ALTER TABLE `gc_work_flow_instance_step` ADD `topest_flow_task_id` char(64) DEFAULT NULL COMMENT '最顶层任务id，根据这个字段判断是否在同一个任务流程';

--
ALTER TABLE `gc_volume_attachments` ADD `provider` int(10) DEFAULT null;
ALTER TABLE `gc_volume_attachments` ADD `provider_ref_id` varchar(40) DEFAULT null;

-- 获取任务流步骤优先级
delimiter ~
DROP FUNCTION IF EXISTS getPriority~

CREATE FUNCTION getPriority (inID INT, flowId INT) RETURNS VARCHAR(255) DETERMINISTIC
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

-- 存储池加上hostname    2019-06-28
ALTER TABLE `gc_storage_pools` ADD `hostname` varchar(36) DEFAULT NULL;

-- 添加回滚失败后是否必回滚
ALTER TABLE `gc_work_flow_template` ADD `rollback_fail_continue` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0 流程回滚失败后不回滚，1 流程回滚失败后回滚；';
ALTER TABLE `gc_work_flow_instance_step` ADD `rollback_fail_continue` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0 流程回滚失败后不回滚，1 流程回滚失败后回滚；';

-- 磁盘类型加上code    2019-07-03
ALTER TABLE `gc_disk_categories` ADD `code` varchar(50) NOT NULL;

-- 完成获取步骤层级顺序函数，因为mysql存储过程不支持递归，故用sub的形式调用。暂时不支持单步骤并行形式，只支持单步和分支。
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

-- 添加步骤描述字段  2019-7-8
 ALTER TABLE `gc_work_flow_template` ADD `step_desc` varchar(128) DEFAULT NULL COMMENT '步骤描述';
 ALTER TABLE `gc_work_flow_instance_step` ADD `step_desc` varchar(128) DEFAULT NULL COMMENT '步骤描述';
 ALTER TABLE `gc_work_flow_instance_step` ADD `rollback_start_time` datetime DEFAULT NULL COMMENT '回滚开始时间';
 ALTER TABLE `gc_work_flow_instance_step` ADD `rollback_update_time` datetime DEFAULT NULL COMMENT '回滚修改时间';

-- 实例类型添加zone_id 2019-7-11
ALTER TABLE `gc_instance_types` ADD `zone_id` varchar(36) DEFAULT NULL COMMENT '关联的可用区ID';

-- 存储池加上driver    2019-07-16
ALTER TABLE `gc_storage_pools` ADD `driver` varchar(36) DEFAULT NULL;


-- 存储类型 分布式改为 distributed 2019-07-17
alter table gc_storage_pools modify storage_type varchar(20) not null;
update gc_storage_pools set storage_type = 'distributed' where storage_type = 'rbd';
alter table gc_volumes modify storage_type varchar(20) not null;
alter table gc_snapshots modify storage_type varchar(20) not null;

-- 存储池增加connect_protocol 2019-07-18
alter table gc_storage_pools add connect_protocol varchar(20) null;

-- 修复slb问题
alter table gc_slb add vip_port_id varchar(36) null;
alter table gc_slb change vswitch_id vip_subnet_id varchar(64) not null;

DROP TABLE IF EXISTS `gc_image_stores`;
CREATE TABLE `gc_image_stores` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID，自增',
  `image_id` char(64) NOT NULL COMMENT '镜像ID',
  `store_target` char(64) NOT NULL COMMENT '存储目标',
  `store_type` char(64) NOT NULL COMMENT '存储类型，node、vg、rbd',
  `status` char(32) NOT NULL COMMENT '镜像分发状态，downloading\deleting\active',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `gc_images` ADD `disable` tinyint(1) NOT NULL COMMENT '是否禁用，1禁用0可用';

-- 网络修改  2019-08-01
alter table gc_subnets drop column router_id;
alter table gc_routers change external_network_id external_gateway_network_id varchar(50) null comment '外网网关';
CREATE TABLE `gc_router_ports` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `router_id` varchar(36) DEFAULT NULL,
  `port_id` varchar(36) DEFAULT NULL,
  `port_type` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

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

-- 打包流程修改 2019-08-06
INSERT INTO gc_work_flow_command_value_template (`flow_type_code`, `step_id`, `field_name`, `from_step_id`, `from_param_type`, `from_field_name`)
VALUES ('BundleInstanceWorkflow', 4, 'imageId', 1, 'res', 'imageId');

delete from gc_work_flow_template where flow_type_code = 'BundleInstanceWorkflow' and step_id in(2, 5, 6);
delete from gc_work_flow_command_value_template where flow_type_code = 'BundleInstanceWorkflow' and step_id in(2, 5, 6);

UPDATE gc_work_flow_template SET y_to_ids = '3' WHERE flow_type_code = 'BundleInstanceWorkflow' step_id = 1;
UPDATE gc_work_flow_template SET from_ids = '1' WHERE flow_type_code = 'BundleInstanceWorkflow' step_id = 3;
UPDATE gc_work_flow_template SET y_to_ids = '7' WHERE flow_type_code = 'BundleInstanceWorkflow' step_id = 8;
UPDATE gc_work_flow_template SET from_ids = '8' WHERE flow_type_code = 'BundleInstanceWorkflow' step_id = 7;

-- instance type 、 disk categories 和 storage_pool 关联可用区处理。
rename table gc_zones_instance_type to gc_zone_instance_types;


CREATE TABLE `gc_storage_pool_zones` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `storage_pool_id` varchar(36) DEFAULT NULL,
  `zone_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `gc_disk_category_pools` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disk_category_id` varchar(36) DEFAULT NULL,
  `zone_id` varchar(36) DEFAULT NULL,
  `storage_pool_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into gc_zone_instance_types (instance_type_id, zone_id) select t.id, t.zone_id from gc_instance_types t;
insert into gc_storage_pool_zones(storage_pool_id, zone_id) select t.id, t.zone_id from gc_storage_pools t;
insert into gc_disk_category_pools(disk_category_id, zone_id, storage_pool_id) SELECT t.category_id, t.zone_id, t.id from gc_storage_pools t;

ALTER TABLE gc_storage_pools ADD UNIQUE INDEX `UN_01` (`storage_type` ASC, `pool_name` ASC, `provider` ASC, `hostname` ASC);
alter table gc_disk_categories drop column zone_id;
alter table gc_storage_pools drop column zone_id;
alter table gc_storage_pools drop column category_id;
alter table gc_disk_categories drop column code;
alter table gc_instance_types drop column zone_id;

-- 添加可用区和磁盘类型的关联表  2019-08-15
CREATE TABLE `gc_zone_disk_categories` (
	`id` int(16) AUTO_INCREMENT NOT NULL,
	`zone_id` VARCHAR(36) NOT NULL,
	`disk_category_id` VARCHAR(36) NOT NULL,
	PRIMARY KEY(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

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

ALTER TABLE `gc_work_flow_task` ADD `need_feedback_log` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否需要feedback日志，0 | 1';