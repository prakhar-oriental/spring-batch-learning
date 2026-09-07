package com.example.spring_batch_learning;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchLearningApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchLearningApplication.class, args);
	}

}
