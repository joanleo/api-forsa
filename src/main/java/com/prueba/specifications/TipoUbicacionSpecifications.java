package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.dto.TipoUbicacionDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.TipoUbicacion;

@Component
public class TipoUbicacionSpecifications {

	public Specification<TipoUbicacion> getTipoUbibcacion(TipoUbicacionDTO tipoUbicacionDTO, Empresa empresa){
		return (root, query, criteryBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();
			
			if(tipoUbicacionDTO.getId() != null) {
			predicates.add(criteryBuilder.like(root.get("nit").as(String.class), "%"+tipoUbicacionDTO.getId()+ "%"));
			}
			if(tipoUbicacionDTO.getNombre() != null) {
			predicates.add(criteryBuilder.equal(root.get("nombre"), tipoUbicacionDTO.getNombre()));
			}
			
			predicates.add(criteryBuilder.equal(root.get("empresa"), empresa));
			
			predicates.add(criteryBuilder.isTrue(root.get("estaActivo").as(Boolean.class)));
			
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
