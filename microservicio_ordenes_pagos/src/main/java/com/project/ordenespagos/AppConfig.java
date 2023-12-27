package com.project.ordenespagos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

	@Value("${config.base.endpoint.usuario}")
	private String urlClientUsuario;
	
	@Value("${config.base.endpoint.carrito}")
	private String urlClientCarrito;
	
	@Bean
	@LoadBalanced
	public WebClient.Builder registrarWebClientUsuario() {
		return WebClient.builder().baseUrl(urlClientUsuario);
	}
	
	@Bean
	@LoadBalanced
	public WebClient.Builder registrarWebClientCarrito() {
		return WebClient.builder().baseUrl(urlClientCarrito);
	}
}
