package com.project.usuario.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

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
import org.springframework.web.bind.support.WebExchangeBindException;

import com.project.usuario.documents.Usuario;
import com.project.usuario.service.UsuarioService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Usuario>> findById(@PathVariable String id) {

		return usuarioService.findById(id)
				.map(user -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(user))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Mono<ResponseEntity<Map<String, Object>>> createUser(@Valid @RequestBody Mono<Usuario> monoUsuario) {

		Map<String, Object> respuesta = new HashMap<>();

		return monoUsuario.flatMap(user -> {

			return usuarioService.createdUser(user).map(u -> {
				respuesta.put("usuario", u);
				return ResponseEntity.created(URI.create("/usuario/".concat(u.getId())))
						.contentType(MediaType.APPLICATION_JSON).body(respuesta);
			});
		}).onErrorResume(t -> Mono.just(t).cast(WebExchangeBindException.class)
				.flatMap(e -> Mono.just(e.getFieldErrors())).flatMapMany(errors -> Flux.fromIterable(errors))
				.map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
				.collectList().flatMap(list -> {
					respuesta.put("errors", list);
					return Mono.just(ResponseEntity.badRequest().body(respuesta));
				}));
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Mono<Usuario>>> updateUser(@PathVariable String id, @RequestBody Usuario usuario) {
		return usuarioService.findById(id).flatMap(user -> {
			user.setNombres(usuario.getNombres());
			user.setApellidos(usuario.getApellidos());
			user.setEmail(usuario.getEmail());
			user.setFechaNacimiento(usuario.getFechaNacimiento());
			user.setRol(usuario.getRol());

			return usuarioService.createdUser(user);
		}).map(user -> ResponseEntity.created(URI.create("/usuario/".concat(id)))
				.contentType(MediaType.APPLICATION_JSON).body(usuarioService.createdUser(user)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
		return usuarioService.findById(id)
				.flatMap(user -> usuarioService.deleteUser(user)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
				.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}

}
