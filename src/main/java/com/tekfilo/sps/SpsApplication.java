package com.tekfilo.sps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan("com.tekfilo.sps")
@EnableAsync
public class SpsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpsApplication.class, args);
	}

}
