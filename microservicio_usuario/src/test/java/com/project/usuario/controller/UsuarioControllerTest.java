package com.project.usuario.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.project.usuario.documents.Usuario;
import com.project.usuario.dto.Rol;
import com.project.usuario.service.UsuarioService;

import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
public class UsuarioControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    
    @MockBean
	private UsuarioService usuarioService;
	
    private Usuario u1;

    @BeforeEach
    public void setUp() {
    	u1 = new Usuario();
		
    	u1.setId("1");
    	u1.setNombres("Miguel");
    	u1.setApellidos("Sanchez Gonzales");
    	u1.setEmail("correo@gmail.com");
		u1.setFechaNacimiento(LocalDate.of(1996, 10, 27));
		u1.setRol(Rol.USER);
    }
    
	@Test
    void findById() {
		
		when(usuarioService.findById(any(String.class))).thenReturn(Mono.just(u1));
		
		webTestClient.get()
        .uri("/usuario/{id}", Collections.singletonMap("id", u1.getId()))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.nombres").isNotEmpty();
	}
	
    @Test
    void createUser() {
    	
    	when(usuarioService.createdUser(any(Usuario.class))).thenReturn(Mono.just(u1));
    	
    	webTestClient.post()
        .uri("/usuario")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(u1)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.usuario.id").isNotEmpty()
        .jsonPath("$.usuario.nombres").isNotEmpty();
    }
    
    @Test
    void createUserException() {
    	
    	u1.setNombres("");
    	when(usuarioService.createdUser(any(Usuario.class))).thenReturn(Mono.just(u1));
    	
    	webTestClient.post()
        .uri("/usuario")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(u1)
        .exchange()
        .expectStatus().isBadRequest();
    }
    
    @Test
    void updateUser() {
    	
    	when(usuarioService.findById(any(String.class))).thenReturn(Mono.just(u1));
    	when(usuarioService.createdUser(any(Usuario.class))).thenReturn(Mono.just(u1));
    	
    	webTestClient.put()
        .uri("/usuario/{id}", Collections.singletonMap("id", u1.getId()))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(u1)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.nombres").isNotEmpty();
    }
    
    @Test
    void deleteUser() {
    	
    	when(usuarioService.findById(any(String.class))).thenReturn(Mono.just(u1));
    	when(usuarioService.deleteUser(any(Usuario.class))).thenReturn(Mono.empty());
    	
    	webTestClient.delete()
        .uri("/usuario/{id}", Collections.singletonMap("id", u1.getId()))
        .exchange()
        .expectStatus().isNoContent()
        .expectBody()
        .isEmpty();
    }
}
