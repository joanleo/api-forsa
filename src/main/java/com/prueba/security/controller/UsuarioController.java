package com.prueba.security.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.dto.ApiResponse;
import com.prueba.security.dto.RegistroDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/usuarios")
@Api(tags = "Usuarios", description = "Operaciones referentes a los usuarios")
public class UsuarioController {
	
	@GetMapping
	@ApiOperation(value = "Encuentra los usuarios", notes = "Retorna los usuarios de una empresa dada")
	public ApiResponse<Page<RegistroDTO>> paginationList(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@Valid@RequestBody(required=false)RegistroDTO usuario){
		
		
		return null;
	}

}
