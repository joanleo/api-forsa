package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.dto.EstadoDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Estado;

@Component
public class EstadoSpecifications {

	public Specification<Estado> getEstado(EstadoDTO estadoDTO, Empresa empresa) {
		return (root, query, criteryBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();
			
			if(estadoDTO.getId() != null) {
				predicates.add(criteryBuilder.like(root.get("id").as(String.class), "%"+estadoDTO.getId()+ "%"));
				}
				if(estadoDTO.getTipo() != null) {
				predicates.add(criteryBuilder.equal(root.get("tipo"), "%"+estadoDTO.getTipo()+ "%"));
				}
				
				predicates.add(criteryBuilder.equal(root.get("empresa"), empresa));
				
				predicates.add(criteryBuilder.isTrue(root.get("estaActivo").as(Boolean.class)));
				
				return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}

}
