package com.cai.scheduler.config.core

import com.cai.general.util.jackson.ConvertUtil
import com.cai.general.util.response.ResponseMessage
import com.cai.mongo.service.MongoService
import com.cai.scheduler.config.ActionBuilder
import com.cai.scheduler.config.domain.JobBean
import com.cai.scheduler.config.domain.JobDomain
import org.bson.Document
import org.quartz.Job
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class JobNotifyListener implements ApplicationListener<ApplicationStartedEvent>{

    Logger logger = LoggerFactory.getLogger(JobNotifyListener)

    @Autowired
    MongoService mongoSvc

    @Autowired
    JobBeanService jbSvc

    @Autowired
    ActionBuilder actionBuilder

    @Override
    void onApplicationEvent(ApplicationStartedEvent event) {
        ResponseMessage rsp
        List<JobBean> jobs = jbSvc.findJobBeans()
        jobs.each {job->
            List<Document> domains = mongoSvc.findList(job.name as String, new Document())
            domains.each {doc->
                Class<JobDomain> domainClazz = Class.forName(job.domainClazz as String)
                doc.remove("_id")
                JobDomain domain = ConvertUtil.JSON.convertValue(doc, domainClazz)
                rsp = actionBuilder.build(domain, Class.forName(job.jobClazz as String)  as Class<Job>)
                if (rsp.isSuccess)
                    logger.error("-------启动 ${job.name}:${domain.code} JOB")
                else
                    logger.error("-------未启动 ${job.name}:${domain.code} JOB")
            }
        }
    }
}
