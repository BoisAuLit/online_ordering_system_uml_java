/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.controller.filterPattern;

import java.util.ArrayList;
import java.util.List;
import mvc.model.basicTypes.Product;

/**
 *
 * @author bohao
 */
public class CheaperCriteria extends PriceCriteria{

	@Override
	public List<Product> meetCriteria(List<Product> products) {

		if (this.getLimit() == -1) {
			return products;
		}
		
		List<Product> cheaperProducts = new ArrayList();
		
		products.stream().filter((product) -> 
				(product.getPrice() <= this.getLimit())).forEach((product) -> {
			cheaperProducts.add(product);
		});
		
		return cheaperProducts;
	}
}
