package com.project.producto.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "categorias")
public class Categoria {

	@Id
	private String id;
	private String nombre;
	
	public Categoria(String nombre) {
		this.nombre = nombre;
	}
	
}
