package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.dto.TipoEmpresaDTO;
import com.prueba.entity.TipoEmpresa;

@Component
public class TipoEmpresaSpecifications {

	public Specification<TipoEmpresa> getTipoEmpresa(TipoEmpresaDTO tipoEmpresaDTO){
		return (root, query, criteryBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();

			if(tipoEmpresaDTO.getId() != null) {
			predicates.add(criteryBuilder.like(root.get("id").as(String.class), "%"+tipoEmpresaDTO.getId()+ "%"));
			}
			if(tipoEmpresaDTO.getTipo() != null) {
			predicates.add(criteryBuilder.equal(root.get("tipo"), tipoEmpresaDTO.getTipo()));
			}
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
