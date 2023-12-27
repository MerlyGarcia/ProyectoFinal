package com.project.usuario.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.project.usuario.documents.Usuario;

public interface UsuarioDao extends ReactiveMongoRepository<Usuario, String>{

}
