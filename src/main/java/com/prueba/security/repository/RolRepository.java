package com.prueba.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Empresa;
import com.prueba.security.entity.Rol;




public interface RolRepository extends JpaRepository<Rol, Long>{

	public Rol findByNombre(String nombre);

	public Rol findByNombreAndEmpresaAndEstaActivoTrue(String nombre, Empresa empresa);

	public List<Rol> findByEmpresaAndEstaActivoTrue(Empresa empresa);

	public List<Rol> findByNombreContainsAndEmpresaAndEstaActivoTrue(String letras, Empresa empresa);
}
