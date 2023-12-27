package com.project.carritocompras.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.project.carritocompras.documents.Carrito;

public interface CarritoDao extends ReactiveMongoRepository<Carrito, String>{

}
