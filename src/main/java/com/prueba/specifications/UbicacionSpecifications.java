package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.dto.UbicacionDTO;
import com.prueba.entity.Empresa;
import com.prueba.entity.Ubicacion;

@Component
public class UbicacionSpecifications {
	
	public Specification<Ubicacion> getUbicacion(UbicacionDTO ubicacionDTO, Empresa empresa){
		return (root, query, criteryBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();
			
			if(ubicacionDTO.getId() != null) {
				predicates.add(criteryBuilder.like(root.get("id").as(String.class), "%"+ubicacionDTO.getId() + "%"));
			}
			if(ubicacionDTO.getNombre() != null) {
				predicates.add(criteryBuilder.like(root.get("nombre"), "%"+ubicacionDTO.getNombre()+ "%"));
			}
			if(ubicacionDTO.getCiudad() != null) {
				predicates.add(criteryBuilder.like(root.get("ciudad"), "%"+ubicacionDTO.getCiudad()+ "%"));
			}
			if(ubicacionDTO.getDireccion() != null) {
				predicates.add(criteryBuilder.like(root.get("direccion"), "%"+ubicacionDTO.getDireccion()+ "%"));
			}
			if(ubicacionDTO.getTipo() != null) {
				predicates.add(criteryBuilder.equal(root.get("tipo"), ubicacionDTO.getTipo()));
			}
			if(ubicacionDTO.getEstaActiva() != null && ubicacionDTO.getEstaActiva()) {
				predicates.add(criteryBuilder.isTrue(root.get("estaActivo").as(Boolean.class)));				
			}
			if(ubicacionDTO.getEstaActiva() != null && !ubicacionDTO.getEstaActiva()) {
				predicates.add(criteryBuilder.isFalse(root.get("estaActivo").as(Boolean.class)));				
			}
			
			predicates.add(criteryBuilder.equal(root.get("empresa"), empresa));
			
			
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
