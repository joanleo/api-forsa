/**
 * 
 */
package com.prueba.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.dto.ApiResponse;
import com.prueba.entity.Empresa;
import com.prueba.entity.Salida;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;
import com.prueba.service.SalidaService;
import com.prueba.util.UtilitiesApi;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author Joan Leon
 *
 */
@RestController
@RequestMapping("/salidas")
@Tag(name = "Salidas", description = "Operaciones referentes a los documentos de salida")
public class SalidaController {
	
	@Autowired
	private SalidaService salidaService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private UtilitiesApi util;
	
	@PostMapping
	public ResponseEntity<?> crearSalida(@RequestBody Salida salida){
		return new ResponseEntity<Salida>(salidaService.crearSalida(salida), HttpStatus.CREATED);
	}
	
	@GetMapping
	public ApiResponse<Page<Salida>> obtenerSalidas(
			@RequestParam(required=false, defaultValue = "0") Integer pagina, 
			@RequestParam(required=false, defaultValue = "0") Integer items,
			@RequestBody(required=false) Salida salida,
			@RequestParam(required=false) Long nit){
		
		Empresa empresa;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioRepo.findByNombreUsuarioOrEmail(authentication.getName(), authentication.getName()).get();
		
		if(nit != null) {
			empresa = util.obtenerEmpresa(nit);
		}else {
			empresa = usuario.getEmpresa();			
		}
		
		if(Objects.isNull(salida)) {
			Page<Salida> salidas = salidaService.buscarSalidas(empresa, pagina, items);
			return new ApiResponse<>(salidas.getSize(), salidas);
		}else {
			Page<Salida> salidas = salidaService.buscarSalidas(salida, empresa, pagina, items);
		}
		return null;
	}

}
