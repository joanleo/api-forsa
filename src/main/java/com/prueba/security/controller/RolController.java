package com.prueba.security.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
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
import com.prueba.security.dto.RutinaDTO;
import com.prueba.security.entity.Politica;
import com.prueba.security.entity.Rol;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.security.service.RolService;
import com.prueba.service.PoliticaService;
import com.prueba.util.CsvExportService;
import com.prueba.util.UtilitiesApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;




@RestController
@RequestMapping("/roles")
@Tag(name = "Roles", description = "Operaciones referentes a los roles de los usuarios")
public class RolController {

	@Autowired
	private RolService rolService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	@Autowired
	private PoliticaService politicaService;
	
	@Autowired
	private CsvExportService csvService;
	
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
		return rolService.list(letras, empresa);
	}
	
	@PostMapping("/indexados")
	@Operation(summary = "Encuentra los roles", description = "Retorna los roles que coincidan con el filtro del datos "
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
		System.out.println(empresa.getNombre());
		if(Objects.isNull(rolDTO)) {
			Page<Rol> roles = rolService.searchRoles(empresa, pagina, items);
			return new ApiResponse<>(roles.getSize(), roles);
		}else {
			rolDTO.setEmpresa(empresa);
			Page<Rol> roles = rolService.serachRoles(rolDTO, empresa, pagina, items);
			return new ApiResponse<>(roles.getSize(), roles);
		}
				
	}
	
	
	@GetMapping("/{id},{nitEmpresa}")
	@Operation(summary = "Encuentra un rol", description = "Retorna un rol por su id")
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
	@Operation(summary = "Actualiza un rol", description = "actualiza el nombre de un rol")
	public ResponseEntity<RolDTO> update(@RequestBody RolDTO rol,
			@PathVariable long id){
		
		return new ResponseEntity<RolDTO>(rolService.update(id, rol), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "Elimina un rol", description = "Elimina el rol kque concuerde con el id especificado")
	public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) {
		rolService.delete(id);
		return new ResponseEntity<>("Rol eliminado con exito", HttpStatus.OK);
	}
	
	@GetMapping("/politicas")
	@Operation(summary = "Obtiene las politicas de un rol", description = "Retorna una lista con las politicas de un rol")
	public List<Politica> listaPoliticas(@RequestParam(required=false)Long id){
		List<Politica> politicas = rolService.listarPoliticas(id);
		return politicas;
	}
	
	@PostMapping("/politicas/indexados")
	@Operation(summary = "Obtiene las politicas de un rol", description = "Retorna paginacion de las politicas que coincidan con el filtro enviado en rol")
	public Set<RutinaDTO> paginacionPoliticas(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestBody(required=true) Rol rol,
			@RequestParam(required=false) Long nit){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}

		Set<RutinaDTO> politicas = politicaService.buscarPoliticas(rol, empresa);
				
		return politicas;

	}
	@PostMapping("/politicas/indexados/descarga")
	@Operation(summary = "Descarga listado en formato csv", description = "Descarga listado de politicas de un rol de la busqueda realizada en formato csv")
	public void getCsvPoliticasRol(HttpServletResponse servletResponse,
			@RequestBody(required=true) Rol rol,
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
		System.out.println(rol.getIdRol());
		Set<RutinaDTO> politicas = politicaService.buscarPoliticas(rol, empresa);
		csvService.writePolitica(servletResponse.getWriter(), politicas);
	}
		
	@PutMapping("/politicas/{idPolitica}")
	@Operation(summary = "Actualiza el permiso de una politica", description = "Actualiza el permiso de una politica segun su id")
	public ResponseEntity<?> actualizarPolitica(@PathVariable Long idPolitica,
			@RequestBody Politica politica){
		return new ResponseEntity<>(rolService.actualizarPoliticar(idPolitica, politica), HttpStatus.OK);
	}
}

