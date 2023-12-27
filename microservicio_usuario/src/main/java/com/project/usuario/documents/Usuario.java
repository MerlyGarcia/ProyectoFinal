package com.project.usuario.documents;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.project.usuario.dto.Rol;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "usuarios")
@NoArgsConstructor
public class Usuario {
	
	@Id
	private String id;
	
	@NotEmpty
	private String nombres;
	
	@NotEmpty
	private String apellidos;
	
	@NotEmpty
	private String email;
	private LocalDate fechaNacimiento;
	
	@NotNull
	private Rol rol;
	
}
