package com.project.ordenespagos.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class Carrito {

	private String id;
	private List<ItemCarrito> items;
	private Double total;
	private LocalDate fecha;
}
