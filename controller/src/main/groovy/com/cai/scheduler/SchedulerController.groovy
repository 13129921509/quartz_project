package com.cai.scheduler

import com.cai.scheduler.service.SchedulerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class SchedulerController {
    @Autowired
    SchedulerService schedulerService


}
