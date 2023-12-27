package com.project.carritocompras.documents;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "carrito")
@NoArgsConstructor
public class Carrito {
	
	@Id
	private String id;
	
	private List<ItemCarrito> items;
	private Double total;
	private LocalDate fecha;
	
	public Carrito(List<ItemCarrito> items, Double total, LocalDate fecha) {
		this.items = items;
		this.total = total;
		this.fecha = fecha;
	}

}
