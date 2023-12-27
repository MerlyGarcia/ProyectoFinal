package com.project.carritocompras.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

	private String id;
	private String nombre;
	private String descripcion;
	private Integer stock;
	private Double precio;
	private String imagen;
	private String estado;
	private Categoria categoria;
	private Material material;
	
}
