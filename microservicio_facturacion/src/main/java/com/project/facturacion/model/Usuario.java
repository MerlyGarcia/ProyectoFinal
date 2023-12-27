package com.project.facturacion.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Usuario {
	private String id;	
	private String nombres;
	private String apellidos;
	private String email;
	private LocalDate fechaNacimiento;
	private Rol rol;
}
