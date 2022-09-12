/**
 * 
 */
package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.entity.Permiso;

/**
 * @author Joan Leon
 *
 */
@Component
public class PermisoSpecifications {
	
	public Specification<Permiso> getPermiso(String url, String metodo){
		return (root, query, criteryBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			
			predicates.add(criteryBuilder.like(root.get("url").as(String.class), "%"+url + "%"));
			predicates.add(criteryBuilder.like(root.get("metodo").as(String.class), "%"+metodo + "%"));
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}

}
