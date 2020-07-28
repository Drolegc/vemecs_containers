package com.magicbussines.VeMecApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class VeMecApiApplication extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		SpringApplication.run(VeMecApiApplication.class, args);
	}
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(VeMecApiApplication.class);
    }
	
	
}
