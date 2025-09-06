package com.tuempresa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// @ComponentScan(basePackages = "com.tuempresa")
@SpringBootApplication
@EnableDiscoveryClient
public class TrainingAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(TrainingAppApplication.class, args);
	}
} 