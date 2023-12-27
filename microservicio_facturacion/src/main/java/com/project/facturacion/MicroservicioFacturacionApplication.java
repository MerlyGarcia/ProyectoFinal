package com.project.facturacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroservicioFacturacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioFacturacionApplication.class, args);
	}

}
