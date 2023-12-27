package com.project.ordenespagos.client;

import org.springframework.http.ResponseEntity;

import com.project.ordenespagos.dto.Usuario;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface UsuarioClient {

	public Mono<ResponseEntity<Usuario>> obtenerPorId(String id);
}
