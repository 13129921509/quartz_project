package com.cai.job.scheduler

import com.cai.job.scheduler.job.ColorJob
import com.cai.job.scheduler.job.SimpleJob
import org.junit.jupiter.api.Test
import org.quartz.*
import org.quartz.impl.StdSchedulerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import java.time.LocalDateTime
import java.time.ZoneId

import static org.quartz.CronScheduleBuilder.cronSchedule
@SpringBootTest
class JobSchedulerApplicationTests {

    @Test
    void contextLoads() {
    }

    SchedulerFactory sf
    Scheduler scheduler

    void before(){
        sf = new StdSchedulerFactory();
        scheduler = sf.getScheduler();
    }

    @Test
    void cronConcurrent1Test(){
        before()

        JobDetail job = JobBuilder
                .newJob(ColorJob.class)
                .withIdentity("job1","group1")
                .build()

        job.jobDataMap.put(ColorJob.COLOR, 'red')
        job.jobDataMap.put(ColorJob.COUNT_VALUE, 1)
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("job1","group1")
                .withSchedule(cronSchedule("3/3 * * * * ?"))
                .build()
        scheduler.scheduleJob(job,trigger)

        scheduler.start()

        Thread.sleep(1000000)

        scheduler.shutdown()
        SchedulerMetaData metaData = scheduler.getMetaData()
        println "Executed " + metaData.getNumberOfJobsExecuted() + " jobs."
    }

    @Test
    void cronConcurrent2Test(){
        before()


        JobDetail job = JobBuilder
                .newJob(ColorJob.class)
                .withIdentity("job2","group1")
                .build()

        job.jobDataMap.put(ColorJob.COLOR, 'green')
        job.jobDataMap.put(ColorJob.COUNT_VALUE, 1)

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("job1","group1")
                .withSchedule(cronSchedule("3/4 * * * * ?"))
                .build()
        scheduler.scheduleJob(job,trigger)

        scheduler.start()

        Thread.sleep(10000000)

        scheduler.shutdown()
        SchedulerMetaData metaData = scheduler.getMetaData()
        println "Executed " + metaData.getNumberOfJobsExecuted() + " jobs."

    }
}
