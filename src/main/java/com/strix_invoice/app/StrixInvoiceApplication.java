package com.strix_invoice.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class StrixInvoiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StrixInvoiceApplication.class, args);
	}


	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:3000") // Allow only your frontend domain
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowCredentials(true) // Allow cookies to be included
						.allowedHeaders("*") // Allow any headers
						.exposedHeaders("Authorization") // Expose any needed headers
						.maxAge(3600); // Cache preflight requests for an hour
			}
		};
	}

}
