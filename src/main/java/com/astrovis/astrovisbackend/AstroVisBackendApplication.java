package com.astrovis.astrovisbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.astrovis.astrovisbackend.mappers")
@EnableAsync
@EnableScheduling
public class AstroVisBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(AstroVisBackendApplication.class, args);
	}

}
