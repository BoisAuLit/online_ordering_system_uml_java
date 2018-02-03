package mvc.controller.StrategyPattern;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import mvc.model.basicTypes.Product;

public class PriceHighToLowSorter implements ProductSorter{
	private final Comparator comparator;
	
	public PriceHighToLowSorter() {
		comparator = (Comparator<Product>) 
				(Product lhs, Product rhs) -> lhs.getPrice() > rhs.getPrice()
				? -1 : (lhs.getPrice() < rhs.getPrice()) ? 1 : 0
		;
	}

	@Override
	public void sort(List<Product> products) {
		Collections.sort(products, comparator);
	}
}
