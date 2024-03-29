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
import com.prueba.dto.FamiliaDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Familia;
import com.prueba.security.dto.ResDTO;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.service.FamiliaService;
import com.prueba.util.CsvExportService;
import com.prueba.util.UtilitiesApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;




@RestController
@RequestMapping("/familias")
@Tag(name = "Familias", description = "Operaciones referentes a las familias")
public class FamiliaController {

	@Autowired
	private FamiliaService familiaService;
	
	@Autowired
	private CsvExportService csvService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	@PostMapping
	@Operation(summary = "Crea una familia", description = "Crea una nueva familia")
	public ResponseEntity<FamiliaDTO> create(@Valid @RequestBody FamiliaDTO familiaDTO,
			 								 @RequestParam(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		familiaDTO.setEmpresa(empresa);
		return new ResponseEntity<FamiliaDTO>(familiaService.create(familiaDTO, empresa), HttpStatus.CREATED);
	}
	
	@GetMapping
	@Operation(summary="Encuentra las familias", description = "Retorna una lista de las familias que coincidan con las letras indicadas, retorna todas las familias si no se indica ninguna letra")
	public List<FamiliaDTO> get(
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
			return familiaService.list(empresa);
		}
		return  familiaService.findByNameAndEmpresaAndEstaActivo(letras, empresa);
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "Actualiza una familia", description = "Actualiza los datos de una familia")
	public ResponseEntity<FamiliaDTO> update(@Valid @RequestBody FamiliaDTO familiaDTO,
											 @PathVariable Long id,
			 								 @RequestParam(required=false) Long nit){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		familiaDTO.setEmpresa(empresa);
		
		FamiliaDTO actualizada = familiaService.update(id, familiaDTO, empresa);
		
		return new ResponseEntity<>(actualizada, HttpStatus.OK);
	}
	
	
	@PostMapping("/indexados")
	@Operation(summary = "Encuentra las familias", description = "Retorna paginacion de las empresas que coincidan con el filtro enviado en familiaDTO")
	public ApiResponse<Page<Familia>> paginationlist(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestBody(required=false) FamiliaDTO familiaDTO,
			@RequestParam(required=false) Long nit){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		if(Objects.isNull(familiaDTO)) {
			Page<Familia> familias = familiaService.searchFabricantes(empresa, pagina, items);
			return new ApiResponse<>(familias.getSize(), familias);
		}else {
			familiaDTO.setEmpresa(empresa);
			Page<Familia> familias = familiaService.searchFabricantes(familiaDTO, empresa, pagina, items);
			return new ApiResponse<>(familias.getSize(), familias);
		}
	}
	
	@GetMapping("/{id},{nitEmpresa}")
	@Operation(summary = "Encuentra una familia", description = "Retorna una familia por el id")
	public ResponseEntity<FamiliaDTO> get(@PathVariable(name = "id") Long id,
			 							  @PathVariable(required=false) Long nitEmpresa){
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		
		if(nitEmpresa != null) {
		empresa = util.obtenerEmpresa(nitEmpresa);
		}else {
		empresa = usuario.getEmpresa();			
		}
		return ResponseEntity.ok(familiaService.getFamilia(id, empresa));
	}
	
	@DeleteMapping("/{id},{nitEmpresa}")
	@Operation(summary = "Elimina una familia", description = "Elimina una familia por su id")
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
		
		familiaService.delete(id, empresa);
		return new ResponseEntity<ResDTO>(new ResDTO("familia eliminada con exito"), HttpStatus.OK);
	}
	
	@PatchMapping("/{id},{nitEmpresa}")
	@Operation(summary = "Inhabilita una familia", description = "Inhabilita una familia por su id")
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
		
		familiaService.unable(id, empresa);
		return new ResponseEntity<ResDTO>(new ResDTO("Estado inhabilitada con exito"), HttpStatus.OK);
	}
	
	@PostMapping("/descarga")
	@Operation(summary = "Descarga listado en formato csv", description = "Descarga listado de familias de la busqueda realizada en formato csv")
	public void getCsvEmpresas(HttpServletResponse servletResponse,
								@RequestParam(required=false, defaultValue = "0") Integer pagina, 
								@RequestParam(required=false, defaultValue = "0") Integer items,
								@RequestBody(required=false) FamiliaDTO familiaDTO,
								@RequestParam(required=false) Long nit) throws IOException {
		servletResponse.setContentType("application/x-download");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
        servletResponse.addHeader("Content-Disposition", "attachment;filename=\"" + "familias"+ "_" + currentDateTime + ".csv" + "\"");
        
        Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
      		
        if(Objects.isNull(familiaDTO)){
        	List<FamiliaDTO> familias = familiaService.list(empresa);
        	csvService.writeFamiliasToCsv(servletResponse.getWriter(), familias);
		}else{
			familiaDTO.setEmpresa(empresa);
			List<FamiliaDTO> familias =  familiaService.listFamilias(familiaDTO, empresa);
			csvService.writeFamiliasToCsv(servletResponse.getWriter(), familias);
		}
		        
	}
}

