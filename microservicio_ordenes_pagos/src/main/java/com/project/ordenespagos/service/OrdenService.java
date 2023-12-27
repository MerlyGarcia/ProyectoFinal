package com.project.ordenespagos.service;

import com.project.ordenespagos.documents.Orden;
import com.project.ordenespagos.dto.CrearOrdenRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrdenService {

	public Mono<Orden> buscarPorId(String id);

	public Mono<Orden> generarOrden(CrearOrdenRequest orden);
	
	public Flux<Orden> buscarPorIdUsuario(String id);
	
	public Mono<Orden> modificarEstado(Orden orden);
}
