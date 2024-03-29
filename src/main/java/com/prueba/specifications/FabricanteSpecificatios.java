package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.dto.FabricanteDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Fabricante;



@Component
public class FabricanteSpecificatios {

	public Specification<Fabricante> getFabricante(FabricanteDTO fabricanteDTO, Empresa empresa){
		return (root, query, criteryBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();
			
			if(fabricanteDTO.getNit() != null) {
			predicates.add(criteryBuilder.like(root.get("nit").as(String.class), "%"+fabricanteDTO.getNit()+ "%"));
			}
			if(fabricanteDTO.getNombre() != null) {
			predicates.add(criteryBuilder.like(root.get("nombre"), "%" + fabricanteDTO.getNombre() + "%"));
			}
			if(fabricanteDTO.getEstaActivo() != null && fabricanteDTO.getEstaActivo() ) {
				predicates.add(criteryBuilder.isTrue(root.get("estaActivo").as(Boolean.class)));
			}
			if(fabricanteDTO.getEstaActivo() != null && !fabricanteDTO.getEstaActivo() ) {
				predicates.add(criteryBuilder.isFalse(root.get("estaActivo").as(Boolean.class)));
			}
			
			predicates.add(criteryBuilder.equal(root.get("empresa"), empresa));
			
			
			
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
