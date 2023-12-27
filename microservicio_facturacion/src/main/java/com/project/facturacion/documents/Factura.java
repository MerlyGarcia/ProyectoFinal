package com.project.facturacion.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.project.facturacion.model.PayloadKafka;

import lombok.Data;

@Data
@Document(collection = "facturas")
public class Factura {
	
	@Id
	private String id;
	private PayloadKafka detalle;

}
