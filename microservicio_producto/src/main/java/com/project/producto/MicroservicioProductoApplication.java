package com.project.producto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroservicioProductoApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MicroservicioProductoApplication.class, args);
	}

}
