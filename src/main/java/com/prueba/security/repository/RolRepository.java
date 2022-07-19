package com.prueba.security.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.prueba.entity.Empresa;
import com.prueba.security.entity.Rol;




public interface RolRepository extends JpaRepository<Rol, Long>, JpaSpecificationExecutor<Rol>{

	public Rol findByNombre(String nombre);

	public Rol findByNombreAndEmpresaAndEstaActivoTrue(String nombre, Empresa empresa);

	public List<Rol> findByEmpresaAndEstaActivoTrue(Empresa empresa);

	public List<Rol> findByNombreContainsAndEmpresaAndEstaActivoTrue(String letras, Empresa empresa);
	
	Page<Rol> findByEmpresaAndEstaActivoTrue(Empresa empresa, Pageable page);

	public Rol findFirstByOrderByIdRolAsc();

	public Rol findByIdRol(Long id);

	/**
	 * @param idRol
	 * @return
	 */
	public Rol findByIdRolAndEstaActivoTrue(Long idRol);

	/**
	 * @param idRol
	 * @param empresa
	 * @return
	 */
	public Rol findByIdRolAndEmpresaAndEstaActivoTrue(Long idRol, Empresa empresa);
}
