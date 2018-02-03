/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model.basicTypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import mvc.controller.ConnectionHandler;

/**
 *
 * @author bohao
 */
public class Administrator extends User{
	List<SimpleCustomer> customers;
	
	private Administrator() {
		
	}
	
	private static class SingletonHolder {
		private static final Administrator ADMINISTRATOR = new Administrator();
	}

	public static Administrator getInstance() {
		return SingletonHolder.ADMINISTRATOR;
	}
	
	private static Administrator getNewInstance() {
		reset();
		return getInstance();
	}
	
	public static void reset() {
		Administrator administrator = Administrator.getInstance();
		administrator.resetUser();
	}

	public List<SimpleCustomer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<SimpleCustomer> customers) {
		this.customers = customers;
	}

	public static Administrator buildAdministrator() {
		Administrator administrator = Administrator.getNewInstance();

		String query;
		PreparedStatement ps;
		ResultSet rs;
		query = "SELECT * FROM customer";
		ps = ConnectionHandler.getPreparedStatement(query);
		rs = ConnectionHandler.executeQuery(ps);

		administrator.setCustomers(SimpleCustomer.buildSimpleCustomers(rs));

		System.out.println("*************************************************");
		System.out.println("customer found: " + administrator.getCustomers().size());
		System.out.println("*************************************************");

		
		return administrator;
	}
	
}
