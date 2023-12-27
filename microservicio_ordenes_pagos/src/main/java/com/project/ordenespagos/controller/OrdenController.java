package com.project.ordenespagos.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.ordenespagos.documents.Orden;
import com.project.ordenespagos.dto.CrearOrdenRequest;
import com.project.ordenespagos.dto.ModificarStatusRequest;
import com.project.ordenespagos.exception.ResourceNotFoundException;
import com.project.ordenespagos.service.OrdenService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class OrdenController {
	
	@Autowired
	private OrdenService ordenService;
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Orden>> buscarPorId(@PathVariable String id) {
		return ordenService.buscarPorId(id)
				.map(o -> ResponseEntity.status(HttpStatus.OK)
						.contentType(MediaType.APPLICATION_JSON)
						.body(o))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping("/crear")
	public Mono<ResponseEntity<Orden>> crearOrden (@RequestBody CrearOrdenRequest request){
		
		return ordenService.generarOrden(request)
				.map(o -> ResponseEntity.created(URI.create("/orden/".concat(o.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(o))
				.onErrorResume(throwable -> {
					if (throwable instanceof ResourceNotFoundException) {
	                    return Mono.just(ResponseEntity.notFound().build());
	                } else {
	                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	                }
				});
	}
	
	@GetMapping("/listar-cliente/{id}")
	public Mono<ResponseEntity<Flux<Orden>>> buscarPorIdCliente(@PathVariable String id) {

		Flux<Orden> orden = ordenService.buscarPorIdUsuario(id);

		return orden
			    .hasElements()
			    .flatMap(hasElements -> {
			        if (hasElements) {
			            return Mono.just(ResponseEntity.ok()
			                    .contentType(MediaType.APPLICATION_JSON)
			                    .body(orden));
			        } else {
			            return Mono.just(ResponseEntity.notFound().build());
			        }
			    });
	}
	
	@PostMapping("/modificar")
	public Mono<ResponseEntity<Orden>> modificarStatus(@RequestBody ModificarStatusRequest request) {

		return ordenService.buscarPorId(request.getIdOrden())
				.flatMap(orden -> {
					orden.setStatus(request.getEstado());
					return ordenService.modificarEstado(orden)
							.map(o -> ResponseEntity.ok()
									.contentType(MediaType.APPLICATION_JSON)
									.body(o));
				})
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
}
