package com.window.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExportExecutor")
    public Executor taskExecutor() {
        String uuid = UUID.randomUUID().toString();
        int processorCount = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(processorCount); // 核心线程数
        executor.setMaxPoolSize(processorCount); // 最大线程数
        executor.setQueueCapacity(200); // 队列大小
        executor.setKeepAliveSeconds(60); // 线程空闲时间
        executor.setThreadNamePrefix("async-export-task-" + uuid + "-");
        // 拒绝策略：由调用线程处理（防止任务丢失，但会阻塞主线程）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
