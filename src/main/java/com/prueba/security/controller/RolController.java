package com.prueba.security.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.dto.ApiResponse;
import com.prueba.entity.Empresa;
import com.prueba.security.dto.RolDTO;
import com.prueba.security.entity.Rol;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.security.service.RolService;
import com.prueba.util.UtilitiesApi;

//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/roles")
//@Api(tags = "Roles",description = "Operaciones referentes a los roles de los usuarios")
public class RolController {

	@Autowired
	private RolService rolService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	@PostMapping
	//@ApiOperation(value = "Crear un rol", notes = "Crea un nuevo rol")
	public ResponseEntity<RolDTO> create(@Valid @RequestBody RolDTO rolDTO){
		return new ResponseEntity<RolDTO>(rolService.create(rolDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	//@ApiOperation(value = "Encuentra los roles", notes = "Retorna todos los roles existentes")
	public List<RolDTO> list(@RequestParam(required=false)String letras,
							 @RequestParam(required=false) Long nit){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();
		}
		if(letras == null) {
			return rolService.list(empresa);
		}
		System.out.println("controller, empresa: "+empresa.getNombre());
		return rolService.list(letras, empresa);
	}
	
	@PostMapping("/indexados")
	public ApiResponse<Page<Rol>> paginationList(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestBody(required=false) RolDTO rolDTO,
			@RequestParam(required=false) Long nit){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		if(Objects.isNull(rolDTO)) {
			Page<Rol> roles = rolService.searchRoles(empresa, pagina, items);
			return new ApiResponse<>(roles.getSize(), roles);
		}else {
			Page<Rol> roles = rolService.serachRoles(rolDTO, empresa, pagina, items);
			return new ApiResponse<>(roles.getSize(), roles);
		}
				
	}
	
	
	@GetMapping("/{id},{nitEmpresa}")
	//@ApiOperation(value = "Encuentra un rol", notes = "Retorna un rol por su id")
	public ResponseEntity<RolDTO> get(@PathVariable(name = "id") Long id,
									@PathVariable(required=false) Long nitEmpresa){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nitEmpresa != null) {
		empresa = util.obtenerEmpresa(nitEmpresa);
		}else {
		empresa = usuario.getEmpresa();			
		}
		
		return ResponseEntity.ok(rolService.getRol(id, empresa));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<RolDTO> update(@RequestBody RolDTO rol,
			@PathVariable long id){
		
		return new ResponseEntity<RolDTO>(rolService.update(id, rol), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	//@ApiOperation(value = "Elimina un rol", notes = "Elimina el rol kque concuerde con el id especificado")
	public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) {
		rolService.delete(id);
		return new ResponseEntity<>("Rol eliminado con exito", HttpStatus.OK);
	}
}

