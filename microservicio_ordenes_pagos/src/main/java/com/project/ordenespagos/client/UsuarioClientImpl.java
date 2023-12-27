package com.project.ordenespagos.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.project.ordenespagos.dto.Usuario;

import reactor.core.publisher.Mono;

@Component
public class UsuarioClientImpl implements UsuarioClient {

	private static Logger logger = LoggerFactory.getLogger(UsuarioClientImpl.class);

	@Autowired
	@Qualifier("registrarWebClientUsuario")
	private WebClient.Builder webClient;
	
	@Override
	public Mono<ResponseEntity<Usuario>> obtenerPorId(String id) {
		
		return webClient.build().get()
			    .uri("/{id}",id)
			    .retrieve()
			    .toEntity(Usuario.class)
			    .onErrorResume(WebClientResponseException.class, ex -> {
			        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
			            logger.info("Mensaje del error: " + ex.getMessage());
			            return Mono.just(ResponseEntity.notFound().build());
			        } else {
			        	logger.error("Error del cliente: " + ex.getRawStatusCode() + " " + ex.getStatusText());
			            return Mono.just(ResponseEntity.status(ex.getRawStatusCode()).build());
			        }
			    });
	}

}
