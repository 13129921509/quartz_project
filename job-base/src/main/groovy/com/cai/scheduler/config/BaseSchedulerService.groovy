package com.cai.scheduler.config

import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.mongo.service.MongoService
import com.cai.scheduler.config.core.JobBeanService
import com.cai.scheduler.config.domain.JobDomain
import org.quartz.Job
import org.quartz.Scheduler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Primary
@Service
abstract class BaseSchedulerService<T extends JobDomain> {


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

    ResponseMessage beforeInsertJob(T domain){
        return ResponseMessageFactory.success()
    }

    ResponseMessage insertJob(T domain){
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
        scheduler.getCurrentlyExecutingJobs()
    }
}
