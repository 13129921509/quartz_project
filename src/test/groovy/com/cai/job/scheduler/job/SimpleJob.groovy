package com.cai.job.scheduler.job

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicInteger

class SimpleJob implements Job {

    @Override
    void execute(JobExecutionContext context) throws JobExecutionException {
//        println context.getJobDetail().get
        println LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}
