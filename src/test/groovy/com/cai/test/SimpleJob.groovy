package com.cai.test

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SimpleJob implements Job {

    @Override
    void execute(JobExecutionContext context) throws JobExecutionException {
//        println context.getJobDetail().get
        println LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}
