package com.project.carritocompras.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.carritocompras.client.ProductoClient;
import com.project.carritocompras.dao.CarritoDao;
import com.project.carritocompras.dao.ItemCarritoDao;
import com.project.carritocompras.documents.Carrito;
import com.project.carritocompras.documents.ItemCarrito;
import com.project.carritocompras.dto.AgregarProductoRequest;
import com.project.carritocompras.dto.EliminarProductoRequest;
import com.project.carritocompras.dto.Producto;

import reactor.core.publisher.Mono;

@Service
public class CarritoServiceImpl implements CarritoService {

	private static Logger logger = LoggerFactory.getLogger(CarritoServiceImpl.class);

	@Autowired
	private ProductoClient productoClient;

	@Autowired
	private CarritoDao carritoDao;

	@Autowired
	private ItemCarritoDao itemCarritoDao;
	
	@Override
	public Mono<Carrito> buscarPorId(String id) {
		return carritoDao.findById(id);
	}

	@Override
	public Mono<Carrito> crearCarrito() {

		return carritoDao.save(new Carrito(new ArrayList<>(), 0.0, LocalDate.now()));
	}

	@Override
	public Mono<Carrito> agregarProductoAlCarrito(AgregarProductoRequest request) {

		logger.info("***** Inicio Service AgregarProductoAlCarrito *****");

		logger.info("Obtener carrito por Id");
		Mono<Carrito> carritoMono = carritoDao.findById(request.getIdCarrito());

		logger.info("Obtener producto por Id");
		Mono<ResponseEntity<Producto>> productoMono = productoClient.obtenerPorId(request.getIdProducto());

		return Mono.zip(carritoMono, productoMono).flatMap(tuple -> {
			Carrito carrito = tuple.getT1();
			ResponseEntity<Producto> productoResponse = tuple.getT2();

			if (productoResponse.getStatusCode() == HttpStatus.OK) {
				Producto producto = productoResponse.getBody();
				
				List<ItemCarrito> items = carrito.getItems();

				Optional<ItemCarrito> itemExistente = items.stream()
						.filter(i -> i.getIdProducto().equals(request.getIdProducto())).findFirst();
				
				if (itemExistente.isPresent()) {
					ItemCarrito item = itemExistente.get();
					Double totalAnterior = item.getTotal();
					Integer nuevoStock = producto.getStock() + item.getCantidad();
					
					if (nuevoStock >= request.getCantidad()) {
						producto.setStock(nuevoStock - request.getCantidad());
						
						item.setCantidad(request.getCantidad());
						item.setTotal(item.getCantidad() * producto.getPrecio());
						carrito.setTotal(carrito.getTotal() - totalAnterior + item.getTotal());
						
						logger.info("Actualizando Item");
						logger.info("***** Fin Service AgregarProductoAlCarrito *****");
						return productoClient.actualizarProducto(producto.getId(), producto)
								.then(itemCarritoDao.save(item))
								.then(carritoDao.save(carrito));
					}
					
					logger.info("Cantidad a agregar excede el stock disponible");
					logger.info("***** Fin Service AgregarProductoAlCarrito *****");
					return Mono.just(carrito);
					
				} else {
					if (producto.getStock() >= request.getCantidad()) {
						ItemCarrito nuevoItem = new ItemCarrito();
						nuevoItem.setIdProducto(request.getIdProducto());
						nuevoItem.setNombreProducto(producto.getNombre());
						nuevoItem.setCantidad(request.getCantidad());
						nuevoItem.setTotal(producto.getPrecio() * request.getCantidad());

						items.add(nuevoItem);
						carrito.setTotal(carrito.getTotal() + nuevoItem.getTotal());
						
						logger.info("Registrando nuevo Item");
						logger.info("***** Fin Service AgregarProductoAlCarrito *****");
						
						return productoClient.restarStockProducto(producto.getId(), request.getCantidad())
								.then(itemCarritoDao.save(nuevoItem))
								.then(carritoDao.save(carrito));
					}
					
					logger.info("Cantidad a agregar excede el stock disponible");
					logger.info("***** Fin Service AgregarProductoAlCarrito *****");
					return Mono.just(carrito);
				}
			}

			logger.info("Producto no encontrado");
			logger.info("***** Fin Service AgregarProductoAlCarrito *****");
			return Mono.just(carrito);

		});
	}

	@Override
	public Mono<Carrito> eliminarProductoDelCarrito(EliminarProductoRequest request) {
		
		logger.info("***** Inicio Service EliminarProductoDelCarrito *****");

		logger.info("Obtener carrito por Id");
		Mono<Carrito> carritoMono = carritoDao.findById(request.getIdCarrito());
		
		logger.info("Obtener producto por Id");
		Mono<ResponseEntity<Producto>> productoMono = productoClient.obtenerPorId(request.getIdProducto());
		
		return Mono.zip(carritoMono, productoMono).flatMap(tuple -> {
			Carrito carrito = tuple.getT1();
			ResponseEntity<Producto> productoResponse = tuple.getT2();

			if (productoResponse.getStatusCode() == HttpStatus.OK) {
				Producto producto = productoResponse.getBody();

					List<ItemCarrito> items = carrito.getItems();

					Optional<ItemCarrito> itemExistente = items.stream()
							.filter(i -> i.getIdProducto().equals(request.getIdProducto())).findFirst();

					if (itemExistente.isPresent()) {
						ItemCarrito item = itemExistente.get();
						Double totalItem = item.getTotal();
						
						carrito.setTotal(carrito.getTotal() - totalItem);
						producto.setStock(producto.getStock() + item.getCantidad());
						carrito.getItems().remove(item);
						logger.info("***** Fin Service EliminarProductoDelCarrito *****");
						return productoClient.actualizarProducto(producto.getId(), producto)
								.then(itemCarritoDao.delete(item))
								.then(carritoDao.save(carrito));
					}
			}

			logger.info("Producto no encontrado");
			logger.info("***** Fin Service AgregarProductoAlCarrito *****");
			return Mono.just(carrito);

		});
	}
}
