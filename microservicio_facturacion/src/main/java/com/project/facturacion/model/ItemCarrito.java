package com.project.facturacion.model;

import lombok.Data;

@Data
public class ItemCarrito {

	private String id;
	private String idProducto;
	private String nombreProducto;
	private Double total;
	private Integer cantidad;
}
