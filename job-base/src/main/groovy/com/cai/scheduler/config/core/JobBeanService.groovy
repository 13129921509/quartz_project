package com.cai.scheduler.config.core

import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.mongo.service.MongoService
import com.cai.scheduler.config.domain.JobBean
import com.cai.scheduler.config.domain.JobDomain
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import javax.xml.ws.Response

@Component
class JobBeanService {

    @Value('${mongo.contains.jobListName:job_list}')
    String jobListName

    @Autowired
    MongoService mongoSvc

    List<JobBean> findJobBeans(){
        List<JobBean> jobs = mongoSvc.findList(jobListName, new Document())
        return jobs
    }

    ResponseMessage insertJobBean(String jobDomain, String jobClazz, String domainClazz){
        Document filter = new Document()
        filter.append('name', jobDomain)
        List<Document> lists = mongoSvc.findList(jobListName, filter)
        if (lists.size() == 0){
            JobBean jobBean = new JobBean()
            jobBean.append('name', jobDomain)
            jobBean.append('jobClazz', jobClazz)
            jobBean.append('domainClazz', domainClazz)
            mongoSvc.insert(jobListName, jobBean)
        }
        return ResponseMessageFactory.success()
    }
}
