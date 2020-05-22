package com.cai.scheduler.config.job

import com.cai.general.util.http.HttpUtil
import com.cai.general.util.jackson.ConvertUtil
import com.cai.scheduler.config.domain.JobDomain
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * 统一采用post方式访问
 */
@Component
class UrlJob implements Job{

    void execute(JobExecutionContext context) throws JobExecutionException {
        Map data = ConvertUtil.JSON.readValue(context.getJobDetail().getJobDataMap().get('data'), Map.class)
        HttpUtil.postToEntity(data.url, Object)
    }
}
