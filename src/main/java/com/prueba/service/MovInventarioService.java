package com.prueba.service;

import org.springframework.data.domain.Page;

import com.prueba.dto.MovInventarioDTO;
import com.prueba.entity.MovInventario;

public interface MovInventarioService {

	MovInventarioDTO create(MovInventarioDTO movInventarioDto);

	Page<MovInventario> searchInv(String letras, Integer pagina, Integer items);

	Page<MovInventario> list(Integer pagina, Integer items);

	MovInventario getInventario(Long id);

}
