package com.cai.scheduler.config

import com.cai.mongo.config.MongoConfiguration
import org.quartz.Scheduler
import org.quartz.SchedulerFactory
import org.quartz.impl.StdSchedulerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Scope
import org.springframework.core.Ordered
import org.springframework.web.client.RestTemplate

@Import(MongoConfiguration)
@Configuration
class SchedulerConfig implements Ordered{

    @Bean
    @Scope('singleton')
    SchedulerFactory schedulerFactory(){
        return new StdSchedulerFactory()
    }

    @Bean
    Scheduler scheduler(SchedulerFactory schedulerFactory){
        return schedulerFactory.getScheduler()
    }

    @Override
    int getOrder() {
        return Ordered.LOWEST_PRECEDENCE
    }
}
