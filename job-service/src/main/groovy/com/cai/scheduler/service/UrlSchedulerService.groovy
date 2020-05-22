package com.cai.scheduler.service

import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.mongo.service.MongoService
import com.cai.scheduler.config.ActionBuilder
import com.cai.scheduler.config.BaseSchedulerService
import com.cai.scheduler.config.domain.JobDomain
import org.quartz.Job
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import com.cai.general.util.jackson.ConvertUtil
@Service
class UrlSchedulerService extends BaseSchedulerService{

    @Autowired
    MongoService mongoSvc

    @Autowired
    ActionBuilder builder

    @Value('${mongo.database:scheduler}')
    String database

    static String COLLECTION_NAME = 'job_list'

    Logger log = LoggerFactory.getLogger(UrlSchedulerService.class)

    @Override
    ResponseMessage beforeInsert() {
        return ResponseMessageFactory.error(null)
    }

    @Override
    def <T extends Class<Job>> ResponseMessage insertJob(Map data, T clazz){
        try{
            boolean result = false
            JobDomain domain = ConvertUtil.convertValue(data, JobDomain.class)
            result = builder.addJob(domain, clazz)
            if (result)
                afterAddJob(domain)
            return ResponseMessageFactory.success()
        }catch(Throwable t){
            t.printStackTrace()
            return ResponseMessageFactory.error(t.message)
        }
    }

    def <T> void afterAddJob(T domain){
        Map res = ConvertUtil.JSON.convertValue(domain, Map)
        mongoSvc.insert(database, COLLECTION_NAME, res)
        log.info("new job : ${res.get('code')} created and runing")
    }
}


