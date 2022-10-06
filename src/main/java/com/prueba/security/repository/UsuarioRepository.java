package com.prueba.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prueba.entity.Empresa;
import com.prueba.security.entity.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> , JpaSpecificationExecutor<Usuario> {

	@Query("SELECT u FROM Usuario u WHERE u.email=:email")
	Usuario findByEmail(@Param("email") String email);

	public Optional<Usuario> findByNombreUsuarioOrEmail(String username, String email);

	public Optional<Usuario> findByNombreUsuario(String username);

	public Boolean existsByNombreUsuario(String username);

	public Boolean existsByEmail(String email);
	
	Usuario findByNombre(String nombre);
	
	Optional<Usuario> findByTokenPassword(String token);

	Usuario findByNombreAndEmpresa(String nombre, Empresa Empresa);

	Page<Usuario> findByEmpresaAndEstaActivoTrue(Empresa empresa, Pageable page);

	List<Usuario> findByNombreContainsAndEmpresaAndEstaActivoTrue(String letras, Empresa empresa);

	Usuario findByIdAndEmpresa(Long id, Empresa empresa);

	List<Usuario> findByEmpresaAndEstaActivoTrue(Empresa empresa);

	/**
	 * @param nombreUsuario
	 * @param empresa
	 * @return
	 */
	Usuario findByNombreUsuarioAndEmpresa(String nombreUsuario, Empresa empresa);


}
