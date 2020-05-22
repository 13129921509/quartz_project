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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ActionBuilder extends BaseAction<JobDomain>{

    static ActionBuilder action = new ActionBuilder()

    @Autowired
    Scheduler scheduler

    static boolean addJob(JobDomain domain, Class<Job> jobClass) {
        try{
            domain.group = JobGroupSequenceGenerator.nextGroup()
            action.build(domain)
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
                .usingJobData('data', ConvertUtil.JSON.writeValueAsString(domain.url))
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
