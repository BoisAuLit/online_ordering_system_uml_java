/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model.basicTypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import mvc.controller.ConnectionHandler;

/**
 *
 * @author bohao
 */
public class Shipping {

	private String id;
	private String shippingMethod;
	private Address destination;

	public String getId() {
		return id;
	}


	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public Address getDestination() {
		return destination;
	}

	public void setDestination(Address destination) {
		this.destination = destination;
	}

	public static void buildBasicInfo(ResultSet rs, Shipping shipping) throws SQLException {
//		rs.next();
		shipping.setId(rs.getString("id"));
	}

	public static void buidlExtrainfo(ResultSet rs, Shipping shipping) throws SQLException {
//		// 1. Set the status of the Shipping object
//		String shippingStatusId = rs.getString("shipping_status_id");
//		ResultSet shippingStatusSet = ConnectionHandler.getResultSet(
//				"SELECT * FROM shipping_status where id='"
//				+ shippingStatusId + "'");
//		shippingStatusSet.next();
//		shipping.setStatus(shippingStatusSet.getString("name"));

		// 2. Set the shipping method of the shipping object
		String shippingMethodId = rs.getString("shipping_method_id");
		ResultSet shippingMethodSet = ConnectionHandler.getResultSet(
				"SELECT * FROM shipping_method where id='"
				+ shippingMethodId + "'");
		shippingMethodSet.next();
		shipping.setShippingMethod(shippingMethodSet.getString("name"));

		// 3. Set the destination (of type Address) for the Shipping object
		String addressId = rs.getString("address_id");
		
		
		
		ResultSet addressSet = ConnectionHandler.getResultSet(
				"SELECT * FROM address WHERE id='" + addressId + "'");
		addressSet.next();
		shipping.setDestination(Address.buildAddress(addressSet));
	}

	public static Shipping buildShipping(ResultSet rs) throws SQLException {
		Shipping shipping = new Shipping();
		buildBasicInfo(rs, shipping);
		buidlExtrainfo(rs, shipping);
		return shipping;
	}
	
	public static Shipping buildDefaultShipping(ItemInOrder itemInOrder) {
		Shipping shipping = new Shipping();
		shipping.setDestination(Customer.getInstance().getDefaultAddress());
		shipping.setShippingMethod(ShippingMethod.getDefaultShippingMethod());
		
		int itemInOrderId = Integer.valueOf(itemInOrder.getId());
		int shippingMethodId = 
				Integer.valueOf(ShippingMethod.getDefaultShippingMethodId());
		int addressId = Integer.valueOf(
				Customer.getInstance().getDefaultAddress().getId());
		PreparedStatement ps;
		String query;
		ResultSet rs;
		String shippingId = null;
		
		// insert the shipping info into the database
		query = "INSERT INTO shipping VALUES(NULL, ?, ?, ?, ?)";
		ps = ConnectionHandler.getPreparedStatement(query);
		ConnectionHandler.setInt(ps, 1, itemInOrderId);
		ConnectionHandler.setInt(ps, 2, shippingMethodId);
		ConnectionHandler.setInt(ps, 3, addressId);
		ConnectionHandler.setString(ps, 4, "Y");
		ConnectionHandler.executeUpdate(ps);
		
		
		// get the id of the Shipping object that we inserted into
		// the database just now
		query = "SELECT * FROM shipping WHERE order_product_id = ?";
		ps = ConnectionHandler.getPreparedStatement(query);
		ConnectionHandler.setInt(ps, 1, itemInOrderId);
		rs = ConnectionHandler.executeQuery(ps);
		try {
			shippingId = rs.getString("id");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
		shipping.setId(shippingId);
		
		return shipping;
	}
}
