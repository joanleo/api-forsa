package com.prueba.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.prueba.dto.EmpresaDTO;
import com.prueba.security.dto.ResDTO;
import com.prueba.service.EmpresaService;
import com.prueba.util.CsvExportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/empresas")
@Api(tags = "Empresas",description = "Operaciones referentes a las empresas")
public class EmpresaController {
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private CsvExportService csvService;
	
	@PostMapping
	@ApiOperation(value = "Crea una empresa", notes = "Crea una nueva empresa")
	public ResponseEntity<EmpresaDTO> create(@Valid @RequestBody EmpresaDTO empresaDTO){
		return new ResponseEntity<EmpresaDTO>(empresaService.create(empresaDTO), HttpStatus.CREATED);
	}
	
	@PutMapping("/{nit}")
	@ApiOperation(value = "Actualiza una empresa", notes = "Actualiza los datos de una empresa")
	public ResponseEntity<EmpresaDTO> update(@Valid @RequestBody EmpresaDTO empresaDTO,
											 @PathVariable Long nit){
		EmpresaDTO actualizada = empresaService.update(nit, empresaDTO);
		
		return new ResponseEntity<>(actualizada, HttpStatus.OK);
	}
	
	@GetMapping
	@ApiOperation(value = "Encuentra las empresas activas", notes = "Retorna las empresas activas que en su nombre contengan las letrtas indicadas, retorna todas las empresas si no se indica ninguna letra")
	public List<EmpresaDTO> list(@RequestParam(required=false) String letras){
		if(letras != null) {
			return empresaService.findByNameAndEstaActiva(letras, true);
		}else {
			return empresaService.list();			
		}
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Encuentra una empresa", notes = "Retorna una empresa por el id")
	public ResponseEntity<EmpresaDTO> get(@PathVariable(name = "id") Long id){
		return ResponseEntity.ok(empresaService.getEmpresa(id));
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Elimina una empresa", notes = "Elimina un empresa por su id")
	public ResponseEntity<ResDTO> delete(@PathVariable(name="id")Long id){
		empresaService.delete(id);
		return new ResponseEntity<ResDTO>(new ResDTO("Empresa eliminada con exito"), HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
	@ApiOperation(value = "Inhabilita una empresa", notes = "Inhabilita un empresa por su id")
	public ResponseEntity<ResDTO> unable(@PathVariable(name="id")Long id){
		empresaService.unable(id);
		return new ResponseEntity<ResDTO>(new ResDTO("Empresa inhabilitada con exito"), HttpStatus.OK);
	}
	
	@PostMapping("/descarga")
	@ApiOperation(value = "Descarga listado en formato csv", notes = "Descarga listado de empresas de la busqueda realizada en formato csv")
	public void getCsvEmpresas(HttpServletResponse servletResponse,
								@RequestParam(required=false, defaultValue = "0") Integer pagina, 
								@RequestParam(required=false, defaultValue = "0") Integer items,
								@RequestParam(required=false) String letras) throws IOException {
		servletResponse.setContentType("application/x-download");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
        servletResponse.addHeader("Content-Disposition", "attachment;filename=\"" + "empresas"+ "_" + currentDateTime + ".csv" + "\"");
        
        
		
        if(letras != null){
			List<EmpresaDTO> empresas =  empresaService.findByNameAndEstaActiva(letras, true);
			csvService.writeEmpresasToCsv(servletResponse.getWriter(), empresas);
		}else{
			System.out.println("Controller busqueda vacia");
			List<EmpresaDTO> empresas = empresaService.list();
			csvService.writeEmpresasToCsv(servletResponse.getWriter(), empresas);
		}
		
		
        
	}

}

