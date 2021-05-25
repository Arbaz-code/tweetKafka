package com.tweetapp.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class ApiApplication extends SpringBootServletInitializer{

    @Value("${logging.level.root:OFF}")
    String message = "";
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
