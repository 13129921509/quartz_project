package com.cai.scheduler.service

import com.cai.general.util.http.HttpUtil
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.mongo.service.MongoService
import com.cai.scheduler.config.SchedulerAction
import com.cai.scheduler.config.domain.UrlJobDomain
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.bson.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import com.cai.general.util.jackson.ConvertUtil
@Service
class SchedulerService {

    @Autowired
    MongoService mongoSvc

    @Autowired
    SchedulerAction sdAct

    @Value('${mongo.database:scheduler}')
    String database

    static String COLLECTION_NAME = 'job_list'

    Logger log = LoggerFactory.getLogger(SchedulerService.class)

    def addJob(Map data){
        try{
            boolean result = false
            UrlJobDomain domain = ConvertUtil.convertValue(data, UrlJobDomain.class)
            result = sdAct.addJob(domain.code as String, domain.cron as String, domain.url as String)
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


