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
public class CategoryCriteria implements Criteria{
	
	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public List<Product> meetCriteria(List<Product> products) {
		if (this.getCategory().isEmpty())
			return products;
		
		List<Product> result = new ArrayList();
		
		products.stream().filter((product) -> (product.getCategory()
				.equalsIgnoreCase(this.getCategory()))).forEach((product) -> {
			result.add(product);
		});
		
		return result;
	}
	
	
	
	
}
