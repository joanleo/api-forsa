package com.prueba.security.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.security.entity.PoliRol;
import com.prueba.security.entity.Ruta;

public interface PoliRolRepository extends JpaRepository<PoliRol, Long> {
	
	public PoliRol findByNombre(String nombre);

	//public PoliRol findByNombreAndMetodo(String nombre, String metodo);

	public PoliRol findByRuta(Ruta ruta);

	public PoliRol findByRutaAndPermitidoTrue(Ruta ruta);

	public void saveAndFlush(List<PoliRol> politicas);

}
