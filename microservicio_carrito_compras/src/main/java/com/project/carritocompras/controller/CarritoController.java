package com.project.carritocompras.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.carritocompras.documents.Carrito;
import com.project.carritocompras.dto.AgregarProductoRequest;
import com.project.carritocompras.dto.EliminarProductoRequest;
import com.project.carritocompras.service.CarritoService;

import reactor.core.publisher.Mono;

@RestController
public class CarritoController {
	
	private static Logger logger = LoggerFactory.getLogger(CarritoController.class);  
	
	@Autowired
	private CarritoService carritoService;
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Carrito>> buscarCarritoPorId(@PathVariable String id) {
		
		logger.info("Llamada al metodo buscarCarritoPorId");

	    return carritoService.buscarPorId(id)
	    		.map(c -> ResponseEntity.ok()
	    				.contentType(MediaType.APPLICATION_JSON)
	    				.body(c))
	    		.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/crear")
	public Mono<ResponseEntity<Carrito>> crearCarrito() {
		
		logger.info("Llamada al metodo crearCarrito");

	    return carritoService.crearCarrito()
	    		.map(c -> ResponseEntity.ok()
	    				.contentType(MediaType.APPLICATION_JSON)
	    				.body(c));
	}
	
	@PostMapping("/agregar")
	public Mono<ResponseEntity<Carrito>> agregarProducto(@RequestBody AgregarProductoRequest request) {
		
		logger.info("Llamada al metodo agregar Producto a carrito");
		
		return carritoService.agregarProductoAlCarrito(request)
				.map(c -> ResponseEntity.ok()
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(c));

	}
	
	@PostMapping("/eliminar")
	public Mono<ResponseEntity<Carrito>> eliminarProducto(@RequestBody EliminarProductoRequest request) {
		
		logger.info("Llamada al metodo eliminar Producto del carrito");
		
		return carritoService.eliminarProductoDelCarrito(request)
				.map(c -> ResponseEntity.ok()
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(c));

	}

}
