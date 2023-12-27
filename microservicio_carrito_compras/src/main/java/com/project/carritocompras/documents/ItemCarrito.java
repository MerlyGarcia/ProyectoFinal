package com.project.carritocompras.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "item_carrito")
public class ItemCarrito {

	@Id
	private String id;
	
	@Field("id_producto")
	private String idProducto;
	
	@Field("nombre_producto")
	private String nombreProducto;
	private Double total;
	private Integer cantidad;
}
