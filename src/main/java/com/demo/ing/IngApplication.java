package com.demo.ing;

import com.demo.ing.transaction.service.TransactionPopulateService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class IngApplication implements CommandLineRunner {
	private final TransactionPopulateService transactionPopulateService;

	public IngApplication(TransactionPopulateService transactionPopulateService) {
		this.transactionPopulateService = transactionPopulateService;
	}

	public static void main(String[] args) {
		SpringApplication.run(IngApplication.class, args);
	}

	@Override
	public void run(String... args) {
		transactionPopulateService.populateRedis();
	}

}
