package com.cai.scheduler.config

import com.cai.scheduler.config.domain.UrlJobDomain
import com.cai.scheduler.config.job.UrlJob
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SchedulerActionImp implements SchedulerAction{

    @Autowired
    Scheduler scheduler

    @Override
    boolean addJob(String name, String cron, String url) {
        try{
            String group = JobGroupSequenceGenerator.nextGroup()
            build(name, group, cron, url)
            return true
        }catch(Throwable t){
            t.printStackTrace()
            return false
        }
    }

    void build(String name, String group, String cron, String url){
        UrlJobDomain data = new UrlJobDomain()
        data.url = url
        JobDetail jobDetail = getJobDetail(name, group, data)
        Trigger trigger = getTrigger(name, group, cron)
        scheduler.scheduleJob(jobDetail, trigger)
        scheduler.start()
    }

    JobDetail getJobDetail(String name ,String group, UrlJobDomain data){
        return JobBuilder
                .newJob(UrlJob).withIdentity(name, group)
                .usingJobData('data',data)
                .build()
    }

    Trigger getTrigger(String name ,String group, String cron){
        return TriggerBuilder
                .newTrigger()
                .withIdentity(name,group)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build()
    }
}
