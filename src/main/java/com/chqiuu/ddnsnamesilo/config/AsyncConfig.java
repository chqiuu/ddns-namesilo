package com.chqiuu.ddnsnamesilo.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

/**
 * 异步执行配置
 */
@Slf4j
@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        taskScheduler.setAwaitTerminationSeconds(5);
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        taskScheduler.initialize();
        return taskScheduler;
    }

    @Bean
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-pool-");
        //默认是false，即shutdown时会立即停止并终止当前正在执行任务
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //指定被拒绝任务的处理方法，经过测试当并发量超过队列长度时可以继续执行，否则会抛出 org.springframework.core.task.TaskRejectedException异常
        executor.setRejectedExecutionHandler((r, executor1) -> {
            for (; ; ) {
                try {
                    executor1.getQueue().put(r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return;
            }
        });
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, params) -> {
            log.error("异步任务异常：方法：{} 参数：{}", method.getName(), JSON.toJSONString(params));
            log.error(throwable.getMessage(), throwable);
        };
    }
}
