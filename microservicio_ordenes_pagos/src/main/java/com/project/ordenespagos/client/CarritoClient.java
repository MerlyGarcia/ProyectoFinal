package com.project.ordenespagos.client;

import org.springframework.http.ResponseEntity;

import com.project.ordenespagos.dto.Carrito;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface CarritoClient {

	public Mono<ResponseEntity<Carrito>> buscarCarritoPorId(String id);
}
