/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model.basicTypes;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mvc.controller.ConnectionHandler;

/**
 *
 * @author bohao
 */
public class Order{
	private String id;
	private float weight;	
	private float priceOfProducts;
	private float priceOfShipping;
	private float priceInTotal;
	private int numberOfItems;
	private ArrayList items;
	private String status;
	private String datePlaced;
	private String comments;

	private Order() {
	}
	
	private static class SingletonHolder {
		private static final Order ORDER = new Order();
	}

	public static Order getInstance() {
		return SingletonHolder.ORDER;
	}
	
	private static Order getNewInstance() {
		reset();
		return getInstance();
	}

	private static void reset() {
		Order order = Order.getInstance();
		order.setId(null);
		order.setWeight(.0f);
		order.setPriceOfProducts(.0f);
		order.setPriceOfShipping(.0f);
		order.setPriceInTotal(.0f);
		order.setNumberOfItems(0);
		order.setItems(null);
		order.setStatus(null);
		order.setDatePlaced(null);
		order.setComments(null);
	}
	
	public String getId() {
		return id;
	}

	public float getWeight() {
		return weight;
	}

	public float getPriceOfProducts() {
		return priceOfProducts;
	}

	public float getPriceOfShipping() {
		return priceOfShipping;
	}

	public float getPriceInTotal() {
		return priceInTotal;
	}

	public int getNumberOfItems() {
		return numberOfItems;
	}

	public ArrayList getItems() {
		return items;
	}

	public String getStatus() {
		return status;
	}

	public String getDatePlaced() {
		return datePlaced;
	}

	public String getComments() {
		return comments;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public void setPriceOfProducts(float priceOfProducts) {
		this.priceOfProducts = priceOfProducts;
	}

	public void setPriceOfShipping(float priceOfShipping) {
		this.priceOfShipping = priceOfShipping;
	}

	public void setPriceInTotal(float priceInTotal) {
		this.priceInTotal = priceInTotal;
	}

	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
	}

	public void setItems(ArrayList items) {
		this.items = items;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setDatePlaced(String datePlaced) {
		this.datePlaced = datePlaced;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
	public static void buildBasicInfo(ResultSet rs, Order order)
			throws SQLException {
		// 1. Set id, weight, priceOfProducts and numberOfItems
		//    for the Order object
		order.setId(rs.getString("id"));
		order.setWeight(Float.valueOf(rs.getString("weight")));
		order.setPriceOfProducts(Float.valueOf(rs.getString(
				"price_of_products")));
		order.setNumberOfItems(Integer.valueOf(rs.getString(
				"number_of_items")));
		// 3. Set datePlaced, comments, priceOfShipping, priceInTotal
		//    for the Order object
		order.setDatePlaced(rs.getString("date_placed"));
		order.setComments(rs.getString("comments"));
		order.setPriceOfShipping(Float.valueOf(rs.getString(
				"price_of_shipping")));
		order.setPriceInTotal(Float.valueOf(rs.getString("price_in_total")));
	}

	public static void buildExtraInfo(ResultSet rs, Order order)
			throws SQLException {
		// 2. Set the order status of the order				
		String orderStatusId = rs.getString("order_status_id");
		ResultSet orderStatusSet = ConnectionHandler.getResultSet(
				"SELECT * FROM order_status WHERE id='" 
						+ orderStatusId + "'");
		orderStatusSet.next();
		order.setStatus(orderStatusSet.getString("name"));
		// 4. Set the items in the Order object
		ResultSet itemInOrderSet = ConnectionHandler.getResultSet(
				"SELECT * FROM order_product WHERE order_id='" 
						+ order.getId() + "'");
		ArrayList<ItemInOrder> items = new ArrayList();
		while (itemInOrderSet.next()) {
			items.add(ItemInOrder.buildItemInOrder(itemInOrderSet));
		}
		order.setItems(items);
	}

	public static Order buildOrder(ResultSet rs) throws SQLException {
		Order order = Order.getNewInstance();
		buildBasicInfo(rs, order);
		buildExtraInfo(rs, order);
		return order;
	}

	// build an order from the cart
	public static Order builderOrder(Cart cart) {
		Order order = Order.getNewInstance();
		order.setWeight(cart.getWeight());
		order.setNumberOfItems(cart.getNumberOfItems());
		order.setStatus(OrderStatus.getDefaultOrderStatus());
		java.util.Date utilDate= new java.util.Date();
		Date sqlDate = new java.sql.Date(utilDate.getTime());;
		order.setDatePlaced(sqlDate.toString());
		order.setPriceOfProducts(cart.getPriceOfProducts());
		
		// get default shipping method (string)
		String shippingMethod = ShippingMethod.getDefaultShippingMethod();
		// get default percentage of the default shipping method
		float percentageShippingCost 
				= ShippingMethod.getPercentage(shippingMethod);
		// calculate the shipping cost according to the shipping percentage
		// that we computed just now
		float shippingCost 
				= order.getPriceOfProducts() * percentageShippingCost;
		// calculate the full price
		float fullPrice 
				= order.getPriceOfProducts() * (1 + percentageShippingCost);
		// set the shipping cost of the order
		order.setPriceOfShipping(shippingCost);
		// set the full price of the order
		order.setPriceInTotal(fullPrice);
		
		//**********************************************************************
		// insert the Order object into the database		
		String orderId = null;
		String query;
		PreparedStatement ps;
		ResultSet rs;
		int customerId = Integer.valueOf(Customer.getInstance().getId());
		int orderStatusId = 
				Integer.valueOf(OrderStatus.getDefaultOrderStatusId());
		Date datePlaced = sqlDate;

		query = "INSERT INTO order VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		ps = ConnectionHandler.getPreparedStatement(query);
		ConnectionHandler.setInt(ps, 1, customerId);
		ConnectionHandler.setInt(ps, 2, orderStatusId);
		ConnectionHandler.setDate(ps, 3, datePlaced);
		ConnectionHandler.setInt(ps, 4, order.getNumberOfItems());
		ConnectionHandler.setFloat(ps, 5, order.getWeight());
		ConnectionHandler.setString(ps, 6, "comments");
		ConnectionHandler.setFloat(ps, 7, order.getPriceOfProducts());
		ConnectionHandler.setFloat(ps, 8, order.getPriceOfShipping());
		ConnectionHandler.setFloat(ps, 9, order.getPriceInTotal());
		ConnectionHandler.setString(ps, 10, "Y");
		
		query = "SELECT * FROM `order` WHERE customer_id = ?";
		ps = ConnectionHandler.getPreparedStatement(query);
		ConnectionHandler.setInt(ps, 1, customerId);
		rs = ConnectionHandler.executeQuery(ps);
		try {
			orderId = rs.getString("id");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		order.setId(orderId);
		
		ArrayList<ItemInCart> itemsInCart = cart.getItems();
		order.setItems(ItemInOrder.buildItemInOrders(itemsInCart));
		
		//**********************************************************************
		
		Cart.removeRecordInDatabase();
		
		query = "UPDATE customer SET has_order = 'Y'";
		ps = ConnectionHandler.getPreparedStatement(query);
		ConnectionHandler.executeUpdate(ps);
		
		//**********************************************************************
		// super mega important
		// don't forget to set the flag have order to 'Y'
		//**********************************************************************
		
		return order;
	}
}
