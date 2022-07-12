package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.entity.Empresa;
import com.prueba.security.dto.RolDTO;
import com.prueba.security.entity.Rol;

@Component
public class RolSpecifications {
	
	public Specification<Rol> getRoles(RolDTO rolDTO, Empresa empresa) {
		
		return (root, query, criteryBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();
			
			if(rolDTO.getNombre() != null) {
				predicates.add(criteryBuilder.like(root.get("nombre").as(String.class), "%"+rolDTO.getNombre()+ "%"));
			}
			predicates.add(criteryBuilder.equal(root.get("empresa"), empresa));
			
			predicates.add(criteryBuilder.isTrue(root.get("estaActivo").as(Boolean.class)));
			
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}

}
