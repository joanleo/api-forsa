package com.prueba.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.Traslado;
import com.prueba.security.entity.Usuario;

public interface TrasladoRepository extends JpaRepository<Traslado, Long> {
	
	Page<Traslado> findByFechaSalidaBetween(LocalDate desde, LocalDate hasta, Pageable pageable);

	/**
	 * @param usuario
	 * @param usuario2
	 * @return
	 */
	public Traslado findByUsuarioEnvioOrUsuarioRecibe(Usuario usuario, Usuario usuario2);

}
