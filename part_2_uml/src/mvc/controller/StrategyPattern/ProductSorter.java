package mvc.controller.StrategyPattern;

import java.util.List;
import mvc.model.basicTypes.Product;

public interface ProductSorter {
	public void sort(List<Product> products);
}
