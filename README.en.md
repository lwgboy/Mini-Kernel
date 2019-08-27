[中文](https://github.com/lihangqi/Mini-Kernel/blob/master/README.md) | English

# China Cloud OS Minimum Kernel

#### Description
China's cloud operating system minimum kernel open source project, based on the Chinese cloud standard API.
China's cloud OS minimum kernel is positioned as a basic cloud platform that creates a standard minimum core, with functional components including computing, storage, networking, and authentication.

**Main function module**
- Flexible computing services:

1. Regional management
2. Virtual machine management
3. Disk management
4. Image management
5. Snapshot management
6. Proprietary network management
7. External network management
8. Route Management
9. Switch Management
10. Security Group
11. Network card management
12. Flexible public network IP management

- Load balancing service:

1. Load balancing instance management
2. Listener management
3. Backend service management
4. Scheduling policy management
5. Health check

#### Software Architecture
1. Backend:
- Basic framework: Spring Boot 1.5.13.RELEASE
- Persistence layer framework: gcloud-framework-db-7.1.0
- Security framework: gcloud-service-identity-8.0.0
- Database connection pool: Alibaba Druid 1.0.26
- Message queue: rabbitmq
- Cache Framework: redis
- Log printing: log4j
- Other: fastjson, quartz, lombok (simplified code), etc.

2. Development environment:
- Language: Java 8
- IDE (JAVA): Eclipse installs lombok plugin or IDEA
- Dependency Management: Maven
- Database: Mariadb
- Cache: Redis

#### Installation

[See WIKI for this project for details.](https://github.com/lihangqi/Mini-Kernel/wiki/%E4%BA%91%E6%93%8D%E4%BD%9C%E7%B3%BB%E7%BB%9F%E6%9C%80%E6%96%B0%E5%86%85%E6%A0%B8%E7%BC%96%E8%AF%91%E9%83%A8%E7%BD%B2)

#### Instructions

**Background development environment and dependencies**

1. java
2. Maven
3. jdk8
4. mariadb
5. Database step: gcloud\gcloud-boot\sql\gcloud_controller_quartz.sql gcloud\gcloud-boot\sql\gcloud_controller.sql
6. Default login account: admin/gcloud123 (source version free login)


**Project download and run**

1. Pull the project code
Download this project directly, or remotely pull git clone http://113.105.131.176:29780/gcloud/g-cloud-8.0.git
`cd g-cloud-8.0/gcloud`

2. Compile
`mvn clean;mvn package;`

3. Run
`cd gcloud/gcloud-boot/target/`
`nohup java -jar gcloud-boot-8.0.jar &`

4. Interface test (take the vpc list as an example)
`curl -i -X POST -H "X-Auth-Token: 4723ad1b-5190-3fc0-8c1b-96581dda3841" http://127.0.0.1:8089/ecs/DescribeVpcs.do`