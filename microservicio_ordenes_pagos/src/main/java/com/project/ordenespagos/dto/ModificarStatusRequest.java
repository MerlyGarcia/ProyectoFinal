package com.project.ordenespagos.dto;

import lombok.Data;

@Data
public class ModificarStatusRequest {
	
	private String idOrden;
	private EstadoOrden estado;
}
