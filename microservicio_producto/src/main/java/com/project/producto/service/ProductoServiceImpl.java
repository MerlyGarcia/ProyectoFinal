package com.project.producto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.producto.dao.CategoriaDao;
import com.project.producto.dao.MaterialDao;
import com.project.producto.dao.ProductoDao;
import com.project.producto.documents.Categoria;
import com.project.producto.documents.Material;
import com.project.producto.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	private ProductoDao productoDao;
	
	@Autowired
	private CategoriaDao categoriaDao;
	
	@Autowired
	private MaterialDao materialDao;
	
	@Override
	@Transactional(readOnly = true)
	public Flux<Producto> findAll() {
		return productoDao.findAll();
	}

	@Override
	public Flux<Producto> findByCategoria(String categoria) {
		return productoDao.findByCategoriaNombre(categoria);
	}

	@Override
	public Flux<Producto> findByMaterial(String material) {
		return productoDao.findByMaterialNombre(material);
	}

	@Override
	public Mono<Producto> findById(String id) {
		return productoDao.findById(id);
	}

	@Override
	public Mono<Producto> registerProduct(Producto producto) {
		return productoDao.save(producto);
	}

	@Override
	public Mono<Void> deleteProduct(Producto producto) {
		return productoDao.delete(producto);
	}

	@Override
	public Mono<Categoria> registerCategoria(Categoria categoria) {
		return categoriaDao.save(categoria);
	}

	@Override
	public Mono<Material> registerMaterial(Material material) {
		return materialDao.save(material);
	}

}
