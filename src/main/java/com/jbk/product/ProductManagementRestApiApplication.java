package com.jbk.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@SpringBootApplication
public class ProductManagementRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductManagementRestApiApplication.class, args);
		System.out.println("running successfully");
		System.out.println("Developer 1 changes");
	}
@Bean
public CommonsMultipartResolver commonsMultipartResolver() {
	return new CommonsMultipartResolver();
	
}
	 
}
