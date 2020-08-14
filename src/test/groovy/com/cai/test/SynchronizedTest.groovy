package com.cai.test

import ch.qos.logback.core.util.TimeUtil
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import java.util.concurrent.Callable
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CountDownLatch
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Future
import java.util.concurrent.FutureTask
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.Semaphore
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import java.util.stream.IntStream

/**
 * 并发相关的一些测试
 * #CountDownLatch
 * #Semaphore
 * #FutureTask
 * #CycleBarrier
 * #ThreadPoolExecutor
 * #自旋
 * #ReentrantLock
 */
@RunWith(SpringJUnit4ClassRunner)
class SynchronizedTest {
    @Test
    void latch(){

    }

    @Test
    void semaphoreTest(){
//        AtomicInteger countDownState = new AtomicInteger(0)
        CountDownLatch releaseLatch = new CountDownLatch(1)
        CyclicBarrier threeBarrier = new CyclicBarrier(3, new Runnable() {
            @Override
            void run() {
                println "-----------已放出批次------"
            }
        })
        // 我尼玛为啥用FutureTask 还他么上CycleBarrier，脑残吧
        BoundSizeSet<String> bset = new BoundSizeSet<>(10)
        ThreadPoolExecutor executor = new ThreadPoolExecutor(15,15,Long.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>())
        Set<Future<Boolean>> futureSet = []
        IntStream.range(0,15).forEach{it->
            FutureTask<Boolean> task = executor.submit(new Callable<Boolean>() {
                @Override
                Boolean call() throws Exception {
                    try{
                        threeBarrier.await()
                        println "$it 正在添加----"
                        return bset.add(it as String)
                    }finally{
                        if (it == 14){
                            releaseLatch.countDown()
                        }
                    }
                }
            })
            futureSet.add(task)
        }
        println "先堵住，不要消费----"
        releaseLatch.await()
//        while (countDownState.get() != 15){ // 此地方不能准确在子线程内部全部执行完毕时判断是否可以消费，需要标志位
//            //自旋
//        }
        println "可以开始消费了----"
        IntStream.range(0,5).forEach{it->
            executor.execute(new Runnable() {
                @Override
                void run() {
                    println "$it 正在消费----"
                    bset.remove(it as String)
                }
            })
            null
        }
        printResult(futureSet)
        null


    }

    void printResult(Set<Future<Boolean>> sets){
        CopyOnWriteArrayList<Future<Boolean>> list = new CopyOnWriteArrayList<>(sets)
        while (!list.empty){
            list.each {task->
                if (task.isDone()){
                    println "${Thread.currentThread().name}  -- ${task.get()}"
                    list.remove(task)
                }
            }
        }

    }

    /**
     * 采用闭包形式使用并发lock 内部使用ReentrantLock
     */
    @Test
    void customerLock(){
        CustomerLock lock = CustomerLock.generate(5, TimeUnit.SECONDS)
        Thread[] ts = new Thread[3]

        for (int i = 0 ; i < 3 ; i++){
            ts[i] = new Thread(new Runnable() {
                @Override
                void run() {
                    lock.lock {
                        println "${Thread.currentThread().name} lock!!!"
                        Thread.sleep(10000L)
                    }
                }
            })
            ts[i].start()
        }
        for (int i = 0 ; i < 3 ; i++){
            ts[i].join()
        }
    }
}


class BoundSizeSet<T> {

    private int size = 1

    private Set<T> pset= new HashSet<T> ()

    private Semaphore semaphore

    BoundSizeSet(Integer size){
        this.size = size
        init()
    }

    private void init(){
        semaphore = new Semaphore(size)
    }

    boolean add(T val){
        try{
            if (!semaphore.tryAcquire(5L, TimeUnit.SECONDS)){
                println "error | cause: 尝试获取资源到规定时间，依旧无法获取, params: $val"
                return false
            }
            Thread.sleep(3000L)
            pset.add(val)
            return true
        }catch(InterruptedException e){
            e.printStackTrace()
            return false
        }
    }

    boolean remove(T val){
        try{
            boolean res = pset.remove(val)
            semaphore.release()
            return res
        }catch(InterruptedException e){
            e.printStackTrace()
            return false
        }
    }
}

class CustomerLock{

    private ReentrantLock lock = new ReentrantLock(false)

    private TimeUnit timeUnit

    private long time

    CustomerLock(TimeUnit timeUnit, Long time) {
        this.timeUnit = timeUnit
        this.time = time
    }

    static CustomerLock generate(Long time, TimeUnit util){
        return new CustomerLock(util, time)
    }
    void lock(Closure closure) {
        try {
            if (lock.tryLock(time, timeUnit)) {
                closure.call()
            }else{
                throw new IllegalStateException("check error!!!")
            }
        }catch(Throwable t) {
            t.printStackTrace()
        }finally {
            lock.unlock()
        }
    }

}