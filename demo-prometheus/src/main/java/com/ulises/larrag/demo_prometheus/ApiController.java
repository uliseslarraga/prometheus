package com.ulises.larrag.demo_prometheus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
   private final ExternalApiService apiService;

   public ApiController(ExternalApiService apiService) {
       this.apiService = apiService;
   }

   @GetMapping("/posts")
   public Object getPosts() {
       return apiService.fetchPosts();
   }
}
