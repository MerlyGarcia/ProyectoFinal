package com.project.producto.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "materiales")
public class Material {
	
	@Id
	private String id;
	private String nombre;
	
	public Material(String nombre) {
		this.nombre = nombre;
	}

}
