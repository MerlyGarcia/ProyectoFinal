package com.project.carritocompras.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.project.carritocompras.dto.Producto;

import reactor.core.publisher.Mono;

@Component
public class ProductoClientImpl implements ProductoClient{
	
	private static Logger logger = LoggerFactory.getLogger(ProductoClientImpl.class);

	@Autowired
	private WebClient.Builder webClient;
	
	@Override
	public Mono<ResponseEntity<Producto>> obtenerPorId(String id) {
		
		return webClient.build().get()
			    .uri("/{id}",id)
			    .retrieve()
			    .toEntity(Producto.class)
			    .onErrorResume(WebClientResponseException.class, ex -> {
			        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
			            logger.info("Mensaje del error: " + ex.getMessage());
			            return Mono.just(ResponseEntity.notFound().build());
			        } else {
			        	logger.error("Error del cliente: " + ex.getRawStatusCode() + " " + ex.getStatusText());
			            return Mono.just(ResponseEntity.status(ex.getRawStatusCode()).build());
			        }
			    });
	}

	@Override
	public Mono<ResponseEntity<Producto>> restarStockProducto(String id, Integer cantidad) {
		return webClient.build().get()
			    .uri("/restar_stock/{id}/{cantidad}", id, cantidad)
			    .retrieve()
			    .toEntity(Producto.class)
			    .onErrorResume(WebClientResponseException.class, ex -> {
			        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
			            logger.info("Mensaje del error: " + ex.getMessage());
			            return Mono.just(ResponseEntity.notFound().build());
			        } else {
			        	logger.error("Error del cliente: " + ex.getRawStatusCode() + " " + ex.getStatusText());
			            return Mono.just(ResponseEntity.status(ex.getRawStatusCode()).build());
			        }
			    });
	}

	@Override
	public Mono<ResponseEntity<Producto>> actualizarProducto(String id, Producto producto) {
		return webClient.build().put()
			    .uri("/{id}", id)
			    .bodyValue(producto)
			    .retrieve()
			    .toEntity(Producto.class)
			    .onErrorResume(WebClientResponseException.class, ex -> {
			        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
			            logger.info("Mensaje del error: " + ex.getMessage());
			            return Mono.just(ResponseEntity.notFound().build());
			        } else {
			        	logger.error("Error del cliente: " + ex.getRawStatusCode() + " " + ex.getStatusText());
			            return Mono.just(ResponseEntity.status(ex.getRawStatusCode()).build());
			        }
			    });
	}

}
