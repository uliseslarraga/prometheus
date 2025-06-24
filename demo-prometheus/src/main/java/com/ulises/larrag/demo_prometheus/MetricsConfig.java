package com.ulises.larrag.demo_prometheus;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.MeterBinder;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter requestCounter(MeterRegistry registry) {
        return Counter.builder("http.requests.total")  // metric name
                .description("Total number of HTTP requests")  // metric description
                .tags("app", "demo")  // metric labels
                .register(registry);  // register with Spring's metric registry
    }
    
    @Bean
    public AtomicInteger gaugeActive(MeterRegistry registry) {
        return registry.gauge("http.requests.active",
                Tags.of("app", "demo"),
                new AtomicInteger(0));
    }

    @Bean
    public MeterBinder memoryMetrics() {
    	return registryVar -> {
    		registryVar.gauge("jvm.memory.used", Runtime.getRuntime(), rt -> 
            rt.totalMemory() - rt.freeMemory());
    	};
    }
    
    @Bean
    public Timer requestLatencyHistogram(MeterRegistry registry) {
        return Timer.builder("http.request.duration.seconds")
                .description("HTTP request duration in seconds")
                .tags("app", "demo")
                .publishPercentileHistogram(true)
                .register(registry);
    }

}
