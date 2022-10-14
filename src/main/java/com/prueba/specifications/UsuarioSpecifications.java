package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.entity.Empresa;
import com.prueba.security.dto.RegistroDTO;
import com.prueba.security.entity.Rol;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.RolRepository;

/**
 * 
 * @author Joan Leon
 *
 */
@Component
public class UsuarioSpecifications {
	
	@Autowired
	private RolRepository rolRepo;

	/**
	 * 
	 * @param registroDTO
	 * @param empresa
	 * @return
	 */
	public Specification<Usuario> getUsuarios(RegistroDTO registroDTO, Empresa empresa){
		return (root, query, criteryBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();
			if(registroDTO.getNombre() != null) {
				predicates.add(criteryBuilder.like(root.get("nombre"), "%" + registroDTO.getNombre() + "%"));				
			}
			if(registroDTO.getEmail() != null) {
				predicates.add(criteryBuilder.like(root.get("email"), "%" +registroDTO.getEmail() + "%"));				
			}
			if(registroDTO.getNombreUsuario() != null) {
				predicates.add(criteryBuilder.like(root.get("nombreUsuario"), "%" +registroDTO.getNombreUsuario() + "%"));				
			}
			if(registroDTO.getEstaActivo() != null && registroDTO.getEstaActivo()) {
				predicates.add(criteryBuilder.isTrue(root.get("estaActivo").as(Boolean.class)));				
			}
			if(registroDTO.getEstaActivo() != null && !registroDTO.getEstaActivo()) {
				predicates.add(criteryBuilder.isFalse(root.get("estaActivo").as(Boolean.class)));				
			}
			if(registroDTO.getRol() != null) {
				Rol rol = rolRepo.findByIdRol(registroDTO.getRol().getIdRol());
				predicates.add(criteryBuilder.equal(root.get("rol"), rol));
			} 
			
			predicates.add(criteryBuilder.equal(root.get("empresa"), empresa));
						
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
