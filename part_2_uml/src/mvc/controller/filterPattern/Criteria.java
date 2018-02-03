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
public interface Criteria {
		public List<Product> meetCriteria(List<Product> products);
}
