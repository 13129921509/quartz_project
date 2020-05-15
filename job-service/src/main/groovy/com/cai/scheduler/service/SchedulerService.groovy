package com.cai.scheduler.service

import com.cai.general.util.response.ResponseMessageFactory
import com.cai.mongo.service.MongoService
import com.cai.scheduler.config.SchedulerAction
import com.cai.scheduler.config.domain.UrlJobDomain
import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SchedulerService {

    private static ObjectMapper JSON = new ObjectMapper()
    private static ObjectMapper XML = new ObjectMapper()

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
            UrlJobDomain domain = convertResult(data, UrlJobDomain.class)
            result = sdAct.addJob(domain.code as String, domain.cron as String, domain.url as String)
            if (result)
                afterAddJob(domain)
            return ResponseMessageFactory.success()
        }catch(Throwable t){
            t.printStackTrace()
            return ResponseMessageFactory.error(t.message)
        }
    }

    void afterAddJob(Document domain){
        mongoSvc.insert(database, COLLECTION_NAME, domain)
        log.info("new job : ${domain.get('code')} created and runing")
    }

    def <O,T> T convertResult(O value, Class<T> type, FormatType format = FormatType.JSON){
        if (format == FormatType.JSON)
            return JSON.convertValue(value, type)
        else
            return XML.convertValue(value, type)
    }
}


enum FormatType{
    JSON, XML
}