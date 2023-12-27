package com.project.carritocompras.dto;

import lombok.Data;

@Data
public class AgregarProductoRequest {

	private String idCarrito;
	private String idProducto;
	private Integer cantidad;
}
