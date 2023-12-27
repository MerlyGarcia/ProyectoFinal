package com.project.facturacion.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.project.facturacion.documents.Factura;

public interface FacturaDao extends ReactiveMongoRepository<Factura, String>{

}
