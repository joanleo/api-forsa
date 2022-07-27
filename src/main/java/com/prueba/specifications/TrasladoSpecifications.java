/**
 * 
 */
package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.dto.TrasladoDTO;
import com.prueba.entity.Traslado;

/**
 * @author Joan Leon
 *
 */
@Component
public class TrasladoSpecifications {
	
	public Specification<Traslado> obtenerTraslados(TrasladoDTO trasladoDTO, String orden){
		return (root, query, criteryBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			
			if(trasladoDTO.getDestino() != null) {
				predicates.add(criteryBuilder.equal(root.get("destino"), trasladoDTO.getDestino()));
			}
			if(trasladoDTO.getEstadoTraslado() != null) {
				predicates.add(criteryBuilder.equal(root.get("destino"), trasladoDTO.getEstadoTraslado()));
			}
			if(trasladoDTO.getFechaIngreso() != null) {
				predicates.add(criteryBuilder.equal(root.get("fechaIngreso"), trasladoDTO.getFechaIngreso()));
			}
			
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}

}
