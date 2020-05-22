package com.cai.scheduler.config

import com.cai.general.util.jackson.ConvertUtil
import com.cai.scheduler.config.domain.JobDomain
import org.quartz.CronScheduleBuilder
import org.quartz.Job
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.springframework.beans.BeansException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.stereotype.Component

import javax.annotation.Resource

@Component
class ActionBuilder extends BaseAction<JobDomain>{

    @Autowired
    Scheduler scheduler

    boolean addJob(JobDomain domain, Class<Job> jobClass) {
        try{
            domain.group = JobGroupSequenceGenerator.nextGroup()
            build(domain, jobClass)
            return true
        }catch(Throwable t){
            t.printStackTrace()
            return false
        }
    }

    @Override
    void build(JobDomain domain, Class<Job> jobClass){
        JobDetail jobDetail = getJobDetail(domain,jobClass)
        Trigger trigger = getTrigger(domain)
        scheduler.scheduleJob(jobDetail, trigger)
        scheduler.start()
    }

    @Override
    JobDetail getJobDetail(JobDomain domain, Class<Job> jobClass){
        return JobBuilder
                .newJob(jobClass).withIdentity(domain.name, domain.group)
                .usingJobData('data', ConvertUtil.JSON.writeValueAsString(domain.data))
                .build()
    }

    @Override
    Trigger getTrigger(JobDomain domain){
        return TriggerBuilder
                .newTrigger()
                .withIdentity(domain.name,domain.group)
                .withSchedule(CronScheduleBuilder.cronSchedule(domain.cron))
                .build()
    }
}
