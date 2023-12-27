package com.project.producto.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

import com.project.producto.documents.Categoria;
import com.project.producto.documents.Material;
import com.project.producto.documents.Producto;
import com.project.producto.service.ProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
public class ProductoControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    
    @MockBean
	private ProductoService productoService;
    
    private Producto p1;
    private Producto p2;

    @BeforeEach
    public void setUp() {
    	p1 = new Producto();
		
		Categoria c1 = new Categoria();
		c1.setId("1");
		c1.setNombre("Collar");
		
		Material m1 = new Material();
		m1.setId("1");
		m1.setNombre("Plata");
		
		p1.setId("1");
		p1.setNombre("Collar Mariposa Rosada");
		p1.setDescripcion("Descripción del Producto1");
		p1.setStock(25);
		p1.setPrecio(25D);
		p1.setEstado("Disponible");
		p1.setCategoria(c1);
		p1.setMaterial(m1);
		
		p2 = new Producto();
		
		Categoria c2 = new Categoria();
		c1.setId("2");
		c1.setNombre("Pulsera");
		
		Material m2 = new Material();
		m2.setId("2");
		m2.setNombre("Oro");

		p2.setId("1");
		p2.setNombre("Pulsera Clásica Relieve Dorado");
		p2.setDescripcion("Descripción del Producto2");
		p2.setStock(30);
		p2.setPrecio(50D);
		p2.setEstado("Disponible");
		p2.setCategoria(c2);
		p2.setMaterial(m2);
    }
	
	@Test
    void findAll() {
		
		when(productoService.findAll()).thenReturn(Flux.just(p1, p2));
		
		webTestClient.get()
        .uri("/producto")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Producto.class)
        .consumeWith(response -> {
        	List<Producto> lista = response.getResponseBody();
        	assertThat(lista.size() == 2).isTrue();
        });
	}
	
	@Test
    void findById() {
		
		when(productoService.findById(any(String.class))).thenReturn(Mono.just(p1));
		
		webTestClient.get()
        .uri("/producto/{id}", Collections.singletonMap("id", p1.getId()))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.nombre").isNotEmpty();
	}
	
	@Test
    void findByCategoria() {
		
		when(productoService.findByCategoria(any(String.class))).thenReturn(Flux.just(p1, p2));
		
		webTestClient.get()
        .uri("/producto/categoria/{categoria}", Collections.singletonMap("categoria", "Pulsera"))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Producto.class)
        .consumeWith(response -> {
        	List<Producto> lista = response.getResponseBody();
        	assertThat(lista.size() == 2).isTrue();
        });
	}
	
	@Test
    void findByCategoriaEmpty() {
		
		when(productoService.findByCategoria(any(String.class))).thenReturn(Flux.empty());
		
		webTestClient.get()
        .uri("/producto/categoria/{categoria}", Collections.singletonMap("categoria", "Pulsera"))
        .exchange()
        .expectStatus().isNotFound();   
	}
	
	@Test
    void findByMaterial() {
		
		when(productoService.findByMaterial(any(String.class))).thenReturn(Flux.just(p1, p2));
		
		webTestClient.get()
        .uri("/producto/material/{material}", Collections.singletonMap("material", "Oro"))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Producto.class)
        .consumeWith(response -> {
        	List<Producto> lista = response.getResponseBody();
        	assertThat(lista.size() == 2).isTrue();
        });
	}
	
	@Test
    void findByMaterialEmpty() {
		
		when(productoService.findByMaterial(any(String.class))).thenReturn(Flux.empty());
		
		webTestClient.get()
        .uri("/producto/material/{material}", Collections.singletonMap("material", "Oro"))
        .exchange()
        .expectStatus().isNotFound();   
	}
	
    @Test
    void registrarProducto() {
    	
    	when(productoService.registerProduct(any(Producto.class))).thenReturn(Mono.just(p1));
    	
    	webTestClient.post()
        .uri("/producto")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(p1)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.nombre").isNotEmpty();
    }
    
    @Test
    void eliminarProducto() {
    	
    	when(productoService.findById(any(String.class))).thenReturn(Mono.just(p1));
    	when(productoService.deleteProduct(any(Producto.class))).thenReturn(Mono.empty());
    	
    	webTestClient.delete()
        .uri("/producto/{id}", Collections.singletonMap("id", p1.getId()))
        .exchange()
        .expectStatus().isNoContent()
        .expectBody()
        .isEmpty();
    }
    
    @Test
    void restarStockProducto() {
    	
    	when(productoService.findById(any(String.class))).thenReturn(Mono.just(p1));
    	when(productoService.registerProduct(any(Producto.class))).thenReturn(Mono.just(p1));
    	
    	webTestClient.get()
        .uri("/producto/restar_stock/{id}/{cantidad}", p1.getId(), 25)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.nombre").isNotEmpty();
    }
    
    @Test
    void restarStockProductoStockInsuficiente() {
    	
    	when(productoService.findById(any(String.class))).thenReturn(Mono.just(p1));
    	when(productoService.registerProduct(any(Producto.class))).thenReturn(Mono.just(p1));
    	
    	webTestClient.get()
        .uri("/producto/restar_stock/{id}/{cantidad}", p1.getId(), 30)
        .exchange()
        .expectStatus().isBadRequest();
    }
    
    @Test
    void actualizarProducto() {
    	
    	when(productoService.findById(any(String.class))).thenReturn(Mono.just(p1));
    	when(productoService.registerProduct(any(Producto.class))).thenReturn(Mono.just(p1));
    	
    	webTestClient.put()
        .uri("/producto/{id}", Collections.singletonMap("id", p1.getId()))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(p1)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.nombre").isNotEmpty();
    }

}
