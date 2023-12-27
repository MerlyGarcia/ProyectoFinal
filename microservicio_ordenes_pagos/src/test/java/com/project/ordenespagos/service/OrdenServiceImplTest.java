package com.project.ordenespagos.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import com.project.ordenespagos.client.CarritoClient;
import com.project.ordenespagos.client.UsuarioClient;
import com.project.ordenespagos.dao.OrdenDao;
import com.project.ordenespagos.documents.Orden;
import com.project.ordenespagos.dto.Carrito;
import com.project.ordenespagos.dto.CrearOrdenRequest;
import com.project.ordenespagos.dto.DetalleTarjeta;
import com.project.ordenespagos.dto.EstadoOrden;
import com.project.ordenespagos.dto.ItemCarrito;
import com.project.ordenespagos.dto.Rol;
import com.project.ordenespagos.dto.TipoTarjeta;
import com.project.ordenespagos.dto.Usuario;
import com.project.ordenespagos.exception.ResourceNotFoundException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient
public class OrdenServiceImplTest {

	@Autowired
	private OrdenServiceImpl ordenService;

	@MockBean
	private OrdenDao ordenDao;
	
	@MockBean
	private CarritoClient carritoClient;
	
	@MockBean
	private UsuarioClient usuarioClient;
	
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
    	
    	orden.setId("6586a8756c5a9223644f1605");
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
    	
    	when(ordenDao.findById(any(String.class))).thenReturn(Mono.just(orden));
    	
        Mono<Orden> resultado = ordenService.buscarPorId(orden.getId());

        StepVerifier.create(resultado)
                .expectNext(orden)
                .expectComplete()
                .verify();
    }
    
    @Test
    void generarOrden() {
    	
    	CrearOrdenRequest request = new CrearOrdenRequest();
    	request.setIdCarrito(c.getId());
    	request.setIdUsuario(u.getId());
    	request.setTarjeta(tarjeta);
    	request.setDireccion("Av Larco");
    	
    	when(usuarioClient.obtenerPorId(any(String.class))).thenReturn(Mono.just(ResponseEntity.ok(u)));
    	when(carritoClient.buscarCarritoPorId(any(String.class))).thenReturn(Mono.just(ResponseEntity.ok(c)));
    	when(ordenDao.save(any(Orden.class))).thenReturn(Mono.just(orden));
    	
        Mono<Orden> resultado = ordenService.generarOrden(request);

        StepVerifier.create(resultado)
                .expectNext(orden)
                .expectComplete()
                .verify();
    }
    
    @Test
    void generarOrdenCarritoNotFound() {
    	
    	CrearOrdenRequest request = new CrearOrdenRequest();
    	request.setIdCarrito(c.getId());
    	request.setIdUsuario(u.getId());
    	request.setTarjeta(tarjeta);
    	request.setDireccion("Av Larco");
    	
    	when(usuarioClient.obtenerPorId(any(String.class))).thenReturn(Mono.just(ResponseEntity.ok(u)));
    	when(carritoClient.buscarCarritoPorId(any(String.class))).thenReturn(Mono.just(ResponseEntity.notFound().build()));
    	
        Mono<Orden> resultado = ordenService.generarOrden(request);

        StepVerifier.create(resultado)
                .expectError(ResourceNotFoundException.class)
                .verify();
    }
    
    @Test
    void generarOrdenUsuarioNotFound() {
    	
    	CrearOrdenRequest request = new CrearOrdenRequest();
    	request.setIdCarrito(c.getId());
    	request.setIdUsuario(u.getId());
    	request.setTarjeta(tarjeta);
    	request.setDireccion("Av Larco");
    	
    	when(usuarioClient.obtenerPorId(any(String.class))).thenReturn(Mono.just(ResponseEntity.notFound().build()));
    	when(carritoClient.buscarCarritoPorId(any(String.class))).thenReturn(Mono.just(ResponseEntity.ok(c)));
    	
        Mono<Orden> resultado = ordenService.generarOrden(request);

        StepVerifier.create(resultado)
                .expectError(ResourceNotFoundException.class)
                .verify();
    }
    
    @Test
    void buscarPorIdUsuario() {
    	
    	when(ordenDao.findByUsuarioId(any(String.class))).thenReturn(Flux.just(orden));
    	
    	Flux<Orden> resultado = ordenService.buscarPorIdUsuario(u.getId());

        StepVerifier.create(resultado)
        		.expectNext(orden)
        		.expectComplete()
        		.verify();
    }
    
    @Test
    void modificarEstado() {
    	
    	when(ordenDao.save(any(Orden.class))).thenReturn(Mono.just(orden));
    	
    	Mono<Orden> resultado = ordenService.modificarEstado(orden);

        StepVerifier.create(resultado)
        		.expectNext(orden)
        		.expectComplete()
        		.verify();
    }
}
