package com.project.facturacion.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PayloadKafka {
	
	private String id;
	private Usuario usuario;
	private Carrito carrito;
	private EstadoOrden status;
	private Double total;
	private LocalDate fecha;
	private DetalleTarjeta tarjeta;
	private String direccion;
}
