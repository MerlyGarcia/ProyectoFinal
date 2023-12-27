package com.project.ordenespagos.documents;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.project.ordenespagos.dto.Carrito;
import com.project.ordenespagos.dto.DetalleTarjeta;
import com.project.ordenespagos.dto.EstadoOrden;
import com.project.ordenespagos.dto.Usuario;

import lombok.Data;

@Data
@Document(collection = "ordenes")
public class Orden {

	@Id
	private String id;
	private Usuario usuario;
	private Carrito carrito;
	private EstadoOrden status;
	private Double total;
	private LocalDate fecha;
	private DetalleTarjeta tarjeta;
	private String direccion;
}
