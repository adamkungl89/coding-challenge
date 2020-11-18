package com.adamspokes.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class Application {

	@Value("classpath:static/data.json")
	Resource resourceFile;

	@Autowired
	IAccountService accountService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			loadInitialDataFile();
			System.out.println("Revenue: " + TypeConvertHelpers.moneyNumberFormat(accountService.calculateRevenue()));
			System.out.println("Expenses: " + TypeConvertHelpers.moneyNumberFormat(accountService.calculateExpenses()));
			System.out.println("Gross Profit Margin: " + TypeConvertHelpers.percentageNumberFormat(accountService.calculateGrossProfitMargin()));
			System.out.println("Net Profit Margin: " + TypeConvertHelpers.percentageNumberFormat(accountService.calculateNetProfitMargin()));
			System.out.println("Working Capital Ratio: " + TypeConvertHelpers.percentageNumberFormat(accountService.calculateWorkingCapitalRatio()));
		};
	}

	private void loadInitialDataFile() {
		System.out.println("Loading initial data file...");
		String jsonFileString = TypeConvertHelpers.resourceToString(resourceFile);
		accountService.parseJsonToAccounts(jsonFileString);
	}
	
}
