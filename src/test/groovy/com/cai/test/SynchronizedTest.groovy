package com.cai.test

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import java.util.concurrent.Callable
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Future
import java.util.concurrent.FutureTask
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.Semaphore
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.stream.IntStream

/**
 * 闭锁相关的一些测试
 * #CountDownLatch
 * #Semaphore
 */
@RunWith(SpringJUnit4ClassRunner)
class SynchronizedTest {
    @Test
    void latch(){

    }

    @Test
    void semaphoreTest(){
        CountDownLatch releaseLatch = new CountDownLatch(1)
        BoundSizeSet<String> bset = new BoundSizeSet<>(10)
        ThreadPoolExecutor executor = new ThreadPoolExecutor(15,15,Long.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>())
        Set<Future<Boolean>> futureSet = []
        IntStream.range(0,15).forEach{it->
            FutureTask<Boolean> task = executor.submit(new Callable<Boolean>() {
                @Override
                Boolean call() throws Exception {
                    return bset.add(it as String)
                }
            })
            futureSet.add(task)
            if (it == 14)
                releaseLatch.countDown()
        }
        releaseLatch.await()
        IntStream.range(0,5).forEach{it->
            executor.execute(new Runnable() {
                @Override
                void run() {
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
                return false
            }
            pset.add(val)
            return true
        }catch(InterruptedException e){
            e.printStackTrace()
            return false
        }
    }

    boolean remove(T val){
        try{
            pset.remove(val)
            semaphore.release()
            return true
        }catch(InterruptedException e){
            e.printStackTrace()
            return false
        }
    }
}