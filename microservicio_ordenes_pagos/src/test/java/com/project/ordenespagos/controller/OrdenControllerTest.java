package com.project.ordenespagos.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.project.ordenespagos.documents.Orden;
import com.project.ordenespagos.dto.Carrito;
import com.project.ordenespagos.dto.CrearOrdenRequest;
import com.project.ordenespagos.dto.DetalleTarjeta;
import com.project.ordenespagos.dto.EstadoOrden;
import com.project.ordenespagos.dto.ItemCarrito;
import com.project.ordenespagos.dto.ModificarStatusRequest;
import com.project.ordenespagos.dto.Rol;
import com.project.ordenespagos.dto.TipoTarjeta;
import com.project.ordenespagos.dto.Usuario;
import com.project.ordenespagos.exception.ResourceNotFoundException;
import com.project.ordenespagos.service.OrdenService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
public class OrdenControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    
    @MockBean
    private OrdenService ordenService;
    
    private Orden orden;
    private Usuario u;
    private Carrito c;
    private DetalleTarjeta tarjeta;
    
    @BeforeEach
    public void setUp() {
    	orden = new Orden();
    	
    	u = new Usuario();
    	
    	u.setId("1");
    	u.setNombres("Miguel");
    	u.setApellidos("Sanchez Gonzales");
    	u.setEmail("correo@gmail.com");
		u.setFechaNacimiento(LocalDate.of(1996, 10, 27));
		u.setRol(Rol.USER);
		
		c = new Carrito();
	    List<ItemCarrito> items = new ArrayList<>();
	    ItemCarrito item = new ItemCarrito();
    	item.setId("1");
    	item.setIdProducto("1");
    	item.setNombreProducto("Pulsera");
    	item.setTotal(20D);
    	item.setCantidad(2);
    	
    	items = new ArrayList<>();
    	items.add(item);
    	
    	c.setId("1");
    	c.setFecha(LocalDate.now());
    	c.setItems(items);
    	c.setTotal(50d);
	    
    	tarjeta = new DetalleTarjeta();
    	tarjeta.setNumeroTarjeta("1234567895623104");
    	tarjeta.setTipoTarjeta(TipoTarjeta.DEBITO);
    	tarjeta.setCvv("289");
    	tarjeta.setFechaExpiracion("08/26");
    	
    	orden.setId("1");
    	orden.setUsuario(u);
    	orden.setCarrito(c);
    	orden.setStatus(EstadoOrden.CREADA);
    	orden.setTotal(c.getTotal());
    	orden.setFecha(c.getFecha());
    	orden.setTarjeta(tarjeta);
    	orden.setDireccion("Av Larco");
    }
    
	@Test
    void buscarPorId() {
		
		when(ordenService.buscarPorId(any(String.class))).thenReturn(Mono.just(orden));
		
		webTestClient.get()
        .uri("/orden/{id}", Collections.singletonMap("id", orden.getId()))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.total").isNotEmpty();
	}
	
    @Test
    void crearOrden() {
    	
    	CrearOrdenRequest request = new CrearOrdenRequest();
    	request.setIdCarrito(c.getId());
    	request.setIdUsuario(u.getId());
    	request.setTarjeta(tarjeta);
    	request.setDireccion("Av Larco");
    	
    	when(ordenService.generarOrden(any(CrearOrdenRequest.class))).thenReturn(Mono.just(orden));
    	
    	webTestClient.post()
        .uri("/orden/crear")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.total").isNotEmpty();
    }
    
    @Test
    void crearOrdenErrorNotFound() {
    	
    	CrearOrdenRequest request = new CrearOrdenRequest();
    	request.setIdCarrito(c.getId());
    	request.setIdUsuario("123");
    	request.setTarjeta(tarjeta);
    	request.setDireccion("Av Larco");
    	
    	when(ordenService.generarOrden(any(CrearOrdenRequest.class)))
    	.thenReturn(Mono.error(new ResourceNotFoundException("Carrito no encontrado en la base de datos")));
    	
    	webTestClient.post()
        .uri("/orden/crear")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus().isNotFound();
    }
    
    @Test
    void crearOrdenError500() {
    	
    	CrearOrdenRequest request = new CrearOrdenRequest();
    	request.setIdCarrito(c.getId());
    	request.setIdUsuario("123");
    	request.setTarjeta(tarjeta);
    	request.setDireccion("Av Larco");
    	
    	when(ordenService.generarOrden(any(CrearOrdenRequest.class)))
    	.thenReturn(Mono.error(new Exception("Carrito no encontrado en la base de datos")));
    	
    	webTestClient.post()
        .uri("/orden/crear")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus().is5xxServerError();
    }
    
	@Test
    void buscarPorIdCliente() {
		
		when(ordenService.buscarPorIdUsuario(any(String.class))).thenReturn(Flux.just(orden));
		
		webTestClient.get()
        .uri("/orden/listar-cliente/{id}", Collections.singletonMap("id", orden.getId()))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Orden.class)
        .consumeWith(response -> {
        	List<Orden> lista = response.getResponseBody();
        	assertThat(lista.size() == 1).isTrue();
        });
	}
	
	@Test
    void buscarPorIdClienteSinElementos() {
		
		when(ordenService.buscarPorIdUsuario(any(String.class))).thenReturn(Flux.empty());
		
		webTestClient.get()
        .uri("/orden/listar-cliente/{id}", Collections.singletonMap("id", orden.getId()))
        .exchange()
        .expectStatus().isNotFound();
	}
	
    @Test
    void modificarStatus() {
    	
    	ModificarStatusRequest request = new ModificarStatusRequest();
    	request.setIdOrden(orden.getId());
    	request.setEstado(EstadoOrden.ENVIADA);
    	
    	when(ordenService.buscarPorId(any(String.class))).thenReturn(Mono.just(orden));
    	when(ordenService.modificarEstado(any(Orden.class))).thenReturn(Mono.just(orden));
    	
    	webTestClient.post()
        .uri("/orden/modificar")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.total").isNotEmpty();
    }
}
