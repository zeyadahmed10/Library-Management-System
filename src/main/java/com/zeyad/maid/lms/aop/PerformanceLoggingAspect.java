package com.zeyad.maid.lms.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Aspect
@Component
public class PerformanceLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceLoggingAspect.class);
    private final Map<String, AtomicLong> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> totalResponseTimes = new ConcurrentHashMap<>();

    @Around(value = "@annotation(com.zeyad.maid.lms.annotation.LogPerformance)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        logger.info("{} executed in {} milliseconds", joinPoint.getSignature(), executionTime);

        String endpoint = joinPoint.getSignature().toShortString();
        requestCounts.computeIfAbsent(endpoint, k -> new AtomicLong()).incrementAndGet();
        totalResponseTimes.computeIfAbsent(endpoint, k -> new AtomicLong()).addAndGet(executionTime);

        return result;
    }
    @Scheduled(fixedDelay = 300000)
    public void logMetrics() {
        for (Map.Entry<String, AtomicLong> entry : requestCounts.entrySet()) {
            String endpoint = entry.getKey();
            long requests = entry.getValue().get();
            long totalResponseTimeValue = totalResponseTimes.getOrDefault(endpoint, new AtomicLong()).get();
            double averageResponseTime = requests > 0 ? (double) totalResponseTimeValue / requests : 0;

            logger.info("Endpoint: {}", endpoint);
            logger.info("Total Requests: {}", requests);
            logger.info("Average Response Time: {} milliseconds", averageResponseTime);
        }
    }
}

