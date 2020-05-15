package com.cai.scheduler

import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.scheduler.service.SchedulerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController("scheduler/api")
class SchedulerController {
    @Autowired
    SchedulerService schedulerService

    @PostMapping('/add')
    ResponseMessage addJob(@RequestBody Map data){
        return schedulerService.addJob(data)
    }

    @RequestMapping(method = RequestMethod.GET, value = '/test')
    ResponseMessage test(){
        return ResponseMessageFactory.success()
    }

}
