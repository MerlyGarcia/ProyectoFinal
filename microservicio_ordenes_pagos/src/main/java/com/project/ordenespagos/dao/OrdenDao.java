package com.project.ordenespagos.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.project.ordenespagos.documents.Orden;

import reactor.core.publisher.Flux;

public interface OrdenDao extends ReactiveMongoRepository<Orden, String> {

	public Flux<Orden> findByUsuarioId(String id);
}
