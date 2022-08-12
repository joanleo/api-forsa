package com.prueba.security.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.dto.ApiResponse;
import com.prueba.entity.Empresa;
import com.prueba.exception.ResourceCannotBeAccessException;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.security.dto.RegistroDTO;
import com.prueba.security.dto.ResDTO;
import com.prueba.security.entity.Rol;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.RolRepository;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.security.service.UsuarioService;
import com.prueba.util.CsvExportService;
import com.prueba.util.UtilitiesApi;

import io.swagger.v3.oas.annotations.Operation;
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
	
	@Autowired
	private CsvExportService csvService;
	
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
			throw new ResourceCannotBeAccessException("Debe estar logueado para realizar el registro");
		}
		Usuario usuarioActual = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		if(usuarioActual == null) {
			return new ResponseEntity<>("Debe estar logueado para realizar el registro",HttpStatus.BAD_REQUEST);
		}
		
		if(registroDTO.getEmpresa() != null) {
			empresa = registroDTO.getEmpresa();
		}else {
			empresa = usuarioActual.getEmpresa();			
		}
		if(usuarioRepo.existsByNombreUsuario(registroDTO.getNombreUsuario())) {
			return new ResponseEntity<>("Ese nombre de usuario ya existe",HttpStatus.BAD_REQUEST);
		}
		if(usuarioRepo.existsByEmail(registroDTO.getEmail())) {
			return new ResponseEntity<>("Ese email de usuario ya existe",HttpStatus.BAD_REQUEST);
		}
		Usuario usuario = new Usuario();
		usuario.setEmpresa(empresa);
		usuario.setNombre(registroDTO.getNombre());
		usuario.setNombreUsuario(registroDTO.getNombreUsuario());
		usuario.setEmail(registroDTO.getEmail());
		usuario.setContrasena(passwordEncoder.encode(registroDTO.getContrasena()));
		if(registroDTO.getRol().getIdRol() == null) {
			Rol rol = rolRepo.findByNombre("ROLE_USER");
			rol.setEmpresa(empresa);
			rol = rolRepo.save(rol);
			usuario.setRol(rol);
		}else {
			Rol nuevoRol = rolRepo.findByIdRol(registroDTO.getRol().getIdRol());
			if(Objects.isNull(nuevoRol)) {
				throw new ResourceNotFoundException("Rol", "id", registroDTO.getRol().getIdRol());
			}
			usuario.setRol(nuevoRol);
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
	public ResponseEntity<?> delete(@PathVariable(name="id")Long id,
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
			usuarioService.delete(id, empresa);
			return new ResponseEntity<ResDTO>(new ResDTO("Usuario eliminado con exito"), HttpStatus.OK);
		} catch (Exception error) {
			return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
		} 
		
	}
	
	@PatchMapping(value = "/{id}")
	@Operation(summary = "Deshabilita un usuario", description = "Deshabilita un usuario por su id")
	public ResponseEntity<?> deshabilitar(@PathVariable(name="id")Long id,
			 							  @PathVariable(name = "nitEmpresa", required=false) Long nitEmpresa){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		System.out.println(nitEmpresa);
		if(nitEmpresa != null) {
		empresa = util.obtenerEmpresa(nitEmpresa);
		}else {
		empresa = usuario.getEmpresa();			
		}
		
		usuarioService.deshabilitar(id, empresa);
		
		return new ResponseEntity<ResDTO>(new ResDTO("Usuario inhabilitado con exito"), HttpStatus.OK);
	}
	
	@PostMapping("/descarga")
	@Operation(summary = "Descarga listado de usuarios en formato csv", description = "Descarga listado de usuarios de la busqueda realizada en fotmato csv")
	public void getCsvUsuarios(HttpServletResponse servletResponse,
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestBody(required=false)RegistroDTO registroDTO,
			@RequestParam(required=false) Long nit) throws IOException {
		
		servletResponse.setContentType("application/x-download");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
        servletResponse.addHeader("Content-Disposition", "attachment;filename=\"" + "fabricantes"+ "_" + currentDateTime + ".csv" + "\"");
        
        Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		if(Objects.isNull(registroDTO)) {
			List<Usuario> usuarios = usuarioService.list(empresa);
			csvService.writeUsuariosToCsv(servletResponse.getWriter(), usuarios);
		}else {
			List<Usuario> usuarios = usuarioService.listUsuarios(registroDTO, empresa);
			csvService.writeUsuariosToCsv(servletResponse.getWriter(), usuarios);
		}
	}
}
