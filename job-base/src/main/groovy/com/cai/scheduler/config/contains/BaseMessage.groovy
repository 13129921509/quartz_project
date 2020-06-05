package com.cai.scheduler.config.contains

class BaseMessage {
    static class ERROR{
        final static String JOB_ERROR_MSG_0000 = "系统错误"
        final static String JOB_ERROR_MSG_0001 = "未找到对应job, code : {0}"
        final static String JOB_ERROR_MSG_0002 = "启动job失败"
        final static String JOB_ERROR_MSG_0003 = "{0} 任务未找到"
    }

    static class GENERAL{
        final static String JOB_GENERAL_MSG_0001 = "存在此任务且正在执行"
        final static String JOB_GENERAL_MSG_0002 = "该任务已停止 code:{0}"

    }
}
