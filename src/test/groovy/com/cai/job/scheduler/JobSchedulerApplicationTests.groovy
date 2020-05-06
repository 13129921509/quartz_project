package com.cai.job.scheduler

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
    void simpleJobTest(){

        JobDetail job = JobBuilder
                .newJob(SimpleJob.class)
                .withIdentity("job1","group1")
                .build()

        Date runTime = Date.from(LocalDateTime.now().plusSeconds(30).atZone(ZoneId.systemDefault()).toInstant())


        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("job1","group1").startAt(runTime).build()

        scheduler.scheduleJob(job,trigger)

        scheduler.start()


        Thread.sleep(90L * 1000L);
    }


    @Test
    void cronTest1(){
        before()
        JobDetail job = JobBuilder
                .newJob(SimpleJob.class)
                .withIdentity("job1","group1")
                .build()

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("job1","group1")
                .withSchedule(cronSchedule("3/3 * * * * ?"))
                .build()
        scheduler.scheduleJob(job,trigger)

        scheduler.start()

        Thread.sleep(90L * 1000L);
    }

}
