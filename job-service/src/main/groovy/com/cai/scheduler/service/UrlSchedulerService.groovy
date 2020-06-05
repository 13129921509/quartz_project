package com.cai.scheduler.service

import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.mongo.service.MongoService
import com.cai.scheduler.config.ActionBuilder
import com.cai.scheduler.config.BaseSchedulerService
import com.cai.scheduler.config.contains.BaseMessage
import com.cai.scheduler.config.domain.JobDomain
import com.cai.scheduler.config.job.UrlJob
import com.cai.scheduler.domain.UrlJobDomain
import com.google.common.collect.Lists
import com.mongodb.client.FindIterable
import org.bson.Document
import org.quartz.Job
import org.quartz.JobKey
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

import java.text.MessageFormat

import static org.springframework.data.mongodb.core.query.Criteria.*
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import com.cai.general.util.jackson.ConvertUtil


@Service
class UrlSchedulerService extends BaseSchedulerService<UrlJobDomain>{

    Logger log = LoggerFactory.getLogger(UrlSchedulerService.class)

    @Override
    ResponseMessage beforeInsertJob(UrlJobDomain domain) {
        // 此处若是update，则停止现有相关domain job
        try{
            if (hasCurrentlyExecuting(domain).isSuccess){
                scheduler.deleteJob(JobKey.jobKey(domain.name, domain.group))
            }
            return ResponseMessageFactory.success()
        }catch(Throwable t){
            t.printStackTrace()
            return ResponseMessageFactory.error(BaseMessage.ERROR.JOB_ERROR_MSG_0000)
        }
    }

    @Deprecated
    ResponseMessage insertJobA(UrlJobDomain domain) {
        try{
            builder.addJob(domain, UrlJob.class)
            return ResponseMessageFactory.success()
        }catch(Throwable t){
            t.printStackTrace()
            return ResponseMessageFactory.error(t.message)
        }
    }

    @Override
    ResponseMessage afterInsertJob(UrlJobDomain domain){
        Map res = ConvertUtil.JSON.convertValue(domain, Map)
        Document filter = new Document()
        filter.append('code' , domain.code)
        List<Document> lists =  mongoSvc.findList(domain.DEFINE.table, filter)
        if (lists.size() == 0)
            mongoSvc.insert(domain.DEFINE.table, res)
        else
            mongoSvc.updateMany(
                    domain.DEFINE.table
                    , new Query(
                        where('code').is(domain.code)
                    )
                    , Update.fromDocument(ConvertUtil.JSON.convertValue(domain, Document))
            )
        log.info("new job : ${res.get('code')} created and runing")
        return ResponseMessageFactory.success()
    }

    /**
     * 根据jobName，返回当前运行job中是否存在对应job
     * @param jobName
     * @return
     */
    ResponseMessage hasExecutingJob(String jobName){
        List<UrlJobDomain> results = getJobDomainsByName(jobName, UrlJobDomain)
        if (results.size() > 0)
            return this.hasCurrentlyExecuting(results[0])
        else
            return ResponseMessageFactory.success(false)
    }

    ResponseMessage stopAndRemoveJob(String jobName){
        List<UrlJobDomain> domains = getJobDomainsByName(jobName, UrlJobDomain)
        if (domains.size() > 0)
            return deleteJob(domains[0])
        else
            return ResponseMessageFactory.error(MessageFormat.format(BaseMessage.ERROR.JOB_ERROR_MSG_0003,jobName))
    }

    ResponseMessage stopAndDeadJob(String jobName){
        try{
            List<UrlJobDomain> domains = getJobDomainsByName(jobName, UrlJobDomain)
            domains.each {
                stopJob(it)
                mongoSvc.updateMany(
                        it.DEFINE.table,
                        Query.query(
                                where('name').is(it.name)
                                        .and('code').is(it.code)
                                        .and('group').is(it.group)
                        ),
                        Update.update('isAlive',false)
                )
            }
            return ResponseMessageFactory.success()
        }catch(Throwable t){
            t.printStackTrace()
            return ResponseMessageFactory.error(BaseMessage.ERROR.JOB_ERROR_MSG_0000)
        }

    }



}


