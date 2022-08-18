package com.prueba.service;

import org.springframework.data.domain.Page;

import com.prueba.dto.MovInventarioDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.MovInventario;

public interface MovInventarioService {

	MovInventario create(MovInventarioDTO movInventarioDto);

	//Page<MovInventario> searchInv(String letras, Integer pagina, Integer items);

	//Page<MovInventario> list(Integer pagina, Integer items);

	//MovInventario getInventario(Long id);

	Page<MovInventario> searchInv(String letras, Empresa empresa, Integer pagina, Integer items);

	Page<MovInventario> list(Empresa empresa, Integer pagina, Integer items);

	/**
	 * @param id
	 * @return
	 */
	MovInventario getInventario(Integer id);

}
