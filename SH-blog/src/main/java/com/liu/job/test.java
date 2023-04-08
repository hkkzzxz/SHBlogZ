package com.liu.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
//
//@Component
public class test {
    @Scheduled(cron = "0/5 * * * * ?")
    public void TestJob(){
        System.out.println("定时任务");
    }
}
