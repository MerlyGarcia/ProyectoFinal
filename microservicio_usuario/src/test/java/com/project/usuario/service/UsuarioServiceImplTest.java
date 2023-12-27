package com.project.usuario.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.usuario.dao.UsuarioDao;
import com.project.usuario.documents.Usuario;
import com.project.usuario.dto.Rol;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient
public class UsuarioServiceImplTest {

	@InjectMocks
	private UsuarioServiceImpl usuarioService;
	
	@Mock
	private UsuarioDao usuarioDao;
	
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
    	
    	when(usuarioDao.findById(any(String.class))).thenReturn(Mono.just(u1));
    	
        Mono<Usuario> resultado = usuarioService.findById(u1.getId());

        StepVerifier.create(resultado)
                .expectNext(u1)
                .expectComplete()
                .verify();
    }
    
    @Test
    void createdUser() {
    	
    	when(usuarioDao.save(any(Usuario.class))).thenReturn(Mono.just(u1));
    	
        Mono<Usuario> resultado = usuarioService.createdUser(u1);

        StepVerifier.create(resultado)
                .expectNext(u1)
                .expectComplete()
                .verify();
    }
    
    @Test
    void deleteUser() {
    	
    	when(usuarioDao.delete(any(Usuario.class))).thenReturn(Mono.empty());
    	
        Mono<Void> resultado = usuarioService.deleteUser(u1);

        StepVerifier.create(resultado)
                .expectComplete()
                .verify();
    }
}
