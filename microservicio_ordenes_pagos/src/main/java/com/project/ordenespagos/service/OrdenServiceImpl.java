package com.project.ordenespagos.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.ordenespagos.client.CarritoClient;
import com.project.ordenespagos.client.UsuarioClient;
import com.project.ordenespagos.dao.OrdenDao;
import com.project.ordenespagos.documents.Orden;
import com.project.ordenespagos.dto.Carrito;
import com.project.ordenespagos.dto.CrearOrdenRequest;
import com.project.ordenespagos.dto.EstadoOrden;
import com.project.ordenespagos.dto.Usuario;
import com.project.ordenespagos.exception.ResourceNotFoundException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrdenServiceImpl implements OrdenService {

	private static Logger logger = LoggerFactory.getLogger(OrdenServiceImpl.class);
	
	private final StreamBridge streamBridge;

	@Autowired
	private OrdenDao ordenDao;
	
	@Autowired
	private CarritoClient carritoClient;
	
	@Autowired
	private UsuarioClient usuarioClient;
	
	public OrdenServiceImpl(StreamBridge streamBridge) {
	        this.streamBridge = streamBridge;
	}
	
	@Override
	public Mono<Orden> buscarPorId(String id) {
		return ordenDao.findById(id);
	}
	
	@Override
	public Mono<Orden> generarOrden(CrearOrdenRequest request) {
		
		logger.info("***** Inicio Service GenerarOrden *****");

		logger.info("Obtener Usuario por Id");
		Mono<ResponseEntity<Usuario>> usuarioMono = usuarioClient.obtenerPorId(request.getIdUsuario());
		
		logger.info("Obtener Carrito por Id");
		Mono<ResponseEntity<Carrito>> carritoMono = carritoClient.buscarCarritoPorId(request.getIdCarrito());
		
		return Mono.zip(usuarioMono, carritoMono).flatMap(tuple -> {
			ResponseEntity<Usuario> usuarioResponse = tuple.getT1();
			ResponseEntity<Carrito> carritoResponse = tuple.getT2();
			
			if (usuarioResponse.getStatusCode() == HttpStatus.OK) {
				Usuario usuario = usuarioResponse.getBody();
				
				if (carritoResponse.getStatusCode() == HttpStatus.OK) {
					Carrito carrito = carritoResponse.getBody();
					
					Orden orden = new Orden();
					
					orden.setUsuario(usuario);
					orden.setCarrito(carrito);
					orden.setFecha(LocalDate.now());
					orden.setTotal(carrito.getTotal());
					orden.setStatus(EstadoOrden.CREADA);
					orden.setTarjeta(request.getTarjeta());
					orden.setDireccion(request.getDireccion());
					
					return ordenDao.save(orden)
							.map(o -> {
								logger.info("Generando la factura del cliente " + orden.getUsuario().getNombres() + " " + orden.getUsuario().getApellidos());
								streamBridge.send("proyecto_topic", orden);
								return o;
							});
				}
				
				logger.info("Carrito no encontrado");
				logger.info("***** Fin Service GenerarOrden *****");
				return Mono.error(new ResourceNotFoundException("Carrito no encontrado en la base de datos"));
			}
			
			logger.info("Usuario no encontrado");
			logger.info("***** Fin Service GenerarOrden *****");
			return Mono.error(new ResourceNotFoundException("Usuario no encontrado en la base de datos"));
			
		});
	}

	@Override
	public Flux<Orden> buscarPorIdUsuario(String id) {
		return ordenDao.findByUsuarioId(id);
	}

	@Override
	public Mono<Orden> modificarEstado(Orden orden) {
		return ordenDao.save(orden);
	}

}
