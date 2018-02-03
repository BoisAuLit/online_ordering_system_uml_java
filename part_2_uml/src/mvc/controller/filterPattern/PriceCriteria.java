/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.controller.filterPattern;

/**
 *
 * @author bohao
 */
public abstract class PriceCriteria implements Criteria{
	private float limit;

	public float getLimit() {
		return limit;
	}

	public void setLimit(float limit) {
		this.limit = limit;
	}

}
