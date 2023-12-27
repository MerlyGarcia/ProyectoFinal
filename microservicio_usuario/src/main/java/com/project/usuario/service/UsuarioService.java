package com.project.usuario.service;

import com.project.usuario.documents.Usuario;

import reactor.core.publisher.Mono;

public interface UsuarioService {
	
	public Mono<Usuario> findById(String id);
	
	public Mono<Usuario> createdUser(Usuario usuario);
	
	public Mono<Void> deleteUser(Usuario usuario);

}
