package com.sb.schedule;

import com.sb.config.QuartzJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

@QuartzJob(name = "HelloJob", cronExp = "*/3 * * * * ?")
public class HelloJob extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Hello Job is running @ " + new Date());
    }
}