package com.cai.scheduler.config.aspect

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

import java.lang.reflect.Method

//@Aspect
@Component
class JobServiceAspect {

//    Logger logger = LoggerFactory.getLogger(JobServiceAspect.class)
//
//    @Pointcut("execution (* com.cai.scheduler.service.*Service.insertJob(..))")
//    void controllerAspect(){}
//
//    Closure paramCall = {JoinPoint joinPoint->
//        List<Class> clazzs = []
//        joinPoint.args.each {param->
//            clazzs.add(param.class)
//        }
//        return clazzs.toArray()
//    }
//
//    Closure<Boolean> hasMethod = {Method[] methods, String methodName ->
//        for (int i = 0 ; i < methods.length ; i++){
//            if(methods[i].name.equals(methodName))
//                return true
//        }
//        return false
//    }
//    @Before("controllerAspect()")
//    void doBefore(JoinPoint joinPoint){
//        if(!hasMethod.call(joinPoint.target.class.getDeclaredMethods(),'beforeInsertJob'))
//            return
//        Method before = joinPoint.target.class.getDeclaredMethod('beforeInsertJob',paramCall.call(joinPoint) as Class[])
//        before.invoke(joinPoint.target,joinPoint.args[0])
//        logger.info("${this.class} before starter")
//    }
//
//    @After("controllerAspect()")
//    void doAfter(JoinPoint joinPoint){
//        if(!hasMethod.call(joinPoint.target.class.getDeclaredMethods(),'afterInsertJob'))
//            return
//        Method after = joinPoint.target.class.getDeclaredMethod('afterInsertJob',paramCall.call(joinPoint) as Class[])
//        after.invoke(joinPoint.target,joinPoint.args[0])
//        logger.info("${this.class} after starter")
//    }
}
