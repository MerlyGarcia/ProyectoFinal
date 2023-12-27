package com.project.usuario.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.usuario.dao.UsuarioDao;
import com.project.usuario.documents.Usuario;

import reactor.core.publisher.Mono;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired
	private UsuarioDao usuarioDao;
	
	@Override
	@Transactional(readOnly = true)
	public Mono<Usuario> findById(String id) {
		return usuarioDao.findById(id);
	}

	@Override
	@Transactional
	public Mono<Usuario> createdUser(Usuario usuario) {
		return usuarioDao.save(usuario);
	}

	@Override
	@Transactional
	public Mono<Void> deleteUser(Usuario usuario) {
		return usuarioDao.delete(usuario);
	}

}
