package com.codexsoft.servicesupport.main.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("supportService")
public class SupportServiceImpl implements SupportService {

public static int counter =0;

    @Async
    public void checkAsync(String msg) throws InterruptedException {
        Thread.sleep(5000);

        System.out.println("blink"+Thread.currentThread().getName());
    }

    @Async("threadPoolTaskExecutor")
    public void checkAsyncPooled(String msg) throws InterruptedException {
        Thread.sleep(5000);
        System.out.println("blink"+Thread.currentThread().getName());
    }
}
