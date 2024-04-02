package com.ren.rl.service;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CommonService {

    @Resource(name = "customerRetryTemplate")
    private RetryTemplate retryTemplate;

    public void doWorkWithRetry(){
        long l = System.currentTimeMillis();
        AtomicLong startTime = new AtomicLong(l);
        Serializable execute = retryTemplate.execute(retryContext -> {
            System.out.println("cost time: " + (System.currentTimeMillis() - startTime.get()));
            int a = 2 / 0;
            return "over work";
        }, retryContext -> {
            System.out.println(retryContext.getLastThrowable().getMessage());
            return retryContext.getRetryCount();
        });
        System.out.println("over");
    }
}
