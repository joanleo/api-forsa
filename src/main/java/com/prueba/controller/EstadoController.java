package com.prueba.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.dto.EstadoDTO;
import com.prueba.service.EstadoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/estados")
@Api(tags = "Estado", description="Operaciones referentes al estado del activo")
public class EstadoController {

	@Autowired
	private EstadoService estadoService;
	
	@PostMapping
	@ApiOperation(value = "Crea un estado para los activos", notes = "Crea un nuevo estado para los activos")
	public ResponseEntity<EstadoDTO> create(@Valid @RequestBody EstadoDTO estadoDTO){
		return new ResponseEntity<EstadoDTO>(estadoService.create(estadoDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	@ApiOperation(value = "Encuentra los estados de los activos", notes = "Retorna los estados que pueden tomar los activos que en su nombre contengan las letras indicadas, retorna todos los estados si no se especifica ninguna letra")
	public List<EstadoDTO> list(@RequestParam(required=false) String letters){
		if(letters != null) {
			return estadoService.findByTipo(letters);
		}else {
			return estadoService.list();			
		}
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Encuentra un estado", notes = "Retorna un estado segun su id")
	public ResponseEntity<EstadoDTO> get(@PathVariable(name = "id") Long id){
		return ResponseEntity.ok(estadoService.getEstado(id));
	}
}

