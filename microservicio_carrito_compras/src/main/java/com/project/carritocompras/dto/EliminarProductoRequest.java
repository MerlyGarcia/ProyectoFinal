package com.project.carritocompras.dto;

import lombok.Data;

@Data
public class EliminarProductoRequest {
	
	private String idCarrito;
	private String idProducto;

}
