package com.cai.scheduler.config

import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.mongo.service.MongoService
import com.cai.scheduler.config.domain.JobDomain
import org.quartz.Job
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

    @Value('${mongo.database:scheduler}')
    String database

    abstract ResponseMessage beforeInsertJob(T domain)

    abstract ResponseMessage insertJob(T domain)

    abstract ResponseMessage afterInsertJob(T domain)
}
