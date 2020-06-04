package com.cai.scheduler.domain

import com.cai.scheduler.config.domain.JobDomain
import com.cai.scheduler.config.job.UrlJob

class UrlJobDomain extends JobDomain{

    static DEFINE = define([
            "table" : "url_job_list",
            "jobBean" : UrlJob
    ])

    String url

}
