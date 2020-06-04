package com.cai.scheduler.config

import com.cai.general.util.response.ResponseMessage
import com.cai.scheduler.config.domain.JobDomain
import org.quartz.Job
import org.quartz.JobDetail
import org.quartz.Trigger

abstract class BaseAction <T extends JobDomain>{

    abstract boolean addJob(JobDomain domain, Class<Job> jobClass)

    abstract ResponseMessage build(T domain, Class<Job> jobClass)

    abstract Trigger getTrigger(T domain)

    abstract JobDetail getJobDetail(T domain, Class<Job> job)
}