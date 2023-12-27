package com.project.carritocompras.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.project.carritocompras.client.ProductoClient;
import com.project.carritocompras.dao.CarritoDao;
import com.project.carritocompras.dao.ItemCarritoDao;
import com.project.carritocompras.documents.Carrito;
import com.project.carritocompras.documents.ItemCarrito;
import com.project.carritocompras.dto.AgregarProductoRequest;
import com.project.carritocompras.dto.Categoria;
import com.project.carritocompras.dto.EliminarProductoRequest;
import com.project.carritocompras.dto.Material;
import com.project.carritocompras.dto.Producto;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient
public class CarritoServiceImplTest {

	@InjectMocks
	private CarritoServiceImpl carritoService;

	@Mock
	private ProductoClient productoClient;

	@Mock
	private CarritoDao carritoDao;

	@Mock
	private ItemCarritoDao itemCarritoDao;
	
	private Carrito c1;
    private List<ItemCarrito> items;
    private ItemCarrito item;

    @BeforeEach
    public void setUp() {
    	item = new ItemCarrito();
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
    void buscarPorId() {
    	
    	when(carritoDao.findById(any(String.class))).thenReturn(Mono.just(c1));
    	
        Mono<Carrito> resultado = carritoService.buscarPorId(c1.getId());

        StepVerifier.create(resultado)
                .expectNext(c1)
                .expectComplete()
                .verify();
    }
    
    @Test
    void crearCarrito() {
    	
    	when(carritoDao.save(any(Carrito.class))).thenReturn(Mono.just(c1));
    	
        Mono<Carrito> resultado = carritoService.crearCarrito();

        StepVerifier.create(resultado)
                .expectNext(c1)
                .expectComplete()
                .verify();
    }
    
    @Test
    void agregarProductoAlCarrito() {
    	Producto p1 = new Producto();
    	p1.setId("123");
		
    	Categoria ca1 = new Categoria();
		ca1.setId("1");
		ca1.setNombre("Collar");
		
		Material m1 = new Material();
		m1.setId("1");
		m1.setNombre("Plata");
		
		p1.setId("1");
		p1.setNombre("Collar Mariposa Rosada");
		p1.setDescripcion("Descripción del Producto1");
		p1.setStock(25);
		p1.setPrecio(25D);
		p1.setEstado("Disponible");
		p1.setCategoria(ca1);
		p1.setMaterial(m1);
		
		AgregarProductoRequest request = new AgregarProductoRequest();
		request.setIdCarrito(c1.getId());
		request.setIdProducto(p1.getId());
		request.setCantidad(2);
    	
    	when(carritoDao.findById(any(String.class))).thenReturn(Mono.just(c1));
    	when(productoClient.obtenerPorId(any(String.class))).thenReturn(Mono.just(ResponseEntity.ok(p1)));
    	when(productoClient.actualizarProducto(any(String.class), any(Producto.class))).thenReturn(Mono.just(ResponseEntity.ok(p1)));
    	when(itemCarritoDao.save(any(ItemCarrito.class))).thenReturn(Mono.just(item));
    	when(carritoDao.save(any(Carrito.class))).thenReturn(Mono.just(c1));
    	
    	
        Mono<Carrito> resultado = carritoService.agregarProductoAlCarrito(request);

        StepVerifier.create(resultado)
                .expectNext(c1)
                .expectComplete()
                .verify();
    }
    
    @Test
    void agregarProductoAlCarritoStockInsuficiente() {
    	Producto p1 = new Producto();
    	p1.setId("123");
		
    	Categoria ca1 = new Categoria();
		ca1.setId("1");
		ca1.setNombre("Collar");
		
		Material m1 = new Material();
		m1.setId("1");
		m1.setNombre("Plata");
		
		p1.setId("1");
		p1.setNombre("Collar Mariposa Rosada");
		p1.setDescripcion("Descripción del Producto1");
		p1.setStock(25);
		p1.setPrecio(25D);
		p1.setEstado("Disponible");
		p1.setCategoria(ca1);
		p1.setMaterial(m1);
		
		AgregarProductoRequest request = new AgregarProductoRequest();
		request.setIdCarrito(c1.getId());
		request.setIdProducto(p1.getId());
		request.setCantidad(30);
    	
    	when(carritoDao.findById(any(String.class))).thenReturn(Mono.just(c1));
    	when(productoClient.obtenerPorId(any(String.class))).thenReturn(Mono.just(ResponseEntity.ok(p1)));
    	
        Mono<Carrito> resultado = carritoService.agregarProductoAlCarrito(request);

        StepVerifier.create(resultado)
                .expectNext(c1)
                .expectComplete()
                .verify();
    }
    
    @Test
    void agregarProductoAlCarritoItemNuevo() {
    	Producto p1 = new Producto();
    	p1.setId("123");
		
    	Categoria ca1 = new Categoria();
		ca1.setId("1");
		ca1.setNombre("Collar");
		
		Material m1 = new Material();
		m1.setId("1");
		m1.setNombre("Plata");
		
		p1.setId("1");
		p1.setNombre("Collar Mariposa Rosada");
		p1.setDescripcion("Descripción del Producto1");
		p1.setStock(25);
		p1.setPrecio(25D);
		p1.setEstado("Disponible");
		p1.setCategoria(ca1);
		p1.setMaterial(m1);
		
		AgregarProductoRequest request = new AgregarProductoRequest();
		request.setIdCarrito(c1.getId());
		request.setIdProducto("12345");
		request.setCantidad(2);
    	
    	when(carritoDao.findById(any(String.class))).thenReturn(Mono.just(c1));
    	when(productoClient.obtenerPorId(any(String.class))).thenReturn(Mono.just(ResponseEntity.ok(p1)));
    	when(productoClient.restarStockProducto(any(String.class), any(Integer.class))).thenReturn(Mono.just(ResponseEntity.ok(p1)));
    	when(itemCarritoDao.save(any(ItemCarrito.class))).thenReturn(Mono.just(item));
    	when(carritoDao.save(any(Carrito.class))).thenReturn(Mono.just(c1));
    	
    	
        Mono<Carrito> resultado = carritoService.agregarProductoAlCarrito(request);

        StepVerifier.create(resultado)
                .expectNext(c1)
                .expectComplete()
                .verify();
    }
    
    @Test
    void agregarProductoAlCarritoItemNuevoStockInsuficiente() {
    	Producto p1 = new Producto();
    	p1.setId("123");
		
    	Categoria ca1 = new Categoria();
		ca1.setId("1");
		ca1.setNombre("Collar");
		
		Material m1 = new Material();
		m1.setId("1");
		m1.setNombre("Plata");
		
		p1.setId("1");
		p1.setNombre("Collar Mariposa Rosada");
		p1.setDescripcion("Descripción del Producto1");
		p1.setStock(25);
		p1.setPrecio(25D);
		p1.setEstado("Disponible");
		p1.setCategoria(ca1);
		p1.setMaterial(m1);
		
		AgregarProductoRequest request = new AgregarProductoRequest();
		request.setIdCarrito(c1.getId());
		request.setIdProducto("12345");
		request.setCantidad(30);
    	
    	when(carritoDao.findById(any(String.class))).thenReturn(Mono.just(c1));
    	when(productoClient.obtenerPorId(any(String.class))).thenReturn(Mono.just(ResponseEntity.ok(p1)));
    	
        Mono<Carrito> resultado = carritoService.agregarProductoAlCarrito(request);

        StepVerifier.create(resultado)
                .expectNext(c1)
                .expectComplete()
                .verify();
    }
    
    @Test
    void agregarProductoAlCarritoProductoNoEncontrado() {
    	Producto p1 = new Producto();
    	p1.setId("123");
		
    	Categoria ca1 = new Categoria();
		ca1.setId("1");
		ca1.setNombre("Collar");
		
		Material m1 = new Material();
		m1.setId("1");
		m1.setNombre("Plata");
		
		p1.setId("1");
		p1.setNombre("Collar Mariposa Rosada");
		p1.setDescripcion("Descripción del Producto1");
		p1.setStock(25);
		p1.setPrecio(25D);
		p1.setEstado("Disponible");
		p1.setCategoria(ca1);
		p1.setMaterial(m1);
		
		AgregarProductoRequest request = new AgregarProductoRequest();
		request.setIdCarrito(c1.getId());
		request.setIdProducto("12345");
		request.setCantidad(30);
    	
    	when(carritoDao.findById(any(String.class))).thenReturn(Mono.just(c1));
    	when(productoClient.obtenerPorId(any(String.class))).thenReturn(Mono.just(ResponseEntity.notFound().build()));
    	
        Mono<Carrito> resultado = carritoService.agregarProductoAlCarrito(request);

        StepVerifier.create(resultado)
                .expectNext(c1)
                .expectComplete()
                .verify();
    }
    
    @Test
    void eliminarProductoDelCarrito() {
    	Producto p1 = new Producto();
    	p1.setId("123");
		
    	Categoria ca1 = new Categoria();
		ca1.setId("1");
		ca1.setNombre("Collar");
		
		Material m1 = new Material();
		m1.setId("1");
		m1.setNombre("Plata");
		
		p1.setId("1");
		p1.setNombre("Collar Mariposa Rosada");
		p1.setDescripcion("Descripción del Producto1");
		p1.setStock(25);
		p1.setPrecio(25D);
		p1.setEstado("Disponible");
		p1.setCategoria(ca1);
		p1.setMaterial(m1);
		
		EliminarProductoRequest request = new EliminarProductoRequest();
		request.setIdCarrito(c1.getId());
		request.setIdProducto(p1.getId());
    	
    	when(carritoDao.findById(any(String.class))).thenReturn(Mono.just(c1));
    	when(productoClient.obtenerPorId(any(String.class))).thenReturn(Mono.just(ResponseEntity.ok(p1)));
    	when(productoClient.actualizarProducto(any(String.class), any(Producto.class))).thenReturn(Mono.just(ResponseEntity.ok(p1)));
    	when(itemCarritoDao.delete(any(ItemCarrito.class))).thenReturn(Mono.empty());
    	when(carritoDao.save(any(Carrito.class))).thenReturn(Mono.just(c1));
    	
    	
        Mono<Carrito> resultado = carritoService.eliminarProductoDelCarrito(request);

        StepVerifier.create(resultado)
                .expectNext(c1)
                .expectComplete()
                .verify();
    }
    
    @Test
    void eliminarProductoDelCarritoProductoNoEncontrado() {
    	Producto p1 = new Producto();
    	p1.setId("123");
		
    	Categoria ca1 = new Categoria();
		ca1.setId("1");
		ca1.setNombre("Collar");
		
		Material m1 = new Material();
		m1.setId("1");
		m1.setNombre("Plata");
		
		p1.setId("1");
		p1.setNombre("Collar Mariposa Rosada");
		p1.setDescripcion("Descripción del Producto1");
		p1.setStock(25);
		p1.setPrecio(25D);
		p1.setEstado("Disponible");
		p1.setCategoria(ca1);
		p1.setMaterial(m1);
		
		EliminarProductoRequest request = new EliminarProductoRequest();
		request.setIdCarrito(c1.getId());
		request.setIdProducto(p1.getId());
    	
    	when(carritoDao.findById(any(String.class))).thenReturn(Mono.just(c1));
    	when(productoClient.obtenerPorId(any(String.class))).thenReturn(Mono.just(ResponseEntity.notFound().build()));
    	
        Mono<Carrito> resultado = carritoService.eliminarProductoDelCarrito(request);

        StepVerifier.create(resultado)
                .expectNext(c1)
                .expectComplete()
                .verify();
    }
}
