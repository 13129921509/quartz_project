package com.cai.scheduler.config

import com.cai.general.util.jackson.ConvertUtil
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.scheduler.config.contains.BaseMessage
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
            ResponseMessage rsp = build(domain, jobClass)
            if (!rsp.isSuccess){
                return false
            }
            return true
        }catch(Throwable t){
            t.printStackTrace()
            return false
        }
    }

    @Override
    ResponseMessage build(JobDomain domain, Class<Job> jobClass){
        try{
            JobDetail jobDetail = getJobDetail(domain,jobClass)
            Trigger trigger = getTrigger(domain)
            scheduler.scheduleJob(jobDetail, trigger)
            scheduler.start()
            return ResponseMessageFactory.success()
        }catch(Throwable t){
            t.printStackTrace()
            return ResponseMessageFactory.error(BaseMessage.ERROR.JOB_ERROR_MSG_0002)
        }
    }

    @Override
    JobDetail getJobDetail(JobDomain domain, Class<Job> jobClass){
        return JobBuilder
                .newJob(jobClass).withIdentity(domain.name, domain.group)
                .usingJobData('data', ConvertUtil.JSON.writeValueAsString(domain))
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
