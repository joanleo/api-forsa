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
import com.prueba.entity.Politica;
import com.prueba.entity.Rutina;
import com.prueba.security.dto.RolDTO;
import com.prueba.security.entity.Rol;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.security.service.RolService;
import com.prueba.util.UtilitiesApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;




@RestController
@RequestMapping("/roles")
//@Api(tags = "Roles",description = "Operaciones referentes a los roles de los usuarios")
@Tag(name = "Roles", description = "Operaciones referentes a los roles de los usuarios")
public class RolController {

	@Autowired
	private RolService rolService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	@PostMapping
	@Operation(summary = "Crea un rol", description = "Crea un nuevo rol" )
	public ResponseEntity<RolDTO> create(@Valid @RequestBody RolDTO rolDTO){
		return new ResponseEntity<RolDTO>(rolService.create(rolDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	@Operation(summary = "Obtiene los roles", description = "Retorna una lista de roles que contengan en su nombre "
			+ "las letras enviadas como parametro. Por defecto estos usuarios pertenecen a la empresa de quien esta logueado, "
			+ "si se desea obtener otra empresa en especifico se debe enviar como parametro el nit")
	public List<Rol> list(@RequestParam(required=false)String letras,
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
	@Operation(summary = "Encuentra los roles", description = "Retorna los usuarios que coincidan con el filtro del datos "
			+ "recibidos en formato JSON, segun el esquema RolDTO")
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
	
	@PostMapping("/politicas")
	public ResponseEntity<?> agregarPolitica(@RequestBody Rutina rutina){
		
		return null;
	}
	
	@GetMapping("/politicas")
	public List<Politica> listaPoliticas(@RequestParam(required=false)String role){
		List<Politica> politicas = rolService.listarPoliticas(role);
		return politicas;
	}
	
	@PutMapping("/politicas/{idPolitica}")
	public ResponseEntity<?> actualizarPolitica(@PathVariable Long idPolitica,
			@RequestBody Politica politica){
		return new ResponseEntity<>(rolService.actualizarPoliticar(idPolitica, politica), HttpStatus.OK);
	}
}

