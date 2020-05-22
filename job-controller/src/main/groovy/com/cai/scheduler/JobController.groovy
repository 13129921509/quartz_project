package com.cai.scheduler

import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.scheduler.config.job.UrlJob
import com.cai.scheduler.service.UrlSchedulerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("scheduler/api/job")
class JobController {
    @Autowired
    UrlSchedulerService schedulerService

    @RequestMapping(value = '/add', method = RequestMethod.POST)
    ResponseMessage addJob(@RequestBody Map data){
        return schedulerService.insertJob(data, UrlJob.class)
    }

    @RequestMapping(method = RequestMethod.GET, value = '/test')
    ResponseMessage test(){
        return ResponseMessageFactory.success()
    }

}
