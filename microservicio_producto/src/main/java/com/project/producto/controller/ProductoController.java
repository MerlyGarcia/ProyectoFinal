package com.project.producto.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.producto.documents.Producto;
import com.project.producto.service.ProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ProductoController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

	@Autowired
	private ProductoService productoService;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Producto>>> findAll() {
		
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(productoService.findAll()));
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Producto>> findById(@PathVariable String id) {
		
		return productoService.findById(id)
				.map(p -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/categoria/{categoria}")
	public Mono<ResponseEntity<Flux<Producto>>> findByCategoria(@PathVariable String categoria) {
		
		return productoService.findByCategoria(categoria)
				.collectList()
				.flatMap(lista -> {
					if(lista.isEmpty()) {
						return Mono.just(ResponseEntity.notFound().build());
					}
					return Mono.just(ResponseEntity.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(Flux.fromIterable(lista)));
				});
	}
	
	@GetMapping("/material/{material}")
	public Mono<ResponseEntity<Flux<Producto>>> findByMaterial(@PathVariable String material) {
		
		return productoService.findByMaterial(material)
				.collectList()
				.flatMap(lista -> {
					if(lista.isEmpty()) {
						return Mono.just(ResponseEntity.notFound().build());
					}
					return Mono.just(ResponseEntity.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(Flux.fromIterable(lista)));
				});
	}
	
	@PostMapping
	public Mono<ResponseEntity<Producto>> registrarProducto(@RequestBody Producto producto) {
		
		return productoService.registerProduct(producto)
				.flatMap(p -> Mono.just(ResponseEntity.created(URI.create("/producto/".concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(p)));
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> eliminarProducto(@PathVariable String id) {
		
		return productoService.findById(id)
		.flatMap(p -> productoService.deleteProduct(p)
				.then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
		.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
	
	@GetMapping("/restar_stock/{id}/{cantidad}")
	public Mono<ResponseEntity<Producto>> restarStockProducto(@PathVariable String id, @PathVariable Integer cantidad) {
		
		return productoService.findById(id)
		.flatMap(p -> {
			
			if(cantidad > p.getStock()) {
				logger.error("No hay suficiente stock disponible");
				return Mono.error(new InterruptedException("Stock insuficiente"));
			}
			
			p.setStock(p.getStock() - cantidad);
			
			if(p.getStock() == 0)
				p.setEstado("Agotado");
			
			return productoService.registerProduct(p)
					.map(prod -> ResponseEntity.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(prod));
		})
		.onErrorResume(ex -> Mono.just(new ResponseEntity<Producto>(HttpStatus.BAD_REQUEST)))
		.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Producto>> actualizarProducto(@PathVariable String id, @RequestBody Producto producto) {
		
		return productoService.findById(id)
				.flatMap(p -> {
					p.setNombre(producto.getNombre());
					p.setDescripcion(producto.getDescripcion());
					p.setStock(producto.getStock());
					p.setPrecio(producto.getPrecio());
					p.setEstado(producto.getStock() > 0 ? "Disponible":"Agotado");
					p.setCategoria(producto.getCategoria());
					p.setMaterial(producto.getMaterial());
					
					return productoService.registerProduct(p)
							.flatMap(prod -> Mono.just(ResponseEntity.created(URI.create("/producto/".concat(id)))
									.contentType(MediaType.APPLICATION_JSON)
									.body(prod)));
				})
				.defaultIfEmpty(ResponseEntity.notFound().build());

	}
}
