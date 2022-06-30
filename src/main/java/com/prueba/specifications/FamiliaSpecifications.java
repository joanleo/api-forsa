package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.dto.FamiliaDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Familia;

@Component
public class FamiliaSpecifications {

	public Specification<Familia> getFamilia(FamiliaDTO familiaDTO, Empresa empresa){
		return (root, query, criteryBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();
			
			if(familiaDTO.getId() != null) {
			predicates.add(criteryBuilder.like(root.get("id").as(String.class), "%"+familiaDTO.getId()+ "%"));
			}
			if(familiaDTO.getNombre() != null) {
			predicates.add(criteryBuilder.equal(root.get("nombre"), familiaDTO.getNombre()));
			}
			
			predicates.add(criteryBuilder.equal(root.get("empresa"), empresa));
			
			predicates.add(criteryBuilder.isTrue(root.get("estaActivo").as(Boolean.class)));
			
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
