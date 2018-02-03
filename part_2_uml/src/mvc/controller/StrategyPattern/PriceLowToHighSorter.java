/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.controller.StrategyPattern;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import mvc.model.basicTypes.Product;

/**
 *
 * @author bohao
 */
public class PriceLowToHighSorter implements ProductSorter{

	private final Comparator comparator;
	
	public PriceLowToHighSorter() {
		comparator = (Comparator<Product>) 
				(Product lhs, Product rhs) -> lhs.getPrice() < rhs.getPrice()
				? -1 : (lhs.getPrice() > rhs.getPrice()) ? 1 : 0
		;
	}

	@Override
	public void sort(List<Product> products) {
		Collections.sort(products, comparator);
	}
	
}
