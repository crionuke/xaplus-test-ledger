package com.crionuke.xaplus.test.ledger;


import com.crionuke.bolts.Worker;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
class ThreadPool {

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    ThreadPool() {
        threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("ledger-");
        threadPoolTaskExecutor.setCorePoolSize(16);
        threadPoolTaskExecutor.initialize();
    }

    void execute(Worker service) {
        threadPoolTaskExecutor.execute(service);
    }
}

