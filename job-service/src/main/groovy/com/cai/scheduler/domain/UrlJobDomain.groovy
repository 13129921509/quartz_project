package com.cai.scheduler.domain

import com.cai.scheduler.config.domain.JobDomain

class UrlJobDomain extends JobDomain{

    static DEFINE = define([
            "table" : "url_job_list"
    ])

    String url

}
