package com.project.producto.service;

import com.project.producto.documents.Categoria;
import com.project.producto.documents.Material;
import com.project.producto.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

	public Flux<Producto> findAll();
	
	public Flux<Producto> findByCategoria(String categoria);
	
	public Flux<Producto> findByMaterial(String material);
	
	public Mono<Producto> findById(String id);
	
	public Mono<Producto> registerProduct(Producto producto);
	
	public Mono<Void> deleteProduct(Producto producto);
	
	public Mono<Categoria> registerCategoria(Categoria categoria);
	
	public Mono<Material> registerMaterial(Material material);
}
