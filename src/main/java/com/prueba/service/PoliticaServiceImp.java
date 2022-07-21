/**
 * 
 */
package com.prueba.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.prueba.entity.Empresa;
import com.prueba.entity.Rutina;
import com.prueba.exception.ResourceNotFoundException;
import com.prueba.repository.RutinaRepository;
import com.prueba.security.dto.PoliticaDTO;
import com.prueba.security.dto.RutinaDTO;
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
	public Set<RutinaDTO> buscarPoliticas(Rol rol, Empresa empresa) {
		System.out.println(rol.getIdRol() +" "+ empresa.getNombre());
		Rol esxiste = rolRepo.findByIdRol(rol.getIdRol());
		System.out.println("Buscando Rol por id: "+esxiste);
		/*Rol exist = rolRepo.findByIdRolAndEmpresaAndEstaActivoTrue(rol.getIdRol(), empresa);
		if(Objects.isNull(exist)) {
			throw new ResourceNotFoundException("Rol", "Id", rol.getIdRol());
		}*/
		List<Politica> politicas = politicaRepo.findByRol(esxiste);
		
		String aux = "";
		Set<RutinaDTO> rutinaRol = new HashSet<>();
		for(Politica politica: politicas) {
			
			String auxInterna = politica.getDetalle().getRutina().getNombre();
			if(aux != auxInterna) {
				RutinaDTO nuevaRutina = new RutinaDTO();
				nuevaRutina.setNombre(politica.getDetalle().getRutina().getNombre());
				Set<PoliticaDTO> listPoliDTO = new HashSet<>();
				for(Politica politicaRe: politicas) {
					if(nuevaRutina.getNombre() == politicaRe.getDetalle().getRutina().getNombre()) {
						PoliticaDTO politicaDTO= new PoliticaDTO();
						String nombre = politicaRe.getDetalle().getRuta().getNombre();
						String url = politicaRe.getDetalle().getRuta().getUrl();
						Boolean permitido = politicaRe.getPermiso();
						politicaDTO.setNombre(nombre);
						politicaDTO.setUrl(url);
						politicaDTO.setRol(esxiste.getNombre());
						politicaDTO.setPermiso(permitido);
						listPoliDTO.add(politicaDTO);
					}
				}	
				nuevaRutina.setPolitica(listPoliDTO);
	        	System.out.println(rutinaRol.contains(nuevaRutina));
	        	rutinaRol.add(nuevaRutina);
			}
			
        	aux = auxInterna;
			
		}
		/*if(items == 0) {
			Page<Politica> politicas = politicaRepo.findByRol(Exist, PageRequest.of(0, 10));
			return politicas;
		}
		Page<Politica> politicas = politicaRepo.findByRol(Exist, PageRequest.of(pagina, items));		
		return politicas;*/
		return rutinaRol;
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
