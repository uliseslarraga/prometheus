package com.ulises.larrag.demo_prometheus;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@SpringBootApplication
@RestController
public class DemoPrometheusApplication {
	
	private final Random random = new Random();

	public static void main(String[] args) {
		SpringApplication.run(DemoPrometheusApplication.class, args);
	}

	@GetMapping("/")
   public String hello() throws InterruptedException {
	Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
       return "Hello world!";
   }
	
    @GetMapping("/randomStatus")
    public ResponseEntity<String> getRandomStatus(HttpServletResponse response) {
    	double chance = random.nextDouble(100); // Generate a number between 0-99

        if (chance < 90) { // 80% chance of success
            return ResponseEntity.ok("Success!");
        } else if (chance < 94) { // 10% chance of client error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request!");
        } else { // 10% chance of server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error!");
        }
    }

   @GetMapping("/metrics")
   public String metrics() {
       return "";
   }

}
