package com.project.carritocompras;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroservicioCarritoComprasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioCarritoComprasApplication.class, args);
	}

}
