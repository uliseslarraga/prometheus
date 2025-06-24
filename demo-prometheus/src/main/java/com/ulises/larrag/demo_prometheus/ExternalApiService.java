package com.ulises.larrag.demo_prometheus;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.DistributionSummary;


@Service
public class ExternalApiService {
   private final DistributionSummary requestLatency;
   private final RestTemplate restTemplate;

   public ExternalApiService(MeterRegistry registry) {
       this.requestLatency = DistributionSummary.builder("external_api.request.duration")
               .description("External API request duration")
               .publishPercentiles(0.5, 0.95, 0.99)  // Track 50th, 95th, and 99th percentiles
               .register(registry);

       this.restTemplate = new RestTemplate();
   }

   public Object fetchPosts() {
       long start = System.nanoTime();
       try {
           return restTemplate.getForObject(
               "https://jsonplaceholder.typicode.com/posts",
               Object.class
           );
       } finally {
           long duration = System.nanoTime() - start;
           requestLatency.record(duration / 1_000_000_000.0);  // Convert to seconds
       }
   }
}