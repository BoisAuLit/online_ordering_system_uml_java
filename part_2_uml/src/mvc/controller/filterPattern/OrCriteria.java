/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.controller.filterPattern;

import java.util.List;
import mvc.model.basicTypes.Product;

/**
 *
 * @author bohao
 */
public class OrCriteria implements Criteria {

	private final Criteria criteria;
	private final Criteria otherCriteria;

	public OrCriteria(Criteria criteria, Criteria otherCriteria) {
		this.criteria = criteria;
		this.otherCriteria = otherCriteria;
	}

	@Override
	public List<Product> meetCriteria(List<Product> products) {
		List<Product> firstCriteriaItems = criteria.meetCriteria(products);
		List<Product> otherCriteriaItems = otherCriteria.meetCriteria(products);

		otherCriteriaItems.stream().filter((product) -> 
				(!firstCriteriaItems.contains(product))).forEach((product) -> {
			firstCriteriaItems.add(product);
		});
		return firstCriteriaItems;
	}
}
