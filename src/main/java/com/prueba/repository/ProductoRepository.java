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

public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto>{
	
	public Page<Producto> findAllByEstaActivoTrue(Pageable page);
	
	public List<Producto> findAllByEstaActivoTrue();
	
	public List<Producto> findByEmpresa(Empresa empresa);
	
	public List<Producto> findByFabricanteLike(Long fabricante);
	
	public Producto findByCodigoPieza(String codigoPieza);
	
	public Page<Producto> findByDescripcion(String letra, Boolean activo, Pageable pageable);
	
	@Modifying
    @Transactional
    @Query (value="LOAD DATA INFILE 'src/main/resources/procts.txt' INTO TABLE mov_activos FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n'", nativeQuery = true)
    public void bulkLoadData();
	
}
