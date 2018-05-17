package com.sb.schedule;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.springframework.scheduling.quartz.QuartzJobBean;

@QuartzJob(name = "HelloJob", cronExp = "0/5 * * * * ?")
public class HelloJob extends QuartzJobBean {
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Hello Job is running @ " + new Date());
        System.out.println(this.hashCode());
    }
}