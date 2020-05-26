package com.cai.scheduler

import com.cai.general.util.jackson.ConvertUtil
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.scheduler.config.domain.JobDomain
import com.cai.scheduler.config.job.UrlJob
import com.cai.scheduler.domain.UrlJobDomain
import com.cai.scheduler.service.UrlSchedulerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("scheduler/api/job/url/")
class JobUrlController {
    @Autowired
    UrlSchedulerService schedulerService

    @RequestMapping(value = '/add', method = RequestMethod.POST)
    ResponseMessage addJob(@RequestBody Map data){
        UrlJobDomain domain = ConvertUtil.JSON.convertValue(data, UrlJobDomain.class)
        return schedulerService.insertJob(domain)
    }

    @RequestMapping(method = RequestMethod.GET, value = '/test')
    ResponseMessage test(){
        return ResponseMessageFactory.success()
    }

}
