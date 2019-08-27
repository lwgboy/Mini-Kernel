package com.gcloud.core.quartz.configuration;

import com.gcloud.core.quartz.QuartzInitialize;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableScheduling
@ConditionalOnExpression("${gcloud.quartz.enable:false} == true")
public class QuartzConfiguration {

    private final String RAM_JOB_STORE = "org.quartz.simpl.RAMJobStore";
    private final String STORE_TYPE_RAM = "ram";
    private final String STORE_TYPE_DATABASE = "db";

    @Value("${gcloud.quartz.store-type:db}")
    private String storeType;
	
	@Value("${gcloud.quartz.scheduler.instanceName:gcloud-quartz}")
    private String schedulerInstanceName;
    @Value("${gcloud.quartz.scheduler.instanceId:AUTO}")
    private String schedulerInstanceId;
    @Value("${gcloud.quartz.scheduler.skipUpdateCheck:true}")
    private String schedulerSkipUpdateCheck;

    @Value("${gcloud.quartz.threadPool.class:org.quartz.simpl.SimpleThreadPool}")
    private String threadPoolClass;
    @Value("${gcloud.quartz.threadPool.threadCount:20}")
    private String threadPoolThreadCount;
    @Value("${gcloud.quartz.threadPool.threadPriority:5}")
    private String threadPoolThreadPriority;

    @Value("${gcloud.quartz.jobStore.misfireThreshold:60000}")
    private String jobStoreMisfireThreshold;
    @Value("${gcloud.quartz.jobStore.class:org.quartz.impl.jdbcjobstore.JobStoreTX}")
    private String jobStoreClass;
    @Value("${gcloud.quartz.jobStore.driverDelegateClass:org.quartz.impl.jdbcjobstore.StdJDBCDelegate}")
    private String jobStoreDriverDelegateClass;
    @Value("${gcloud.quartz.jobStore.useProperties:false}")
    private String jobStoreUseProperties;
    @Value("${gcloud.quartz.jobStore.tablePrefix:qrtz_}")
    private String jobStoreTablePrefix;
    @Value("${gcloud.quartz.jobStore.isClustered:true}")
    private String jobStoreIsClustered;


    @PostConstruct
    public void init(){
        if(STORE_TYPE_RAM.equals(storeType)){
            jobStoreClass = RAM_JOB_STORE;
        }
    }

	/**
     * 继承org.springframework.scheduling.quartz.SpringBeanJobFactory
     * 实现任务实例化方式
     */
    public static class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements
            ApplicationContextAware {

        private transient AutowireCapableBeanFactory beanFactory;

        @Override
        public void setApplicationContext(final ApplicationContext context) {
            beanFactory = context.getAutowireCapableBeanFactory();
        }

        /**
         * 将job实例交给spring ioc托管
         * 我们在job实例实现类内可以直接使用spring注入的调用被spring ioc管理的实例
         *
         * @param bundle
         * @return
         * @throws Exception
         */
        @Override
        protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
            final Object job = super.createJobInstance(bundle);
            /**
             * 将job实例交付给spring ioc
             */
            beanFactory.autowireBean(job);
            return job;
        }
    }

    /**
     * 配置任务工厂实例
     *
     * @param applicationContext spring上下文实例
     * @return
     */
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        /**
         * 采用自定义任务工厂 整合spring实例来完成构建任务
         * see {@link AutowiringSpringBeanJobFactory}
         */
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    /**
     * 配置任务调度器
     * 使用项目数据源作为quartz数据源
     *
     * @param jobFactory 自定义配置任务工厂
     * @param dataSource 数据源实例
     * @return
     * @throws Exception
     */
    @Bean(destroyMethod = "destroy", autowire = Autowire.NO)
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory, DataSource dataSource) throws Exception {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        //将spring管理job自定义工厂交由调度器维护
        schedulerFactoryBean.setJobFactory(jobFactory);
        //设置覆盖已存在的任务
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        //项目启动完成后，等待2秒后开始执行调度器初始化
        schedulerFactoryBean.setStartupDelay(2);
        //设置调度器自动运行
        schedulerFactoryBean.setAutoStartup(true);

        if (!RAM_JOB_STORE.equals(jobStoreClass)) {
            //设置数据源，使用与项目统一数据源
            schedulerFactoryBean.setDataSource(dataSource);
        }
        //设置上下文spring bean name
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        //设置配置文件位置
        //schedulerFactoryBean.setConfigLocation(new ClassPathResource("/quartz.properties"));
        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        return schedulerFactoryBean;
    }
    
    @Bean
    public QuartzInitialize quartzInitialize(){
        return new QuartzInitialize();
    }
    
    private Properties quartzProperties(){

        Properties props = new Properties();
        props.put("org.quartz.scheduler.instanceName", schedulerInstanceName);
        props.put("org.quartz.scheduler.instanceId", schedulerInstanceId);
        props.put("org.quartz.scheduler.skipUpdateCheck", schedulerSkipUpdateCheck);

        props.put("org.quartz.threadPool.class", threadPoolClass);
        props.put("org.quartz.threadPool.threadCount", threadPoolThreadCount);
        props.put("org.quartz.threadPool.threadPriority", threadPoolThreadPriority);

        props.put("org.quartz.jobStore.class", jobStoreClass);
        props.put("org.quartz.jobStore.misfireThreshold", jobStoreMisfireThreshold);

        if (!RAM_JOB_STORE.equals(jobStoreClass)) {
            props.put("org.quartz.jobStore.driverDelegateClass", jobStoreDriverDelegateClass);
            props.put("org.quartz.jobStore.useProperties", jobStoreUseProperties);
            props.put("org.quartz.jobStore.tablePrefix", jobStoreTablePrefix);
            props.put("org.quartz.jobStore.isClustered", jobStoreIsClustered);
        }

        return props;

    }
}
