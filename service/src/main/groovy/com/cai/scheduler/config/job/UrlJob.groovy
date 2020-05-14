package com.cai.scheduler.config.job

import com.cai.scheduler.config.domain.UrlJobDomain
import com.cai.scheduler.util.HttpUtil
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.springframework.stereotype.Component

/**
 * 统一采用post方式访问
 */
@Component
class UrlJob implements Job{

    @Override
    void execute(JobExecutionContext context) throws JobExecutionException {
        UrlJobDomain data = context.getJobDetail().getJobDataMap()
        HttpUtil.postToEntity(data.url, Object)
    }
}
