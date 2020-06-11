package com.cai.scheduler.config

import com.cai.general.core.Session
import com.cai.general.util.jackson.ConvertUtil
import com.cai.general.util.log.ErrorLogManager
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.mongo.service.MongoService
import com.cai.scheduler.config.contains.BaseMessage
import com.cai.scheduler.config.core.JobBeanService
import com.cai.scheduler.config.domain.JobDomain
import com.google.common.collect.Lists
import com.mongodb.client.FindIterable
import com.mongodb.client.result.DeleteResult
import org.bson.Document
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobKey
import org.quartz.Scheduler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

import java.text.MessageFormat

@Primary
@Service
abstract class BaseSchedulerService<T extends JobDomain> {

    Logger logger = LoggerFactory.getLogger(BaseSchedulerService.class)

    @Autowired
    MongoService mongoSvc

    @Autowired
    ActionBuilder builder

    @Autowired
    Scheduler scheduler

    @Value('${mongo.database:scheduler}')
    String database

    @Autowired
    JobBeanService jbSvc

    @Autowired
    ErrorLogManager errorLogManager

    ResponseMessage beforeInsertJob(T domain){
        return ResponseMessageFactory.success()
    }

    ResponseMessage updateOrInsertJob(T domain){
        ResponseMessage rsp
        try{
            rsp = beforeInsertJob(domain)
            if (!rsp.isSuccess)
                return rsp
            builder.addJob(domain, domain.entityDefinition.jobBean)
            rsp = afterInsertJob(domain)
            if (!rsp.isSuccess)
                return rsp
            jbSvc.insertJobBean(domain.entityDefinition.table, domain.entityDefinition.jobBean.name, domain.class.name)
            return ResponseMessageFactory.success()
        }catch(Throwable t){
            t.printStackTrace()

            return ResponseMessageFactory.error(t.message)
        }
    }

    ResponseMessage afterInsertJob(T domain){
        return ResponseMessageFactory.success()
    }

    boolean hasJob(T domain){
        return scheduler.checkExists(JobKey.jobKey(domain.name, domain.group))
    }

    boolean hasJob(String name, String group){
        return scheduler.checkExists(JobKey.jobKey(name, group))
    }

    ResponseMessage hasCurrentlyExecuting(T domain){
        if (hasJob(domain)){
            List<JobExecutionContext> contexts = scheduler.getCurrentlyExecutingJobs()
            contexts = contexts.findAll {it->
                it.getJobDetail().getKey() == JobKey.jobKey(domain.name, domain.group)
            }
            if (contexts)
                return ResponseMessageFactory.success(BaseMessage.GENERAL.JOB_GENERAL_MSG_0001,null, true)
        }
        return ResponseMessageFactory.error()
    }

    ResponseMessage deleteJob(Session sess, T domain){
        try{
            // 1.停止所有相关domain job
            stopJob(domain)
            // 2.删除库中存放数据
            Document filter = new Document()
            filter.append('name', domain.name)
            filter.append('group', domain.group)
            filter.append('code', domain.code)
            DeleteResult result = mongoSvc.delete(domain.entityDefinition.table as String, filter)
            if (result.deletedCount > 0)
                return ResponseMessageFactory.success()
            else
                return ResponseMessageFactory.error(MessageFormat.format(BaseMessage.ERROR.JOB_ERROR_MSG_0001, domain.code))
        }catch(Throwable t){
            errorLogManager.logException(sess, t)
        }
    }


    boolean stopJob(T domain){
        boolean res =  scheduler.deleteJob(JobKey.jobKey(domain.name,domain.group))
        if (res)
            logger.error(MessageFormat.format(BaseMessage.GENERAL.JOB_GENERAL_MSG_0002,domain.code))
        return res
    }

    List<T> getJobDomainsByName(String jobName, Class<T> clazz){
        Document filter = new Document()
        filter.append("name", jobName)
        FindIterable<Document> iterable = mongoSvc.find(clazz.DEFINE.table as String, filter)
        List<Document> results = Lists.newArrayList(iterable.hint())
        List<T> domains = []
        results.each {
            it.remove("_id")
            domains.add(ConvertUtil.JSON.convertValue(it, clazz))
        }
        return domains
    }
}
