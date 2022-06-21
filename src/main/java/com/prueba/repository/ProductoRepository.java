package com.prueba.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.entity.Empresa;
import com.prueba.entity.Producto;
import com.prueba.entity.Producto_id;

public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto>{
	
	public Page<Producto> findAllByEstaActivoTrue(Pageable page);
	
	public List<Producto> findAllByEstaActivoTrue();
	
	public List<Producto> findByEmpresa(Empresa empresa);
	
	public List<Producto> findByFabricanteLike(Long fabricante);
	
	public Producto findByCodigoPieza(String codigoPieza);
	
	public Page<Producto> findByDescripcion(String letra, Boolean activo, Pageable pageable);
	
	public Producto findByProducto_id(Producto_id producto_id);
	
}
