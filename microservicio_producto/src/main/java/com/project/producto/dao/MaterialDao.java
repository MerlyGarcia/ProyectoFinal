package com.project.producto.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.project.producto.documents.Material;

public interface MaterialDao extends ReactiveMongoRepository<Material, String> {

}
