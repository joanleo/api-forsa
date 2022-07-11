package com.prueba.security.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.security.entity.Ruta;

public interface RutaRepository extends JpaRepository<Ruta, Long>{

	public Ruta findByRutaAndMetodo(String ruta, String metodo);

	public void save(List<Ruta> rutas);

	//public List<Ruta> findByEmpresa(Empresa empresa);

}
