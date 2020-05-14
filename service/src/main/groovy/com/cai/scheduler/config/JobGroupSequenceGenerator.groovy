package com.cai.scheduler.config

import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicInteger

class JobGroupSequenceGenerator {
    static AtomicInteger groupValue = new AtomicInteger(0)
    static AtomicInteger currentGroupSize = new AtomicInteger(0)
    static Object LOCK = new Object()

    static String nextGroup(){
        synchronized (LOCK){
            if (currentGroupSize.get() < 5){
                return groupValue.get() as String
            }
            else{
                groupValue.incrementAndGet()
                currentGroupSize.set(0)
                return groupValue.get() as String
            }
        }
    }
}
