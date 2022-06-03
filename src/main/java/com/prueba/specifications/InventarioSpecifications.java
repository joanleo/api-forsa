package com.prueba.specifications;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.prueba.entity.MovInventario;

@Component
public class InventarioSpecifications {

	public Specification<MovInventario> getInvenId(String id){
		return (root, query, criteryBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();
			
			predicates.add(criteryBuilder.like(root.get("id").as(String.class), "%"+id+ "%"));
			
			return criteryBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	public Specification<MovInventario> getInvBetweenDate(Date desde, Date hasta) {
		return (root, query, criteryBuilder) ->{
			
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(criteryBuilder.between(root.<Date>get("fecha"), desde, hasta));
			return criteryBuilder.and(predicates .toArray(new Predicate[0]));
		};
	}
}
