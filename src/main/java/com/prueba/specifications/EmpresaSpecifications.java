package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.dto.EmpresaDTO;
import com.prueba.entity.Empresa;

@Component
public class EmpresaSpecifications {

	public Specification<Empresa> getEmpresa(EmpresaDTO empresaDTO){
		return (root, query, criteryBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();
			
			if(empresaDTO.getNit() != null) {
			predicates.add(criteryBuilder.like(root.get("nit").as(String.class), "%"+empresaDTO.getNit()+ "%"));
			}
			if(empresaDTO.getNombre() != null) {
			predicates.add(criteryBuilder.equal(root.get("nombre"), empresaDTO.getNombre()));
			}
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}

