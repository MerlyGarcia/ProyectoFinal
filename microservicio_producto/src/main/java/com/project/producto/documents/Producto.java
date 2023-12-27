package com.project.producto.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "productos")
public class Producto {

	@Id
	private String id;
	private String nombre;
	private String descripcion;
	private Integer stock;
	private Double precio;
	private String imagen;
	private String estado;
	private Categoria categoria;
	private Material material;
	
	
	public Producto(String nombre, String descripcion, Integer stock, Double precio, String estado,
			Categoria categoria, Material material) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.stock = stock;
		this.precio = precio;
		this.estado = estado;
		this.categoria = categoria;
		this.material = material;
	}
	
}
