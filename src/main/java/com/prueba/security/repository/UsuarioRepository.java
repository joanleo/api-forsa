package com.prueba.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.security.entity.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long>  {

	Usuario findByEmail(String email);

	public Optional<Usuario> findByUsernameOrEmail(String username, String email);

	public Optional<Usuario> findByUsername(String username);

	public Boolean existsByUsername(String username);

	public Boolean existsByEmail(String email);
	
	Usuario findByNombre(String nombre);
}
