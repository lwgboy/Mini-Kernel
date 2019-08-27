package com.gcloud.controller.compute.timer;

import com.gcloud.core.quartz.GcloudJobBean;
import com.gcloud.core.quartz.annotation.QuartzTimer;
import com.gcloud.controller.compute.utils.NoVncUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Component;


@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@QuartzTimer(fixedDelay = 30 * 1000L)
@Slf4j
public class CleanNoVncTokenFileTimer extends GcloudJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("start CleanNoVncTokenFileTimer.");
        NoVncUtil.deleteExpiredToken(2000);
    }
}
