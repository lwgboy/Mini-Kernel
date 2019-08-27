DROP DATABASE IF EXISTS `gcloud_identity`;
CREATE DATABASE `gcloud_identity`
    CHARACTER SET 'utf8'
    COLLATE 'utf8_general_ci';
USE gcloud_identity;

-- ----------------------------
-- Table structure for `gc_users`
-- ----------------------------
DROP TABLE IF EXISTS `gc_users`;
CREATE TABLE `gc_users` (
  `id` varchar(64) NOT NULL COMMENT 'ID',
  `login_name` varchar(255) NOT NULL COMMENT '登录名',
  `password` char(32) NOT NULL COMMENT '密码',
  `access_key` varchar(128) NOT NULL COMMENT 'api标识用户key',
  `secret_key` varchar(128) NOT NULL COMMENT 'api签名key',
  `gender` tinyint(1) NOT NULL COMMENT '性别，0男1女',
  `email` varchar(64) NOT NULL COMMENT 'email',
  `mobile` varchar(32) NOT NULL COMMENT '电话',
  `role_id` varchar(32) NOT NULL COMMENT ' 角色ID',
  `disable` tinyint(1) NOT NULL COMMENT '是否禁用，1禁用0可用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `creater` varchar(32) NOT NULL COMMENT '创建者',
  `real_name` varchar(64) DEFAULT NULL COMMENT '真实姓名',
  `login_count` bigint(10) NOT NULL DEFAULT '0' COMMENT '登录次数',
  `online` tinyint(1) NOT NULL COMMENT '是否在线，1在线0离线',
  `last_login_time` datetime DEFAULT NULL COMMENT '上一次登陆时间',
  `locked` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否锁定，1是0否',
  `lock_start_time` datetime DEFAULT NULL COMMENT '开始锁定时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name` (`login_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gc_user
-- ----------------------------
INSERT INTO `gc_users` (`id`, `login_name`, `password`, `access_key`, `secret_key`, `gender`, `email`, `mobile`, `role_id`, `disable`, `create_time`, `creater`, `real_name`, `login_count`, `online`, `last_login_time`,  `locked`, `lock_start_time`) 
VALUES ('cc81efe719bf4b05a3c21524ccf6c1ea', 'admin', '6e539d81a81b91740b22b07733bd6171', 'cc81efe719bf4b05a3c21524ccf6c1ea', 'rgXooMaQAns9ki6SFlHps3O8flgY4TcPpQWwit0G', 1, 'gcloudtest01@gdeii.com.cn', '18922988888', 'superadmin', 0, '2015-6-26 10:18:12', 'admin', '超级管理员', 0, 0, NULL, 0, NULL);

INSERT INTO `gc_users` (`id`, `login_name`, `password`, `access_key`, `secret_key`, `gender`, `email`, `mobile`, `role_id`, `disable`, `create_time`, `creater`, `real_name`, `login_count`, `online`, `last_login_time`, `locked`, `lock_start_time`) 
VALUES ('dd81efe719bf4b05a3c21524ccf6c2qq', 'tenantAdmin', '6e539d81a81b91740b22b07733bd6171', 'dd81efe719bf4b05a3c21524ccf6c2qq', 'weqdsMaQAns9ki6SFlHps212flgY4TcPpQRrit0F', '1', 'gcloudtest@qq.com', '18955555555', 'domainadmin', '0', '2019-06-14 11:15:24', 'admin', '租户管理员', '0', '0', NULL, '0', NULL);

DROP TABLE IF EXISTS `gc_tenants`;
CREATE TABLE `gc_tenants` (
	`id`  varchar(64) NOT NULL ,
	`name`  varchar(64) NOT NULL unique,
	`create_time` datetime NOT NULL COMMENT '创建时间',
	`creator`  varchar(64) NOT NULL ,
	`description`  varchar(255) default NULL ,
	PRIMARY KEY (`id`) 
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `gc_tenants` (`id`, `name`, `create_time`, `creator`, `description`) 
VALUES ('f6d4b7a3-db29-476b-aaa2-392fad843442', 'tenantAdmin', '2019-06-14 10:21:24', 'admin', NULL);

DROP TABLE IF EXISTS `gc_tenant_user`;
CREATE TABLE `gc_tenant_user` (
    `id`  BIGINT(20) NOT NULL  AUTO_INCREMENT ,
	`tenant_id`  varchar(64) NOT NULL ,
	`user_id`  varchar(64) NOT NULL,
	`create_time` datetime NOT NULL COMMENT '创建时间',
	PRIMARY KEY (`id`) 
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `gc_tenant_user` (`id`, `tenant_id`, `user_id`, `create_time`) 
VALUES (null, 'f6d4b7a3-db29-476b-aaa2-392fad843442', 'dd81efe719bf4b05a3c21524ccf6c2qq', '2019-06-14 11:22:15');

DROP TABLE IF EXISTS `gc_roles`;
CREATE TABLE `gc_roles` (
	`id`  varchar(64) NOT NULL ,
	`name`  varchar(64) NOT NULL unique,
	`create_time` datetime NOT NULL COMMENT '创建时间',
	`creator`  varchar(64) NOT NULL ,
	`description`  varchar(255) default NULL ,
	`role_type`  tinyint(1) NOT NULL DEFAULT '0' COMMENT '角色类型，0固定角色、1自定义角色',
	PRIMARY KEY (`id`) 
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `gc_roles` (`id`, `name`, `create_time`, `creator`, `description`, `role_type`) VALUES ('ordinaryadmin', '普通管理员', '2019-6-14 11:19:08', 'admin', '不可修改与删除', 0);
INSERT INTO `gc_roles` (`id`, `name`, `create_time`, `creator`, `description`, `role_type`) VALUES ('superadmin', '超级管理员', '2019-6-13 16:51:49', 'admin', '不可修改与删除', 0);