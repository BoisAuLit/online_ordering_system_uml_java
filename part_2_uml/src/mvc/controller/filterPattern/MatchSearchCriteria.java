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
public class MatchSearchCriteria implements Criteria{
	private String searchText;

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	@Override
	public List<Product> meetCriteria(List<Product> products) {
		List<Product> match = new ArrayList();
		
		products.stream().filter((product) -> 
				(product.getName().toLowerCase().contains(
				this.getSearchText().toLowerCase()))).forEach((product) -> {
					match.add(product);
		});
		
		return match;
	}
	
}
