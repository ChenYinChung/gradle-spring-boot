package com.sb.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.Job;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

public class QuartJobSchedulingListener implements ApplicationListener<ContextRefreshedEvent>
{
    @Autowired
    private Scheduler scheduler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        try
        {
            ApplicationContext applicationContext = event.getApplicationContext();
            List<CronTriggerFactoryBean> cronTriggerBeans = this.loadCronTriggerBeans(applicationContext);
            this.scheduleJobs(cronTriggerBeans);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<CronTriggerFactoryBean> loadCronTriggerBeans(ApplicationContext applicationContext)
    {
        Map<String, Object> quartzJobBeans = applicationContext.getBeansWithAnnotation(QuartzJob.class);
        Set<String> beanNames = quartzJobBeans.keySet();
        List<CronTriggerFactoryBean> cronTriggerBeans = new ArrayList<>();
        for (String beanName : beanNames)
        {
            CronTriggerFactoryBean cronTriggerBean = null;
            Object object = quartzJobBeans.get(beanName);
            System.out.println(object);
            try {
                cronTriggerBean = this.buildCronTriggerBean(object);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(cronTriggerBean != null)
            {
                cronTriggerBeans.add(cronTriggerBean);
            }
        }
        return cronTriggerBeans;
    }

    public CronTriggerFactoryBean buildCronTriggerBean(Object job) throws Exception
    {
        CronTriggerFactoryBean cronTriggerBean = null;
        QuartzJob quartzJobAnnotation = AnnotationUtils.findAnnotation(job.getClass(), QuartzJob.class);
        if(Job.class.isAssignableFrom(job.getClass()))
        {
            System.out.println("It is a Quartz Job");
            cronTriggerBean = new CronTriggerFactoryBean();
            cronTriggerBean.setCronExpression(quartzJobAnnotation.cronExp());
            cronTriggerBean.setName(quartzJobAnnotation.name()+"_trigger");
            JobDetailFactoryBean jobDetail = new JobDetailFactoryBean();
            jobDetail.setName(quartzJobAnnotation.name());
            jobDetail.setJobClass(((Job)job).getClass());

            System.out.println("stop");
//            cronTriggerBean.setJobDetail(jobDetail);
        }
        else
        {
            throw new RuntimeException(job.getClass()+" doesn't implemented "+Job.class);
        }
        return cronTriggerBean;
    }

    protected void scheduleJobs(List<CronTriggerFactoryBean> cronTriggerBeans)
    {
        for (CronTriggerFactoryBean cronTriggerBean : cronTriggerBeans) {

            System.out.println("stop");
//            JobDetail jobDetail = cronTriggerBean.getJobDetail();
//            try {
//                scheduler.scheduleJob(jobDetail, cronTriggerBean);
//            } catch (SchedulerException e) {
//                e.printStackTrace();
//            }
        }
    }
}