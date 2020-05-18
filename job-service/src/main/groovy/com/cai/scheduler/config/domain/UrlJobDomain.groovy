package com.cai.scheduler.config.domain

import org.bson.Document

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UrlJobDomain implements Cloneable, Serializable{

    String url

    String name

    String cron

    String code

    String created = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)

    String createBy = 'api'
}
