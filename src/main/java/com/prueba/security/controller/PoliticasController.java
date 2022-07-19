package com.prueba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.entity.Politica;
import com.prueba.repository.PoliticaRepository;
import com.prueba.security.entity.Rol;
import com.prueba.security.repository.RolRepository;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/politicas")
@Tag(name = "Politicas")
public class PoliticasController {

	@Autowired
	private PoliticaRepository poiliticaRepo;
	
	@Autowired
	private RolRepository rolRepo;
	
	
	@GetMapping
	public List<Politica> listsPoliticas(@RequestParam(required=false)String role){
		Rol rol = rolRepo.findByNombre(role);
		/*Politica politica = poiliticaRepo.findByRol(rol);
		
		System.out.println(rol.getNombre());
		System.out.println(rol.getIdRol());
		System.out.println(politica);*/
		return (poiliticaRepo.findByRol(rol));
	}
}
