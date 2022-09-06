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
import com.prueba.dto.EmpresaDTO;
import com.prueba.dto.TipoEmpresaDTO;
import com.prueba.entity.Empresa;
import com.prueba.security.dto.ResDTO;
import com.prueba.service.EmpresaService;
import com.prueba.service.TipoEmpresaService;
import com.prueba.util.CsvExportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/empresas")
@Tag(name = "Empresas", description = "Operaciones referentes a las empresas")
public class EmpresaController {
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private CsvExportService csvService;
	
	@Autowired
	private TipoEmpresaService tipoEmpresaService;
	
	@PostMapping
	@Operation(summary = "Crea una empresa", description = "Crea una nueva empresa")
	public ResponseEntity<EmpresaDTO> create(@Valid @RequestBody EmpresaDTO empresaDTO){
		return new ResponseEntity<EmpresaDTO>(empresaService.create(empresaDTO), HttpStatus.CREATED);
	}
	
	@GetMapping
	@Operation(summary="Encuentra las empresas", description = "Retorna una lista de las empresas que coincidan con las letras indicadas, retorna todas las empresas si no se indica ninguna letra")
	public List<TipoEmpresaDTO> get(@RequestParam(required=false)String letras){
		if(letras == null) {
			return tipoEmpresaService.list();
		}
		return  tipoEmpresaService.list(letras);
	}
	
	@PutMapping("/{nit}")
	@Operation(summary = "Actualiza una empresa", description = "Actualiza los datos de una empresa")
	public ResponseEntity<EmpresaDTO> update(@Valid @RequestBody EmpresaDTO empresaDTO,
											 @PathVariable Long nit){
		EmpresaDTO actualizada = empresaService.update(nit, empresaDTO);
		
		return new ResponseEntity<>(actualizada, HttpStatus.OK);
	}
	
	@PostMapping("/indexados")
	@Operation(summary = "Encuentra las empresas", description = "Retorna paginacion de las empresas que coincidan con el filtro enviado en EmpresaDTO")
	public ApiResponse<Page<Empresa>> paginationList(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestBody(required=false) EmpresaDTO empresaDTO){

		if(Objects.isNull(empresaDTO)) {
			Page<Empresa> empresas = empresaService.searchEmpresas(pagina, items);
			return new ApiResponse<>(empresas.getSize(), empresas);			
		}else {
			Page<Empresa> empresas = empresaService.searchEmpresas(empresaDTO, pagina, items);
			return new ApiResponse<>(empresas.getSize(), empresas);
		}
	}

	@GetMapping("/{nit}")
	@Operation(summary = "Encuentra una empresa", description = "Retorna una empresa por el id")
	public ResponseEntity<EmpresaDTO> get(@PathVariable(name = "id") Long id){
		return ResponseEntity.ok(empresaService.getEmpresa(id));
	}

	
	@DeleteMapping("/{nit}")
	@Operation(summary = "Elimina una empresa", description = "Elimina un empresa por su id")
	public ResponseEntity<ResDTO> delete(@PathVariable(name="nit")Long id){
		empresaService.delete(id);
		return new ResponseEntity<ResDTO>(new ResDTO("Empresa eliminada con exito"), HttpStatus.OK);
	}
	
	@PatchMapping("/{nit}")
	@Operation(summary = "Inhabilita una empresa", description = "Inhabilita una empresa por su id")
	public ResponseEntity<ResDTO> unable(@PathVariable(name="nit")Long id){
		empresaService.unable(id);
		
		return new ResponseEntity<>(new ResDTO("Empresa inhabilidata con exito"), HttpStatus.OK);
	}
	
	@PostMapping("/descarga")
	@Operation(summary = "Descarga listado en formato csv", description = "Descarga listado de activos de la busqueda realizada en formato csv")
	public void getCsvEmpresa(HttpServletResponse servletResponse,
								@RequestParam(required=false, defaultValue = "0") Integer pagina, 
								@RequestParam(required=false, defaultValue = "0") Integer items,
								@RequestBody(required=false) EmpresaDTO empresaDTO) throws IOException{
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		
		servletResponse.setContentType("application/x-download");
        servletResponse.addHeader("Content-Disposition", "attachment;filename=\"" + "empresas_"+currentDateTime+".csv" + "\"");
        
        
        if(Objects.isNull(empresaDTO)) {
        	List<EmpresaDTO> empresas = empresaService.list();
        	csvService.writeEmpresasToCsv(servletResponse.getWriter(), empresas);
		}else {
			List<EmpresaDTO> empresas = empresaService.list(empresaDTO);
			csvService.writeEmpresasToCsv(servletResponse.getWriter(), empresas);
		}
	}    
	
}

