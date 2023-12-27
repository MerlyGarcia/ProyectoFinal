package com.project.carritocompras.service;

import com.project.carritocompras.documents.Carrito;
import com.project.carritocompras.dto.AgregarProductoRequest;
import com.project.carritocompras.dto.EliminarProductoRequest;

import reactor.core.publisher.Mono;

public interface CarritoService {
	
	public Mono<Carrito> buscarPorId(String id);
	
	public Mono<Carrito> crearCarrito();

	public Mono<Carrito> agregarProductoAlCarrito(AgregarProductoRequest request);
	
	public Mono<Carrito> eliminarProductoDelCarrito(EliminarProductoRequest request);
}
