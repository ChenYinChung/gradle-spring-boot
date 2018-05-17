package com.sb.schedule;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.quartz.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;


public class QuartJobSchedulingListener implements ApplicationListener<ContextRefreshedEvent> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String QUARTZ_PROPERTIES_PATH = "/quartz.properties";
//    @Autowired
//    private Scheduler scheduler;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            ApplicationContext applicationContext = event.getApplicationContext();
            loadAndRunQuartzJob(applicationContext);
        } catch (Exception e) {
            logger.error("startup error",e);
            System.exit(0);
        }
    }

    private void loadAndRunQuartzJob(ApplicationContext applicationContext) throws Exception {

        Map<String, Object> quartzJobBeans = applicationContext.getBeansWithAnnotation(QuartzJob.class);
        Set<String> beanNames = quartzJobBeans.keySet();
        SchedulerFactoryBean schedulerFactoryBean =  buildSchedulerFactoryBean();
        schedulerFactoryBean.afterPropertiesSet();

        beanNames.forEach(t -> {
            QuartzJobBean job = (QuartzJobBean) quartzJobBeans.get(t);
            if (Job.class.isAssignableFrom(job.getClass())) {
                try {
                    CronTriggerFactoryBean cronTriggerFactoryBean = buildCronTriggerFactoryBean(job);
                    JobDetailFactoryBean jobDetailFactoryBean = buidlJobDetailFactoryBean(job);
                    jobDetailFactoryBean.setApplicationContext(applicationContext);
                    jobDetailFactoryBean.afterPropertiesSet();

                    cronTriggerFactoryBean.setJobDetail(jobDetailFactoryBean.getObject());
                    cronTriggerFactoryBean.afterPropertiesSet();

                    if(!schedulerFactoryBean.getObject().checkExists(jobDetailFactoryBean.getObject().getKey())){
                        schedulerFactoryBean.getObject().scheduleJob(jobDetailFactoryBean.getObject(), cronTriggerFactoryBean.getObject());
                    }

                    schedulerFactoryBean.getObject().start();
                } catch (ParseException |SchedulerException pe) {
                    logger.error("CronTriggerFactoryBean error",pe);
                }

            }
        });
    }


    private CronTriggerFactoryBean buildCronTriggerFactoryBean(QuartzJobBean job) {

        CronTriggerFactoryBean cronTriggerFactoryBean = null;
        QuartzJob quartzJobAnnotation = AnnotationUtils.findAnnotation(job.getClass(), QuartzJob.class);
        cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setCronExpression(quartzJobAnnotation.cronExp());
        cronTriggerFactoryBean.setName(quartzJobAnnotation.name() + "_trigger");
        cronTriggerFactoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
        return cronTriggerFactoryBean;
    }

    private JobDetailFactoryBean buidlJobDetailFactoryBean(QuartzJobBean job) {
        QuartzJob quartzJobAnnotation = AnnotationUtils.findAnnotation(job.getClass(), QuartzJob.class);
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setName(quartzJobAnnotation.name());
        jobDetailFactoryBean.setJobClass(job.getClass());
        jobDetailFactoryBean.setDurability(true);
        jobDetailFactoryBean.setBeanName(job.getClass().getName());

        return jobDetailFactoryBean;
    }

    private SchedulerFactoryBean buildSchedulerFactoryBean() throws IOException {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        return schedulerFactoryBean;
    }


    private Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource(QUARTZ_PROPERTIES_PATH));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }


//    protected void scheduleJobs(JobDetail jobDetail, Trigger trigger) {
//
//        try {
//
//            scheduler.scheduleJob(jobDetail, trigger);
//        } catch (SchedulerException e) {
//            logger.error("Scheduler Job error", e);
//        }
//    }
}