package com.cai

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class JobSchedulerApplication {

    static void main(String[] args) {
        SpringApplication.run(JobSchedulerApplication, args)
    }

}
