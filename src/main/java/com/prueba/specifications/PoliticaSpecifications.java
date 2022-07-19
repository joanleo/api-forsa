/**
 * 
 */
package com.prueba.specifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.security.entity.Politica;

/**
 * @author Joan Leon
 *
 */
@Component
public class PoliticaSpecifications {

	public Specification<Politica> getPoliticas(Long nit){
		return (root, query, criteriaBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();
			
			if(nit != null) {
				predicates.add(criteriaBuilder.equal(root.get("rol").get("empresa").get("nit"), nit));
				}
			
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
