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
public class StoreCriteria implements Criteria{
	private String store;

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	@Override
	public List<Product> meetCriteria(List<Product> products) {
		if (this.getStore().isEmpty())
			return products;
		
		List<Product> result = new ArrayList();
		products.stream().filter((product) -> (product.getStore()
				.equalsIgnoreCase(this.getStore()))).forEach((product) -> {
			result.add(product);
		});
		
		return result;
	}
}
