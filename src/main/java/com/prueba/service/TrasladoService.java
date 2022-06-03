package com.prueba.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

import com.prueba.dto.TrasladoDTO;
import com.prueba.entity.Traslado;

public interface TrasladoService {

	TrasladoDTO create(TrasladoDTO trasladoDTO);

	Traslado getTraslado(Long id);

	Page<Traslado> findBetweenDates(LocalDate desde, LocalDate hasta, int offset, int pageSize);

}
