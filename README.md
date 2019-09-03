中文 | [English](https://github.com/lihangqi/Mini-Kernel/blob/master/README.en.md)

# 中国云OS最小内核

#### 介绍
中国云操作系统最小内核开源项目，基于中国云标准API。
中国云OS最小内核定位于打造成一个标准的最小化核心的基础云平台，功能组件包括计算、存储、网络、认证。
 
**主要功能模块** 
- 弹性计算服务：

1. 地域管理
2. 虚拟机管理
3. 磁盘管理
4. 镜像管理
5. 快照管理
6. 专有网络管理
7. 外部网络管理
8. 路由管理
9. 交换机管理
10. 安全组客理
11. 网卡管理
12. 弹性公网IP管理

- 负载均衡服务：

1. 负载均衡实例管理
2. 监听器管理
3. 后端服务管理
4. 调度策略管理
5. 健康检查


#### 软件架构
1. 后端：
- 基础框架：Spring Boot 1.5.13.RELEASE
- 持久层框架：gcloud-framework-db-7.1.0
- 安全框架：gcloud-service-identity-8.0.0
- 数据库连接池：阿里巴巴Druid 1.0.26
- 消息队列：rabbitmq
- 缓存框架：redis
- 日志打印：log4j
- 其他：fastjson，quartz, lombok（简化代码）等

2. 开发环境：
- 语言：Java 8
- IDE(JAVA)： Eclipse安装lombok插件 或者 IDEA
- 依赖管理：Maven
- 数据库：Mariadb
- 缓存：Redis


#### 安装教程

1. [详见本项目WIKI](https://github.com/lihangqi/Mini-Kernel/wiki/%E4%BA%91%E6%93%8D%E4%BD%9C%E7%B3%BB%E7%BB%9F%E6%9C%80%E6%96%B0%E5%86%85%E6%A0%B8%E7%BC%96%E8%AF%91%E9%83%A8%E7%BD%B2)

#### 使用说明

 **后台开发环境和依赖** 

1. java
2. maven
3. jdk8
4. mariadb
5. 数据库脚步：gcloud\gcloud-boot\sql\gcloud_controller_quartz.sql gcloud\gcloud-boot\sql\gcloud_controller.sql
6. 默认登录账号： admin/gcloud123 （源码版本免登录）


 **项目下载和运行** 
1. 拉取项目代码
直接下载本项目，或者远程拉取

`git clone http://113.105.131.176:29780/gcloud/g-cloud-8.0.git`

`cd g-cloud-8.0/gcloud`

2. 编译

`mvn clean;mvn package;`

3. 运行

`cd  gcloud/gcloud-boot/target/`

`nohup java -jar gcloud-boot-8.0.jar &`

4. 接口测试（以获取vpc列表为例）

`curl -i -X POST -H "X-Auth-Token: 4723ad1b-5190-3fc0-8c1b-96581dda3841" http://127.0.0.1:8089/ecs/DescribeVpcs.do`
