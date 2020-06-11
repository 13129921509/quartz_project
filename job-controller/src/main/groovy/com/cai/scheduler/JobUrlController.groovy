package com.cai.scheduler

import com.cai.general.core.BaseController
import com.cai.general.core.Session
import com.cai.general.util.jackson.ConvertUtil
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.scheduler.config.domain.JobDomain
import com.cai.scheduler.config.job.UrlJob
import com.cai.scheduler.domain.UrlJobDomain
import com.cai.scheduler.service.UrlSchedulerService
import javafx.scene.chart.ValueAxis
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("scheduler/api/job/url/")
class JobUrlController extends BaseController{
    @Autowired
    UrlSchedulerService schedulerService

    @RequestMapping(value = '/add', method = RequestMethod.POST)
    ResponseMessage addJob(@RequestBody Map data){
        UrlJobDomain domain = ConvertUtil.JSON.convertValue(data, UrlJobDomain.class)
        return schedulerService.updateOrInsertJob(domain)
    }

    @RequestMapping(method = RequestMethod.GET, value = '/test')
    ResponseMessage test(){
        return ResponseMessageFactory.success()
    }

    @GetMapping(value = "/exist/{jobName}")
    ResponseMessage existJob(@PathVariable String jobName){
        return schedulerService.hasExecutingJob(jobName)
    }

    @DeleteMapping(value = "/delete/{jobName}")
    ResponseMessage deleteJob(HttpServletRequest request, @PathVariable String jobName){
        Session sess = getSession(request)
        return schedulerService.stopAndRemoveJob(sess, jobName)
    }

    @GetMapping(value = "/stop/{jobName}")
    ResponseMessage stopJob(@PathVariable String jobName){
        return schedulerService.stopAndDeadJob(jobName)
    }

}
