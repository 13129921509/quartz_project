package com.cai.scheduler.config.domain

import org.bson.Document

import java.time.LocalDateTime

class UrlJobDomain extends Document{

    String url

    String name

    String cron

    String code

    LocalDateTime created

    String createBy
}
