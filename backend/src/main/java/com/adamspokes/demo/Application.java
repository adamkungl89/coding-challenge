package com.adamspokes.demo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

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
			System.out.println("Revenue: $" + accountService.calculateRevenue());
			System.out.println("Expenses: $" + accountService.calculateExpenses());
			System.out.println("Gross Profit Margin: " + accountService.calculateGrossProfitMargin() + "%");
			System.out.println("Net Profit Margin: " + accountService.calculateNetProfitMargin() + "%");
			System.out.println("Working Capital Ratio: " + accountService.calculateWorkingCapitalRatio() + "%");
		};
	}

	private void loadInitialDataFile() {
		System.out.println("Loading initial data file...");
		String jsonFileString = resourceToString(resourceFile);
		accountService.parseJsonToAccounts(jsonFileString);
	}

	private static String resourceToString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
	
}
