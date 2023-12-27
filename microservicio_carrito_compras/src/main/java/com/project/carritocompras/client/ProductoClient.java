package com.project.carritocompras.client;

import org.springframework.http.ResponseEntity;

import com.project.carritocompras.dto.Producto;

import reactor.core.publisher.Mono;

public interface ProductoClient {

	public Mono<ResponseEntity<Producto>> obtenerPorId(String id);
	
	public Mono<ResponseEntity<Producto>> restarStockProducto(String id, Integer cantidad);
	
	public Mono<ResponseEntity<Producto>> actualizarProducto(String id, Producto producto);
	
}
