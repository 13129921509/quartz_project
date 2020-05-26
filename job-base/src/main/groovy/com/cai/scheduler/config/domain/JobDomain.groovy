package com.cai.scheduler.config.domain

import com.cai.general.util.jackson.ConvertUtil
import com.cai.scheduler.config.core.EntityDefinition
import org.bson.Document

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class JobDomain implements Cloneable, Serializable{

    String group

    String name

    String cron

    String code

    String created = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)

    String createBy = 'api'


    static EntityDefinition define(Map data){
        return ConvertUtil.JSON.convertValue(data, EntityDefinition.class)
    }

}
