/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model.basicTypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import mvc.controller.ConnectionHandler;

/**
 * The customer class is a singleton because there could only be at most one
 * customer at any moment
 *
 * @author bohao
 */
public class Customer extends User {

	private Address defaultAddress;
	private ArrayList<Address> otherAddresses;
	private Cart cart;
	private Order order;
	private ArrayList<BoughtItem> boughtItems;

	private Customer() {
	}
	
	private static class SingletonHolder {
		private static final Customer CUSTOMER = new Customer();
	}

	public static Customer getInstance() {
		return SingletonHolder.CUSTOMER;
	}
	
	private static Customer getNewInstance() {
		reset();
		return getInstance();
	}
	
	public static void reset() {
		Customer customer = Customer.getInstance();
		customer.resetUser();
		customer.setDefaultAddress(null);
		customer.setOtherAddresses(null);
		customer.setCart(null);
		customer.setOrder(null);
		customer.setBoughtItems(null);
	}
	
	public Address getDefaultAddress() {
		return defaultAddress;
	}

	public ArrayList<Address> getOtherAddresses() {
		return otherAddresses;
	}

	public Cart getCart() {
		return cart;
	}

	public Order getOrder() {
		return order;
	}

	public void setDefaultAddress(Address defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

	public void setOtherAddresses(ArrayList<Address> otherAddresses) {
		this.otherAddresses = otherAddresses;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public ArrayList<BoughtItem> getBoughtItems() {
		return boughtItems;
	}

	public void setBoughtItems(ArrayList<BoughtItem> boughtItems) {
		this.boughtItems = boughtItems;
	}

	private static void buildBasicInfo(ResultSet rs, Customer customer)
			throws SQLException {
		// Move to the first row of the reuslt set because 
		// it is initially positioned at the before first row
		rs.next();
		// 1. Set id, userName, firstName, lastName, emailAddress and
		//    phoneNumber for the Customer object
		customer.setId(rs.getString("id"));
		customer.setUserName(rs.getString("user_name"));
		customer.setFirstName(rs.getString("first_name"));
		customer.setLastName(rs.getString("last_name"));
		customer.setEmailAddress(rs.getString("email_address"));
		customer.setPhoneNumber(rs.getString("phone_number"));

	}

	private static void buildExtraInfo(ResultSet rs, Customer customer)
			throws SQLException {
		// 2. Build the customer's address by default and
		//    other addresses if any
		if (rs.getString("has_address").equalsIgnoreCase("Y")) {
			ResultSet addressSet = ConnectionHandler.getResultSet(
					"SELECT * FROM address "
					+ "WHERE customer_id='" + customer.getId() + "'");
			ArrayList<Address> otherAddresses = new ArrayList();
			while (addressSet.next()) {
				if (addressSet.getString("is_address_by_default")
						.equalsIgnoreCase("Y")) {
					customer.setDefaultAddress(
							Address.buildAddress(addressSet));
				} else {
					otherAddresses.add(Address.buildAddress(addressSet));
				}
			}
			if(otherAddresses.size()>0)
				customer.setOtherAddresses(otherAddresses);
		}

		// 3. Build the customer's shopping list information if any
		//    Cart <==> Shopping list
		if (rs.getString("has_shopping_list").
				equalsIgnoreCase("Y")) {
			ResultSet cartSet = ConnectionHandler.getResultSet(
					"SELECT * FROM cart WHERE customer_id='"
					+ customer.getId() + "'");
			cartSet.next();
			customer.setCart(Cart.buildCart(cartSet));
		}
		
		String query;
		PreparedStatement ps;
		ResultSet result;
		String customerId = customer.getId();
		
		// 4. Build the customer's order information if any
		if (rs.getString("has_order").equalsIgnoreCase("Y")) {			
			query = "SELECT * FROM `order` WHERE customer_id=?";
			ps = ConnectionHandler.getPreparedStatement(query);
			ConnectionHandler.setInt(ps, 1, Integer.valueOf(customerId));
			result = ConnectionHandler.executeQuery(ps);
			result.next();
			customer.setOrder(Order.buildOrder(result));
		}
		
		// 5. Build the customers bought items if any
		query = "SELECT * FROM bought_item WHERE customer_id=?";
		ps = ConnectionHandler.getPreparedStatement(query);
		ConnectionHandler.setInt(ps, 1, Integer.valueOf(customer.getId()));
		ResultSet boughtItemsSet = ConnectionHandler.executeQuery(ps);




		// if no bought item is found, we do nothing
		if (!boughtItemsSet.isBeforeFirst()) {
			return;
		}
		customer.setBoughtItems(BoughtItem.buildBoughtItems(boughtItemsSet));
	}

	public boolean has_bought_items() {
		return boughtItems != null;
	}
	
	public static Customer buildCustomer(String userName) throws SQLException {
		ResultSet rs = ConnectionHandler.getResultSet(
				"SELECT * FROM customer WHERE user_name='" + userName + "'");
		Customer customer = Customer.getNewInstance();
		buildBasicInfo(rs, customer);
		buildExtraInfo(rs, customer);
		return customer;
	}
	
	public static void display() {
		Customer c = Customer.getInstance();
		System.out.println("\tCustomer information:\n"
				+ "Id: " + c.getId() + "\n"
				+ "First name: " + c.getFirstName() + "\n"
				+ "Last name : " + c.getLastName() + "\n"
				+ "Email address : " + c.getEmailAddress() + "\n"
				+ "Phone number : " + c.getPhoneNumber() + "\n"
				+ "Cart: \n"
				+ c.getCart());
	}
	
	public boolean has_shopping_list() {
		return cart != null;
	}
}
