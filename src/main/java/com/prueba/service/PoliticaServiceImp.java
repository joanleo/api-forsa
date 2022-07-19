/**
 * 
 */
package com.prueba.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.prueba.entity.Empresa;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.security.entity.Politica;
import com.prueba.security.entity.Rol;
import com.prueba.security.repository.PoliticaRepository;
import com.prueba.security.repository.RolRepository;
import com.prueba.specifications.PoliticaSpecifications;

/**
 * @author Joan Leon
 *
 */
@Service
public class PoliticaServiceImp implements PoliticaService {
	
	@Autowired
	private PoliticaRepository politicaRepo;
	
	@Autowired
	private RolRepository rolRepo;
	
	@Autowired
	private PoliticaSpecifications politicaSpec;

	@Override
	public Page<Politica> buscarPoliticas(Rol rol, Empresa empresa, Integer pagina, Integer items) {
		
		Rol Exist = rolRepo.findByIdRolAndEmpresaAndEstaActivoTrue(rol.getIdRol(), empresa);
		if(Objects.isNull(Exist)) {
			throw new ResourceNotFoundException("Rol", "Id", rol.getIdRol());
		}
		
		if(items == 0) {
			Page<Politica> politicas = politicaRepo.findByRol(Exist, PageRequest.of(0, 10));
			return politicas;
		}
		Page<Politica> politicas = politicaRepo.findByRol(Exist, PageRequest.of(pagina, items));		
		return politicas;
	}

	@Override
	public Page<Politica> buscarPoliticas(Long nit, Integer pagina, Integer items) {
		
		if(items == 0) {
			Page<Politica> politicas = politicaRepo.findAll(politicaSpec.getPoliticas(nit), PageRequest.of(0, 10));
			return politicas;
		}
		Page<Politica> politicas = politicaRepo.findAll(politicaSpec.getPoliticas(nit), PageRequest.of(0, 10));
		return politicas;
	}



}
