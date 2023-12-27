package com.project.carritocompras.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.project.carritocompras.documents.ItemCarrito;

import reactor.core.publisher.Mono;

public interface ItemCarritoDao extends ReactiveMongoRepository<ItemCarrito, String>{

	public Mono<ItemCarrito> findByIdProducto(String idProducto);
}
