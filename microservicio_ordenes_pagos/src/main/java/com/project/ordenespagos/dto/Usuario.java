package com.project.ordenespagos.dto;

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
