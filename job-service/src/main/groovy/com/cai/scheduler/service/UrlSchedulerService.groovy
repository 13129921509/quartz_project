package com.cai.scheduler.service

import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.mongo.service.MongoService
import com.cai.scheduler.config.ActionBuilder
import com.cai.scheduler.config.BaseSchedulerService
import com.cai.scheduler.config.domain.JobDomain
import com.cai.scheduler.config.job.UrlJob
import com.cai.scheduler.domain.UrlJobDomain
import org.quartz.Job
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import com.cai.general.util.jackson.ConvertUtil
@Service
class UrlSchedulerService extends BaseSchedulerService<UrlJobDomain>{

    Logger log = LoggerFactory.getLogger(UrlSchedulerService.class)

    @Override
    ResponseMessage beforeInsertJob(UrlJobDomain domain) {
        return ResponseMessageFactory.success("beforeInsertJob ${domain.url}")
    }

    @Override
    ResponseMessage insertJob(UrlJobDomain domain) {
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
        mongoSvc.insert(database, domain.DEFINE.table, res)
        log.info("new job : ${res.get('code')} created and runing")
    }
}


