package com.prueba.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.security.entity.Rol;




public interface RolRepository extends JpaRepository<Rol, Long>{

	public Rol findByNombre(String nombre);
}
