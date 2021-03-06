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
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.prueba.security.dto.RegistroDTO;
import com.prueba.security.dto.ResDTO;
import com.prueba.security.entity.Rol;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.RolRepository;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.security.service.UsuarioService;
import com.prueba.util.UtilitiesApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;




@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Operaciones referentes a los usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private RolRepository rolRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired	private UtilitiesApi util;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping
	@Operation(summary = "Obtiene los usuarios", description = "Retorna una lista de usuarios que contengan en su nombre "
			+ "las letras enviadas como parametro. Por defecto estos usuarios pertenecen a la empresa de quien esta logueado, "
			+ "si se desea obtener otra empresa en especifico se debe enviar como parametro el nit")
	public List<Usuario> get(@RequestParam(required=false)String letras,
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
			return usuarioService.list(empresa);
		}
		return  usuarioService.findByNombreAndEmpresaAndEstaActivo(letras, empresa);
	}
	
	
	@PostMapping("/indexados")
	@Operation(summary = "Encuentra los usuarios", description = "Retorna los usuarios que coincidan con el filtro del datos "
			+ "recibidos en formato JSON, segun el esquema RegistoDTO")
	public ApiResponse<Page<Usuario>> paginationList(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestBody(required=false)RegistroDTO registroDTO,
			@RequestParam(required=false) Long nit){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}

		if(Objects.isNull(registroDTO)) {
			Page<Usuario> usuarios = usuarioService.searchFabricantes(empresa, pagina, items);
			return new ApiResponse<>(usuarios.getSize(), usuarios);
		}else {
			Page<Usuario> usuarios = usuarioService.searchFabricantes(registroDTO, empresa, pagina, items);
			return new ApiResponse<>(usuarios.getSize(), usuarios);
		}
	}
	
	@PostMapping
	@Operation(summary = "Crea un nuevo usuario", description = "Crea un nuevo usuario")
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario creado exitosamente")
	public ResponseEntity<?> create(@Valid@RequestBody RegistroDTO registroDTO){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication.getName() == "anonymousUser") {
			throw new IllegalAccessError("Debe estar logueado para realizar el registro");
		}
		Usuario usuarioActual = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		System.out.println(usuarioActual);
		if(usuarioActual == null) {
			return new ResponseEntity<>("Debe estar logueado para realizar el registro",HttpStatus.BAD_REQUEST);
		}
		
		if(registroDTO.getEmpresa() != null) {
			empresa = registroDTO.getEmpresa();
		}else {
			empresa = usuarioActual.getEmpresa();			
		}
		System.out.println("verificando nombre de usuario");
		if(usuarioRepo.existsByNombreUsuario(registroDTO.getNombreUsuario())) {
			return new ResponseEntity<>("Ese nombre de usuario ya existe",HttpStatus.BAD_REQUEST);
		}
		System.out.println("verificando correo de usuario");
		if(usuarioRepo.existsByEmail(registroDTO.getEmail())) {
			return new ResponseEntity<>("Ese email de usuario ya existe",HttpStatus.BAD_REQUEST);
		}
		System.out.println("usurio no existe");
		Usuario usuario = new Usuario();
		usuario.setEmpresa(empresa);
		usuario.setNombre(registroDTO.getNombre());
		usuario.setNombreUsuario(registroDTO.getNombreUsuario());
		usuario.setEmail(registroDTO.getEmail());
		usuario.setContrasena(passwordEncoder.encode(registroDTO.getContrasena()));
		if(registroDTO.getRol() == null) {
			System.out.println("no tiene rol");
			Rol rol = rolRepo.findByNombre("ROLE_USER");	
			System.out.println("se asigna rol: "+rol.getNombre());
			usuario.setRol(rol);
		}else {
			usuario.setRol(registroDTO.getRol());
		}
				
		usuarioRepo.save(usuario);
		return new ResponseEntity<>("Usuario creado exitosamente",HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	@Operation(
			summary = "Actualiza un usuario", 
			description = "Actualiza los datos de un usuario")
	public ResponseEntity<?> update(@Valid@RequestBody(required = true) RegistroDTO registroDTO,
			@PathVariable Long id){

		try {
			Usuario actualizado = usuarioService.update(id, registroDTO);
			return new ResponseEntity<Usuario>(actualizado, HttpStatus.OK);
		} catch (Exception error) {

			return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@DeleteMapping("/{id},{nitEmpresa}")
	@Operation(summary = "Elimina un usuario", description = "Elimina un usuario por su id")
	public ResponseEntity<?> delete(@PathVariable(name="nitFabricante")Long nitFabricante,
			 @PathVariable(required=false) Long nitEmpresa){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nitEmpresa != null) {
		empresa = util.obtenerEmpresa(nitEmpresa);
		}else {
		empresa = usuario.getEmpresa();			
		}
		try {
			usuarioService.delete(nitFabricante, empresa);
			return new ResponseEntity<ResDTO>(new ResDTO("Fabricante eliminado con exito"), HttpStatus.OK);
		} catch (Exception error) {
			return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
		} 
		
}

}
