/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model.basicTypes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mvc.controller.ConnectionHandler;

/**
 *
 * @author bohao
 */
public class SimpleCustomer extends User {

	private Address defaultAddress;
	private ArrayList<Address> otherAddresses;
	
	private SimpleCustomer() {

	}

	public Address getDefaultAddress() {
		return defaultAddress;
	}

	public ArrayList<Address> getOtherAddresses() {
		return otherAddresses;
	}

	public void setDefaultAddress(Address defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

	public void setOtherAddresses(ArrayList<Address> otherAddresses) {
		this.otherAddresses = otherAddresses;
	}

	
	
	public static SimpleCustomer buildSimpleCustomer(ResultSet rs) {
		SimpleCustomer customer = new SimpleCustomer();
		try {
			String customerId = rs.getString("id");
			customer.setId(customerId);
			customer.setFirstName(rs.getString("first_name"));
			customer.setLastName(rs.getString("last_name"));
			customer.setUserName(rs.getString("user_name"));
			customer.setEmailAddress(rs.getString("email_address"));
			customer.setPhoneNumber(rs.getString("phone_number"));

			if (rs.getString("has_address").equalsIgnoreCase("Y")) {
				ResultSet addressSet = ConnectionHandler.getResultSet(
						"SELECT * FROM address "
						+ "WHERE customer_id='" + customer.getId() + "'");
				ArrayList<Address> otherAddresses = new ArrayList();
				while (addressSet.next()) {
					if (addressSet.getString("is_address_by_default") .equalsIgnoreCase("Y")) {
						customer.setDefaultAddress(
								Address.buildAddress(addressSet));
					} else {
						otherAddresses.add(Address.buildAddress(addressSet));
					}
				}
				if (otherAddresses.size() > 0) {
					customer.setOtherAddresses(otherAddresses);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}
		return customer;
	}
	
	public static ArrayList<SimpleCustomer> buildSimpleCustomers(ResultSet rs) {
		ArrayList<SimpleCustomer> customers = new ArrayList();
		try {
			while (rs.next())
				customers.add(buildSimpleCustomer(rs));
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return customers;
	}	
	
	@Override
	public String toString() {
		return super.toString();
	}
}
