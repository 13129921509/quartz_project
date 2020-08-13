package com.cai.test

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReferenceArray
import java.util.function.IntBinaryOperator
import java.util.stream.IntStream

@RunWith(SpringJUnit4ClassRunner)
class ParallelTest {

    @Test
    void collectTest(){
        AtomicInteger times = new AtomicInteger(0)
        AtomicReferenceArray<TestConsumer> consumers = new AtomicReferenceArray<TestConsumer>(11)

//        IntStream.rangeClosed(0, 10).mapToObj{
//            return new TestConsumer(it)
//        }.parallel().forEach{ TestConsumer it->
//            println it.getLog()
//        }

        List<TestConsumer> res = IntStream.rangeClosed(0, 10).parallel().collect(
        {
            return new ArrayList<TestConsumer>()
        },{List<TestConsumer> consumerList, Integer i->
            TestConsumer consumer = new TestConsumer(i)
            consumer.getLog()
            consumerList.add(consumer)
            consumers.set(i , consumer)
        },{List<TestConsumer> l1,List<TestConsumer> l2->
            l1.addAll(l2)
        })
        println "times: $times"
        println "consumers: ${consumers.length()}"
        println res.parallelStream().collect {it.log}.join("\n")
//        println logs.size()
//        println logs.unique().size()

    }

    @Test
    void reduceTest(){
        int i = 0
        OptionalInt res = IntStream.generate({ i++ }).limit(1000).reduce{a,b->
            a + b
        }
        println res.getAsInt()
        i = 0
        int ires = IntStream.generate({ i++ }).limit(1000).reduce(1, new IntBinaryOperator(){

            @Override
            int applyAsInt(int left, int right) {
                return left + right
            }
        })
        println ires
        ires = IntStream.generate({ i++ }).limit(1000)
    }


    class TestConsumer{
        private String log = ""

        Integer val

        TestConsumer(Integer val) {
            this.val = val
        }

        TestConsumer(){}

        void setVal(Integer val){
            this.val = val
        }
        void work(){
            sleep(1000L)
            log = "${Thread.currentThread().name} -------- $val"
            println log
        }

        String getLog(){
            work()
            return log
        }
    }
}
