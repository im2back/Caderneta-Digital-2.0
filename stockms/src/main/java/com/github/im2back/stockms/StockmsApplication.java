package com.github.im2back.stockms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class StockmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockmsApplication.class, args);
	}

}
