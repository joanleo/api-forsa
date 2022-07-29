/**
 * 
 */
package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.entity.Empresa;
import com.prueba.entity.Salida;

/**
 * @author Joan Leon
 *
 */
@Component
public class SalidaSpecifications {
	
	public Specification<Salida> obtenerFabricantes(Salida salida, Empresa empresa){
		return (root, query, criteryBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();
			
			if(salida.getNumDocumento() != null) {
				predicates.add(criteryBuilder.like(root.get("numDocumento"), "%" + salida.getNumDocumento() + "%"));
			}
			if(salida.getUsuarioCrea() != null) {
				predicates.add(criteryBuilder.equal(root.get("usuarioCrea"), salida.getUsuarioCrea()));
			}
			predicates.add(criteryBuilder.equal(root.get("empresa"), empresa));
						
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
			
		};
	}

}
