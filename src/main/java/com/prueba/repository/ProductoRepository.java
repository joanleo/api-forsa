package com.prueba.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.prueba.entity.Empresa;
import com.prueba.entity.Producto;
import com.prueba.security.entity.Usuario;

public interface ProductoRepository extends JpaRepository<Producto, String>, JpaSpecificationExecutor<Producto>{
		
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
	
	//public Producto findByIdProducto(Producto_id producto_id);
	
}
