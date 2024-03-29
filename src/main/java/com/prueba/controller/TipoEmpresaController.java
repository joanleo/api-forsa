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
import com.prueba.dto.TipoEmpresaDTO;
import com.prueba.entity.TipoEmpresa;
import com.prueba.security.dto.ResDTO;
import com.prueba.service.TipoEmpresaService;
import com.prueba.util.CsvExportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;




@RestController
@RequestMapping("/tipoempresas")
@Tag(name = "Tipos empresa", description = "Opereciones referentes a los tipos de empresa")
public class TipoEmpresaController {
	
	@Autowired
	private TipoEmpresaService tipoEmpresaService;
		
	@Autowired
	private CsvExportService csvService;
	
	@PostMapping
	@Operation(summary = "Crea un tipo de empresa", description = "Crea un nuevo tipo de empresa")
	public ResponseEntity<TipoEmpresaDTO> create(@Valid @RequestBody TipoEmpresaDTO tipoEmpresaDTO){
		return new ResponseEntity<TipoEmpresaDTO>(tipoEmpresaService.create(tipoEmpresaDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	@Operation(summary = "Obtiene los tipos de empresa")
	public List<TipoEmpresaDTO> list(@RequestParam(required=false)String letras){
		if(letras == null) {
			return tipoEmpresaService.list();
		}
			return tipoEmpresaService.list(letras);
	}
	
	@PostMapping("/indexados")
	@Operation(summary = "Encuentra los tipos de empresas", description = "Retorna paginacion de los tipos de empresas que coincidan con el filtro enviado en tipoEmpresaDTO")
	public ApiResponse<Page<TipoEmpresa>> paginationList(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestBody(required=false) TipoEmpresaDTO tipoEmpresaDTO){
		
		
		if(Objects.isNull(tipoEmpresaDTO)) {
			Page<TipoEmpresa> tiposEmpresa = tipoEmpresaService.searchTiposEmpresa(pagina, items);
			return new ApiResponse<>(tiposEmpresa.getSize(), tiposEmpresa);
		}else {
			Page<TipoEmpresa> tiposEmpresa = tipoEmpresaService.searchTiposEmpresa(tipoEmpresaDTO, pagina, items);
			return new ApiResponse<>(tiposEmpresa.getSize(), tiposEmpresa);
		}
		
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Encuentra un tipo de empresa", description = "Retorna un tipo de empresa segun su id")
	public ResponseEntity<TipoEmpresaDTO> getEmpresa(@PathVariable(name = "id") Long id){
		return ResponseEntity.ok(tipoEmpresaService.getTipoEmpresa(id));
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "Actualiza un tipo de empresa", description = "Actualiza los datos de un tipo de empresa")
	public ResponseEntity<TipoEmpresaDTO> update(
			@Valid @RequestBody TipoEmpresaDTO tipoEmpresaDTO,
			@PathVariable Long id){
		TipoEmpresaDTO actualizado = tipoEmpresaService.update(id, tipoEmpresaDTO);
		
		return new ResponseEntity<TipoEmpresaDTO>(actualizado, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "Elimina un tipo de empresa", description = "Elimina un tipo de empresa por su id")
	public ResponseEntity<ResDTO> delete(@PathVariable(name="id")Long id){
		tipoEmpresaService.delete(id);
		return new ResponseEntity<ResDTO>(new ResDTO("Tipo de empresa eliminado con exito"), HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
	@Operation(summary = "Inhabilita un tipo de empresa", description = "Inhabilita un tipo de empresa por su id")
	public ResponseEntity<ResDTO> unable(@PathVariable(name="id")Long id){
		tipoEmpresaService.unable(id);
		
		return new ResponseEntity<>(new ResDTO("Tipo de empresa inhabilidato con exito"), HttpStatus.OK);
	}
	
	@PostMapping("/descarga")
	@Operation(summary = "Descarga listado en formato csv", description = "Descarga listado de activos de la busqueda realizada en formato csv")
	public void getCsvTiposEmpresa(HttpServletResponse servletResponse,
								@RequestParam(required=false, defaultValue = "0") Integer pagina, 
								@RequestParam(required=false, defaultValue = "0") Integer items,
								@RequestBody(required=false) TipoEmpresaDTO tipoEmpresaDTO) throws IOException{
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		
		servletResponse.setContentType("application/x-download");
        servletResponse.addHeader("Content-Disposition", "attachment;filename=\"" + "Tipo_empresa_"+currentDateTime+".csv" + "\"");
        
        
        if(Objects.isNull(tipoEmpresaDTO)) {
        	List<TipoEmpresaDTO> tiposEmpresa = tipoEmpresaService.list();
        	csvService.writeTiposEmpresaToCsv(servletResponse.getWriter(), tiposEmpresa);
		}else {
			List<TipoEmpresaDTO> tiposEmpresa = tipoEmpresaService.list(tipoEmpresaDTO);
			csvService.writeTiposEmpresaToCsv(servletResponse.getWriter(), tiposEmpresa);
		}
		
		
        
	}

}
