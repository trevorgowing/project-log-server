package com.trevorgowing.projectlog;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    /**
     * https://spring.io/blog/2014/12/02/latest-jackson-integration-improvements-in-spring
     */

    @Bean
    public Module jdk8() {
        return new Jdk8Module();
    }

    @Bean
    public Module javaTimeModule() {
	    return new JavaTimeModule();
    }
}
