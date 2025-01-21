package com.astrovis.astrovisbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.astrovis.astrovisbackend.mappers")
public class AstroVisBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AstroVisBackendApplication.class, args);
	}

}
