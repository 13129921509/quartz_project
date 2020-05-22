package com.cai.scheduler.config

import com.cai.scheduler.config.domain.JobDomain
import org.quartz.Job
import org.quartz.JobDetail
import org.quartz.Trigger

abstract class BaseAction <T extends JobDomain>{

    abstract void build(T domain, Class<Job> jobClass)

    abstract Trigger getTrigger(T domain)

    abstract JobDetail getJobDetail(T domain, Class<Job> job)
}