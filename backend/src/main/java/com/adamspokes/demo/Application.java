package com.adamspokes.demo;

import java.io.InputStream;
import java.util.Arrays;

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

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("Do we have our data file?");

			int x = 0;
			InputStream is = resourceFile.getInputStream();
			while((x = is.read())!=-1) {
				System.out.print((char)x);
			}

/*
			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}
*/		

		};
	}
	
}
