package com.prueba.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.prueba.entity.Empresa;
import com.prueba.entity.Producto;
import com.prueba.entity.TipoActivo;
import com.prueba.security.entity.Usuario;

public interface ProductoRepository extends JpaRepository<Producto, Integer>, JpaSpecificationExecutor<Producto>{
		
	public Page<Producto> findAllByEmpresaAndEstaActivoTrue(Empresa empresa, Pageable page);
	
	public List<Producto> findAllByEmpresaAndEstaActivoTrue(Empresa empresa);
	
	public List<Producto> findByEmpresa(Empresa empresa);
	
	public List<Producto> findByFabricanteLike(Long fabricante);
	
	public Producto findByCodigoPieza(String codigoPieza);
	
	public Page<Producto> findByDescripcion(String letra, Boolean activo, Pageable pageable);

	/**
	 * @param usuario
	 * @return
	 */
	public Producto findByReviso(Usuario usuario);

	/**
	 * @param tipo
	 * @return
	 */
	public Producto findByTipo(TipoActivo tipo);

	/**
	 * @param tipo
	 * @return
	 */
	public Producto findOneByTipo(TipoActivo tipo);

	/**
	 * @param tipo
	 * @return
	 */
	public Producto findFirstByTipo(TipoActivo tipo);

	/**
	 * @param ubicacion
	 * @return
	 */
	@Query(value = "SELECT * FROM mov_activos "
			+ "WHERE mov_activos.nidubicacion=?1", nativeQuery = true)
	public List<Producto> findByUbicacion(Long ubicacion);
		
}
