package com.cai.scheduler.config

interface SchedulerAction {
    boolean addJob(String name, String cron, String url)
}