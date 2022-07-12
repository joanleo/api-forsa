package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.entity.Empresa;
import com.prueba.security.dto.RegistroDTO;
import com.prueba.security.entity.Usuario;

@Component
public class UsuarioSpecifications {

	public Specification<Usuario> getUsuarios(RegistroDTO registroDTO, Empresa empresa){
		return (root, query, criteryBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();
			if(registroDTO.getNombre() != null) {
				predicates.add(criteryBuilder.equal(root.get("nombre"), registroDTO.getNombre()));				
			}
			if(registroDTO.getEmail() != null) {
				predicates.add(criteryBuilder.like(root.get("nombre"), "%" +registroDTO.getEmail() + "%"));				
			}
			if(registroDTO.getUsername() != null) {
				predicates.add(criteryBuilder.like(root.get("nombre"), "%" +registroDTO.getUsername() + "%"));				
			}
			
			predicates.add(criteryBuilder.equal(root.get("empresa"), empresa));
			
			predicates.add(criteryBuilder.isTrue(root.get("estaActivo").as(Boolean.class)));
			
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
