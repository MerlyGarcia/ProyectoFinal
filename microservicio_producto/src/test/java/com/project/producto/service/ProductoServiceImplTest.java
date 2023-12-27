package com.project.producto.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.producto.dao.CategoriaDao;
import com.project.producto.dao.MaterialDao;
import com.project.producto.dao.ProductoDao;
import com.project.producto.documents.Categoria;
import com.project.producto.documents.Material;
import com.project.producto.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient
public class ProductoServiceImplTest {

	@InjectMocks
	private ProductoServiceImpl productoService;

	@Mock
	private ProductoDao productoDao;
	
	@Mock
	private CategoriaDao categoriaDao;

	@Mock
	private MaterialDao materialDao;
	
    private Producto p1;
    private Producto p2;
    
    private Categoria c1;
    private Categoria c2;
    
    private Material m1;
    private Material m2;

    @BeforeEach
    public void setUp() {
    	p1 = new Producto();
		
		c1 = new Categoria();
		c1.setId("1");
		c1.setNombre("Collar");
		
		m1 = new Material();
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
		
		c2 = new Categoria();
		c1.setId("2");
		c1.setNombre("Pulsera");
		
		m2 = new Material();
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
    	
    	when(productoDao.findAll()).thenReturn(Flux.just(p1, p2));
    	
        Flux<Producto> resultado = productoService.findAll();

        StepVerifier.create(resultado)
                .expectNext(p1)
                .expectNext(p2)
                .expectComplete()
                .verify();
    }
    
    @Test
    void findByCategoria() {
    	
    	when(productoDao.findByCategoriaNombre(any(String.class))).thenReturn(Flux.just(p1, p2));
    	
    	Flux<Producto> resultado = productoService.findByCategoria("Pulsera");

        StepVerifier.create(resultado)
        		.expectNext(p1)
        		.expectNext(p2)
        		.expectComplete()
        		.verify();
    }
    
    @Test
    void findByMaterial() {
    	
    	when(productoDao.findByMaterialNombre(any(String.class))).thenReturn(Flux.just(p1, p2));
    	
    	Flux<Producto> resultado = productoService.findByMaterial("Plata");

        StepVerifier.create(resultado)
        		.expectNext(p1)
        		.expectNext(p2)
        		.expectComplete()
        		.verify();
    }
    
    @Test
    void findById() {
    	
    	when(productoDao.findById(any(String.class))).thenReturn(Mono.just(p1));
    	
        Mono<Producto> resultado = productoService.findById(p1.getId());

        StepVerifier.create(resultado)
                .expectNext(p1)
                .expectComplete()
                .verify();
    }
    
    @Test
    void registerProduct() {
    	
    	when(productoDao.save(any(Producto.class))).thenReturn(Mono.just(p1));
    	
        Mono<Producto> resultado = productoService.registerProduct(p1);

        StepVerifier.create(resultado)
                .expectNext(p1)
                .expectComplete()
                .verify();
    }
    
    @Test
    void deleteProduct() {
    	
    	when(productoDao.delete(any(Producto.class))).thenReturn(Mono.empty());
    	
        Mono<Void> resultado = productoService.deleteProduct(p1);

        StepVerifier.create(resultado)
                .expectComplete()
                .verify();
    }
    
    @Test
    void registerCategoria() {
    	
    	when(categoriaDao.save(any(Categoria.class))).thenReturn(Mono.just(c1));
    	
        Mono<Categoria> resultado = productoService.registerCategoria(c1);

        StepVerifier.create(resultado)
                .expectNext(c1)
                .expectComplete()
                .verify();
    }
    
    @Test
    void registerMaterial() {
    	
    	when(materialDao.save(any(Material.class))).thenReturn(Mono.just(m1));
    	
        Mono<Material> resultado = productoService.registerMaterial(m1);

        StepVerifier.create(resultado)
                .expectNext(m1)
                .expectComplete()
                .verify();
    }
    
}
