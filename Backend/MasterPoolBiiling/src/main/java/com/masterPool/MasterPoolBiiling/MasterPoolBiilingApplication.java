package com.masterPool.MasterPoolBiiling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.masterPool.MasterPoolBiiling")
public class MasterPoolBiilingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MasterPoolBiilingApplication.class, args);
	}

}
