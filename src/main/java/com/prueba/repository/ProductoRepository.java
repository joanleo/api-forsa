package com.prueba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.prueba.entity.Empresa;
import com.prueba.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto>{
	
	public List<Producto> findAll();
	
	public List<Producto> findByEmpresa(Empresa empresa);
	
	public List<Producto> findByFabricanteLike(Long fabricante);
	
	public Producto findByCodigoPieza(Long codigoPieza);
	
	List<Producto> findByDescripcionContains(String letra);
	
	/*@Query(
			value = "SELECT * FROM productos as p WHERE p.vcarea LIKE %:area%",
			nativeQuery = true
			)
	public List<Producto> search(@Param("area") String area);
	
	@Query(
			value = "SELECT * FROM productos as p WHERE p.vcarea LIKE %:area% AND p.vccodigopieza LIKE %:codigopieza%",
			nativeQuery = true
			)
	public List<Producto> search(@Param("area") String area, @Param("codigopieza") Long codigopieza);
	
	@Query(
			value = "SELECT * FROM productos as p WHERE p.vcarea LIKE %:area% OR p.vccodigopieza LIKE %:codigopieza% OR"
					+ "p.vcdescripcion LIKE %:descripcion%",
			nativeQuery = true
			)
	public List<Producto> search(@Param("area") String area, @Param("codigopieza") Long codigopieza,
			@Param("descripcion") String descripcion);*/

}
