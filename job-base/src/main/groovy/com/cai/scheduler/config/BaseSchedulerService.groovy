package com.cai.scheduler.config

import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import org.quartz.Job
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Primary
@Service
abstract class BaseSchedulerService {

    ResponseMessage beforeInsert(){
        return ResponseMessageFactory.success()
    }

    def <T extends Class<Job>> ResponseMessage insertJob(Map data, T clazz){
        if (!beforeInsert().isSuccess)
            return
        insertJob(data,clazz)
        return ResponseMessageFactory.success()
    }
}
