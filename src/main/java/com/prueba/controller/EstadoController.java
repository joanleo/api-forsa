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
import com.prueba.dto.EstadoDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Estado;
import com.prueba.security.dto.ResDTO;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.service.EstadoService;
import com.prueba.util.CsvExportService;
import com.prueba.util.UtilitiesApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/estados")
@Tag(name = "Estado", description = "Operaciones referentes al estado del activo")
public class EstadoController {

	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	private CsvExportService csvService;
	
	@Autowired 
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	
	@PostMapping
	@Operation(summary = "Crea un estado para los activos de una empresa", description = "Crea un nuevo estado para los activos")
	public ResponseEntity<EstadoDTO> create(@Valid @RequestBody EstadoDTO estadoDTO){
		return new ResponseEntity<EstadoDTO>(estadoService.create(estadoDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	@Operation(summary = "Encuentra los estados", description = "Retorna una lista de los estados que coincidan con las letras indicadas, retorna todos los estados si no se indica ninguna letra")
	public List<EstadoDTO> get(
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
		return  estadoService.findByNameAndEmpresaAndEstaActivo(letras, empresa);
	}
	
	@PostMapping("/indexados")
	@Operation(summary = "Encuentra los estados de los activos de una empresa", description = "Retorna paginacion de los esados que coincidan con el filtro enviado en estadoDTO")
	public ApiResponse<Page<Estado>> paginationlist(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestBody(required=false) EstadoDTO estadoDTO,
			@RequestParam(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		if(Objects.isNull(estadoDTO)) {
			Page<Estado> estados = estadoService.searchEstados(empresa, pagina, items);
			return new ApiResponse<>(estados.getSize(), estados);
		}else {
			estadoDTO.setEmpresa(empresa);
			Page<Estado> estados = estadoService.searchEstados(estadoDTO, empresa, pagina, items);
			return new ApiResponse<>(estados.getSize(), estados);
		}
	}
	
	@GetMapping("/{id},{nitEmpresa}")
	@Operation(summary = "Encuentra un estado", description = "Retorna un estado segun su id")
	public ResponseEntity<EstadoDTO> get(@PathVariable(name = "id") Long id,
										 @PathVariable(required=false) Long nitEmpresa){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nitEmpresa != null) {
			empresa = util.obtenerEmpresa(nitEmpresa);
		}else {
			empresa = usuario.getEmpresa();			
		}
		return ResponseEntity.ok(estadoService.getEstado(id, empresa));
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "Actualiza el estado de una empresa", description = "Actualiza los datos de un estado")
	public ResponseEntity<EstadoDTO> update(@Valid @RequestBody EstadoDTO estadoDTO,
											@PathVariable Long id){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(estadoDTO.getEmpresa() != null) {
			empresa = util.obtenerEmpresa(estadoDTO.getEmpresa().getNit());
		}else {
			empresa = usuario.getEmpresa();			
		}
		estadoDTO.setEmpresa(empresa);
		EstadoDTO actualizado = estadoService.update(id, estadoDTO);
		
		return new ResponseEntity<>(actualizado, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id},{nitEmpresa}")
	@Operation(summary = "Elimina un estado de una empresa", description = "Elimina un estado por su id")
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
		estadoService.delete(id, empresa);
		return new ResponseEntity<ResDTO>(new ResDTO("Estado eliminada con exito"), HttpStatus.OK);
	}
	
	@PatchMapping("/{id},{nitEmpresa}")
	@Operation(summary = "Inhabilita un estado", description = "Inhabilita un estado por su id")
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
		estadoService.unable(id, empresa);
		return new ResponseEntity<ResDTO>(new ResDTO("Estado inhabilitada con exito"), HttpStatus.OK);
	}
	
	@PostMapping("/descarga")
	@Operation(summary = "Descarga listado en formato csv", description = "Descarga listado de activos de la busqueda realizada en formato csv")
	public void getCsvEmpresas(HttpServletResponse servletResponse,
								@RequestParam(required=false, defaultValue = "0") Integer pagina, 
								@RequestParam(required=false, defaultValue = "0") Integer items,
								@RequestBody(required=false)EstadoDTO estadoDTO,
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
      		
        if(Objects.isNull(estadoDTO)){
        	List<EstadoDTO> estados = estadoService.list(empresa);
        	csvService.writeEstadosToCsv(servletResponse.getWriter(), estados);
		}else{
			estadoDTO.setEmpresa(empresa);
			List<EstadoDTO> estados =  estadoService.listEstados(estadoDTO, empresa);
			csvService.writeEstadosToCsv(servletResponse.getWriter(), estados);
		}
		        
	}
}

