package com.project.producto.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.project.producto.documents.Categoria;

import reactor.core.publisher.Flux;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria, String> {

	public Flux<Categoria> findByNombre();
}
