package com.prueba.controller;

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
import com.prueba.dto.UbicacionDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Ubicacion;
import com.prueba.security.dto.ResDTO;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.service.UbicacionService;
import com.prueba.util.CsvExportService;
import com.prueba.util.UtilitiesApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;




@RestController
@RequestMapping("/ubicaciones")
@Tag(name = "Ubicaciones", description = "Operaciones referentes a las ubicaciones")
public class UbicacionController {

	@Autowired
	private UbicacionService ubicacionService;
	
	@Autowired
	private CsvExportService csvService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	@PostMapping
	@Operation(summary = "Crea una ubicacion", description = "Crea una nueva ubicacion")
	public ResponseEntity<UbicacionDTO> create(@Valid @RequestBody UbicacionDTO ubicacionDTO){
		return new ResponseEntity<UbicacionDTO>(ubicacionService.create(ubicacionDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	@Operation(summary="Obtiene las ubicaciones", description = "Retorna una lista de las ubicaciones que coincidan con las letras indicadas, retorna todas las ubicaciones si no se indica ninguna letra")
	public List<UbicacionDTO> get(
			@RequestParam(required=false)String letras,
			@RequestParam(required=false) Long nit){

		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();
		}
		if(letras == null ) {
			return ubicacionService.list(empresa);
		}
		return  ubicacionService.findByName(letras, empresa);
	}
	
	@PostMapping("/indexados")
	@Operation(summary = "Encuentra las ubicaciones", description = "Retorna paginacion de las ubicaciones que coincidan con el filtro enviado en ubicacionDTO")
	public ApiResponse<Page<Ubicacion>> paginationList(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestBody(required=false) UbicacionDTO ubicacionDTO,
			@RequestParam(required=false) Long nit){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nit != null) {
		empresa = util.obtenerEmpresa(nit);
		}else {
		empresa = usuario.getEmpresa();			
		}
		
		if(Objects.isNull(ubicacionDTO)) {
			Page<Ubicacion> ubicaciones = ubicacionService.searchUbicaciones(empresa, pagina, items);
			return new ApiResponse<>(ubicaciones.getSize(), ubicaciones);
		}else {
			ubicacionDTO.setEmpresa(empresa);
			Page<Ubicacion> ubicaciones = ubicacionService.searchUbicaciones(ubicacionDTO, empresa, pagina, items);
			return new ApiResponse<>(ubicaciones.getSize(), ubicaciones);
		}
		
	}
	
	@GetMapping("/{id},{nitEmpresa}")
	@Operation(summary = "Encuentra una ubicacion", description = "Retorna una ubicacion por su id")
	public ResponseEntity<UbicacionDTO> getUbicacion(@PathVariable(name = "id") Long id,
					 								 @PathVariable(required=false) Long nitEmpresa){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nitEmpresa != null) {
		empresa = util.obtenerEmpresa(nitEmpresa);
		}else {
		empresa = usuario.getEmpresa();			
		}
		
		return ResponseEntity.ok(ubicacionService.getUbicacion(id, empresa));
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "Actualiza una ubicacion", description = "Actualiza los datos de una ubicacion")
	public ResponseEntity<UbicacionDTO> update(@Valid @RequestBody UbicacionDTO ubicacionDTO,
											@PathVariable Long id){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(ubicacionDTO.getEmpresa() != null) {
		empresa = util.obtenerEmpresa(ubicacionDTO.getEmpresa().getNit());
		}else {
		empresa = usuario.getEmpresa();			
		}
		ubicacionDTO.setEmpresa(empresa);
		UbicacionDTO actualizado = ubicacionService.update(id, ubicacionDTO);
		
		return new ResponseEntity<>(actualizado, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id},{nitEmpresa}")
	@Operation(summary = "Elimina una ubicacion", description = "Elimina una ubicacion por su id")
 	public ResponseEntity<ResDTO> delete(@PathVariable(name="id")Long id,
										 @PathVariable(required=false) Long nitEmpresa){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nitEmpresa != null) {
		empresa = util.obtenerEmpresa(nitEmpresa);
		}else {
		empresa = usuario.getEmpresa();			
		}
		ubicacionService.delete(id, empresa);
		return new ResponseEntity<ResDTO>(new ResDTO("Ubicacion eliminada con exito"), HttpStatus.OK);
	}
	
	@PatchMapping("/{id},{nitEmpresa}")
	@Operation(summary = "Inhabilita una ubicacion", description = "Inhabilita una ubicacion por su id")
	public ResponseEntity<ResDTO> unable(@PathVariable(name="id")Long id,
	 									@PathVariable(required=false) Long nitEmpresa){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nitEmpresa != null) {
		empresa = util.obtenerEmpresa(nitEmpresa);
		}else {
		empresa = usuario.getEmpresa();			
		}
		
		ubicacionService.unable(id, empresa);
		
		return new ResponseEntity<ResDTO>(new ResDTO("Ubicacion inhabilitada con exito"), HttpStatus.OK);
	}
	
	@PostMapping("/descarga")
	@Operation(summary = "Descarga listado en formato csv", description = "Descarga listado de activos de la busqueda realizada en formato csv")
	public void getCsvUbicaciones(HttpServletResponse servletResponse,
				@RequestParam(required=false, defaultValue = "0") Integer pagina, 
				@RequestParam(required=false, defaultValue = "0") Integer items,
				@RequestBody(required=false) UbicacionDTO ubicacionDTO,
				@RequestParam(required=false) Long nit) throws IOException {
		servletResponse.setContentType("application/x-download");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
        servletResponse.addHeader("Content-Disposition", "attachment;filename=\"" + "estados"+ "_" + currentDateTime + ".csv" + "\"");
        
        Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
      		
        if(Objects.isNull(ubicacionDTO)){
        	List<UbicacionDTO> ubicaciones = ubicacionService.list(empresa);
        	csvService.writeUbicacionesToCsv(servletResponse.getWriter(), ubicaciones);
		}else{
			ubicacionDTO.setEmpresa(empresa);
			List<UbicacionDTO> ubicaciones =  ubicacionService.listUbicaciones(ubicacionDTO, empresa); 
			csvService.writeUbicacionesToCsv(servletResponse.getWriter(), ubicaciones);
		}
		        
	}
	
}
