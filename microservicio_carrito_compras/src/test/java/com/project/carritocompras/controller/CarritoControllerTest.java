package com.project.carritocompras.controller;

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

import com.project.carritocompras.documents.Carrito;
import com.project.carritocompras.documents.ItemCarrito;
import com.project.carritocompras.dto.AgregarProductoRequest;
import com.project.carritocompras.dto.EliminarProductoRequest;
import com.project.carritocompras.dto.Producto;
import com.project.carritocompras.service.CarritoService;

import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
public class CarritoControllerTest {
	
    @Autowired
    private WebTestClient webTestClient;
    
    @MockBean
	private CarritoService carritoService;

    private Carrito c1;
    private List<ItemCarrito> items;

    @BeforeEach
    public void setUp() {
    	ItemCarrito item = new ItemCarrito();
    	item.setId("1");
    	item.setIdProducto("1");
    	item.setNombreProducto("Pulsera");
    	item.setTotal(20D);
    	item.setCantidad(2);
    	
    	items = new ArrayList<>();
    	items.add(item);
    	
    	c1 = new Carrito();
    	c1.setId("1");
    	c1.setFecha(LocalDate.now());
    	c1.setItems(items);
    	c1.setTotal(50d);
    }
    
	@Test
    void buscarCarritoPorId() {
		
		when(carritoService.buscarPorId(any(String.class))).thenReturn(Mono.just(c1));
		
		webTestClient.get()
        .uri("/carrito/{id}", Collections.singletonMap("id", c1.getId()))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.total").isNotEmpty();
	}
	
    @Test
    void crearCarrito() {
    	
    	when(carritoService.crearCarrito()).thenReturn(Mono.just(c1));
    	
    	webTestClient.get()
        .uri("/carrito/crear")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.total").isNotEmpty();
    }
    
    @Test
    void agregarProducto() {
    	
    	AgregarProductoRequest request = new AgregarProductoRequest();
    	Producto p1 = new Producto();
    	p1.setId("123");
    	request.setIdProducto(p1.getId());
    	request.setIdCarrito(c1.getId());
    	request.setCantidad(2);
    	
    	when(carritoService.agregarProductoAlCarrito(any(AgregarProductoRequest.class))).thenReturn(Mono.just(c1));
    	
    	webTestClient.post()
        .uri("/carrito/agregar")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.total").isNotEmpty();
    }
    
    @Test
    void eliminarProducto() {
    	
    	EliminarProductoRequest request = new EliminarProductoRequest();
    	Producto p1 = new Producto();
    	p1.setId("123");
    	request.setIdProducto(p1.getId());
    	request.setIdCarrito(c1.getId());
    	
    	when(carritoService.eliminarProductoDelCarrito(any(EliminarProductoRequest.class))).thenReturn(Mono.just(c1));
    	
    	webTestClient.post()
        .uri("/carrito/eliminar")
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
