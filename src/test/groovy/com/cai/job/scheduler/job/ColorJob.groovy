package com.cai.job.scheduler.job

import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobDataMap
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.quartz.PersistJobDataAfterExecution

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
class ColorJob implements Job{
    static final String COUNT_VALUE = 'counter';

    static final String COLOR = 'color'

    static int count = 0

    @Override
    void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap()
        count++
        println """
                $COUNT_VALUE : ${dataMap.getInt(ColorJob.COUNT_VALUE)} \n
                $COLOR : ${dataMap.getString(ColorJob.COLOR)} \n
                $count
            """
        dataMap.put(ColorJob.COUNT_VALUE, ++dataMap.getInt(ColorJob.COUNT_VALUE))
    }
}
