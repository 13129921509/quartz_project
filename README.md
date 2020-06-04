# job-scheduler

### 简介
内部采用Quartz开源库，交由springboot管理，采用接口方式进行新增删除任务，
支持增删查改任务项。

### 版本
| 组件名      | 版本            |
| ---        | ---            |
| springboot | v2.2.6-release |
| gradle     | gradle-5.1-all |
| quartz     | 2.3.0          |

### release 1.0.0
|日期|改动|
|---|---|
|2020-05-14|引入mongo-api用来做基础数据库|
|2020-05-15|1. 迁移general相关组件做新工程|
| |2. 新增logback日志功能|
|2020-05-18|1.解决已知bug|
| |2.http相关工具类迁移至base-general工程|


### release 1.2.0

|日期|改动|
|---|---|
|2020-05-26|引入AOP结构，解决项目耦合度高的问题|

### release 1.3.0
|日期|改动|
|---|---|
|2020-06-04|弃用AOP结构，采用模板方式+重写解决耦合度过高问题|

    弃用aop的原因是，后期可能有很多BaseSchedulerService的子类，
    采用模板更高管理，使用aop，后期对每个子类都得创建aspect类，
    十分不方便。

