package com.prueba.security.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.security.dto.RolDTO;
import com.prueba.security.service.RolService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/roles")
@Api(tags = "Roles",description = "Operaciones referentes a los roles de los usuarios")
public class RolController {

	@Autowired
	private RolService rolService;
	
	@PostMapping
	@ApiOperation(value = "Crear un rol", notes = "Crea un nuevo rol")
	public ResponseEntity<RolDTO> create(@Valid @RequestBody RolDTO rolDTO){
		return new ResponseEntity<RolDTO>(rolService.create(rolDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	@ApiOperation(value = "Encuentra los roles", notes = "Retorna todos los roles existentes")
	public List<RolDTO> list(){
		return rolService.list();
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Encunetra un rol", notes = "Retorna un rol por su id")
	public ResponseEntity<RolDTO> get(@PathVariable(name = "id") Long id){
		return ResponseEntity.ok(rolService.getRol(id));
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Elimina un rol", notes = "Elimina el rol kque concuerde con el id especificado")
	public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) {
		rolService.delete(id);
		return new ResponseEntity<>("Rol eliminado con exito", HttpStatus.OK);
	}
}

