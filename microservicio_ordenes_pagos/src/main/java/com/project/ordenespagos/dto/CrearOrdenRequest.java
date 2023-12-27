package com.project.ordenespagos.dto;

import lombok.Data;

@Data
public class CrearOrdenRequest {

	private String idUsuario;
	private String idCarrito;
	private DetalleTarjeta tarjeta;
	private String direccion;
}
