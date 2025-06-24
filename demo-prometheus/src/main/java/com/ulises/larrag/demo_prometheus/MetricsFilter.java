package com.ulises.larrag.demo_prometheus;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MetricsFilter extends OncePerRequestFilter {
    private final Counter requestCounter;
    private final AtomicInteger activeRequests;
    private final Timer requestLatencyHistogram;

    public MetricsFilter(Counter requestCounter, 
    		AtomicInteger activeRequests, 
    		Timer requestLatencyHistogram) {
        this.requestCounter = requestCounter;
        this.activeRequests = activeRequests;
        this.requestLatencyHistogram = requestLatencyHistogram;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        activeRequests.incrementAndGet();  // Increment at start of request
        Timer.Sample sample = Timer.start();
        try {
            requestCounter.increment();
            filterChain.doFilter(request, response);
        } finally {
        	sample.stop(requestLatencyHistogram);
            activeRequests.decrementAndGet();  // Decrement after request completes
        }
    }
}
