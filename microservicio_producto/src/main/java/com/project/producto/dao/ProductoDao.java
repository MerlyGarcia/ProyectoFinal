package com.project.producto.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.project.producto.documents.Producto;

import reactor.core.publisher.Flux;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String>{

	Flux<Producto> findByCategoriaNombre(String categoria);
	
	Flux<Producto> findByMaterialNombre(String material);

}
