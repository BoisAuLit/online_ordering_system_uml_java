/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model.basicTypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import mvc.controller.ConnectionHandler;

/**
 *
 * @author bohao
 */
public class ItemInOrder{

	private String id;
	private Product product;
	private float weight;
	private int quantity;
	private float priceOfProducts;
	private float priceOfShipping;
	private float priceInTotal;
	private Shipping shipping;


	public String getId() {
		return id;
	}

	public Product getProduct() {
		return product;
	}

	public float getWeight() {
		return weight;
	}

	public int getQuantity() {
		return quantity;
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

	public Shipping getShipping() {
		return shipping;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
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

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}
	
	
	public static void buildBasicInfo(ResultSet rs, ItemInOrder itemInOrder) throws SQLException {
		// 1. Set id, weight, quantity, priceOfProducts for
		//    the ItemInOrder object
		itemInOrder.setId(rs.getString("id"));
		itemInOrder.setWeight(Float.valueOf(rs.getString("weight")));
		itemInOrder.setQuantity(Integer.valueOf(rs.getString("quantity")));
		itemInOrder.setPriceOfProducts(Float.valueOf(rs.getString(
				"price_of_products")));
		// 2. Set priceOfShipping, priceInTotal for the ItemInOrder object
		itemInOrder.setPriceOfShipping(Float.valueOf(rs.getString(
				"price_of_shipping")));
		itemInOrder.setPriceInTotal(Float.valueOf(rs.getString(
				"price_in_total")));
	}

	public static void buildExtrainfo(ResultSet rs, ItemInOrder itemInOrder) throws SQLException {
		// 3. Set the product attribute for the ItemInOrder object
		String productId = rs.getString("product_id");
		ResultSet productSet = ConnectionHandler.getResultSet(
				"SELECT * FROM product WHERE id='" + productId + "'");
		productSet.next();
		itemInOrder.setProduct(Product.buildProduct(productSet));

		// 4. Set the status of the ItemInOrder object
//		String orderProductStatusId = rs.getString("order_product_status_id");
//		ResultSet orderProductStatusSet = ConnectionHandler.getResultSet(
//				"SELECT * FROM order_product_status WHERE "
//				+ "id='" + orderProductStatusId + "'");
//		orderProductStatusSet.next();
//		itemInOrder.setStatus(orderProductStatusSet.getString("name"));
		// 5. Set the shipping information of the ItemInOrder object
		ResultSet shippingSet = ConnectionHandler.getResultSet(
				"SELECT * FROM shipping WHERE order_product_id='" 
						+ itemInOrder.getId() +"'");
		shippingSet.next();
		itemInOrder.setShipping(Shipping.buildShipping(shippingSet));
	}

	public static ItemInOrder buildItemInOrder(ResultSet rs) throws SQLException {
		ItemInOrder itemInOrder = new ItemInOrder();
		buildBasicInfo(rs, itemInOrder);
		buildExtrainfo(rs, itemInOrder);
		return itemInOrder;
	}

	public static ItemInOrder buildItemInOrder(ItemInCart itemInCart) {
		ItemInOrder itemInOrder = new ItemInOrder();
		
		itemInOrder.setProduct(itemInCart.getProduct());
		itemInOrder.setPriceOfProducts(itemInCart.getPriceOfProducts());
		itemInOrder.setWeight(itemInCart.getWeight());
		itemInOrder.setQuantity(itemInCart.getQuantity());
		itemInOrder.setPriceOfProducts(itemInCart.getPriceOfProducts());
		
		
		//**********************************************************************
		// get default shipping method (string)
		String shippingMethod = ShippingMethod.getDefaultShippingMethod();
		// get default percentage of the default shipping method
		float percentageShippingCost
				= ShippingMethod.getPercentage(shippingMethod);
		// calculate the shipping cost according to the shipping percentage
		// that we computed just now
		float shippingCost
				= itemInOrder.getPriceOfProducts() * percentageShippingCost;
		// calculate the full price
		float fullPrice
				= itemInOrder.getPriceOfProducts() 
				* (1 + percentageShippingCost);
		// set the shipping cost of the order
		itemInOrder.setPriceOfShipping(shippingCost);
		// set the full price of the order
		itemInOrder.setPriceInTotal(fullPrice);
		//**********************************************************************
		
		
		String query;
		PreparedStatement ps;
		ResultSet rs;
		String orderProductId = null;
		Order order = Order.getInstance();
		int orderId = Integer.valueOf(order.getId());
		int productId = Integer.valueOf(itemInCart.getProduct().getId());
		
		
//		// insert into the data base
		query = "INSERT INTO order_product "
				+ "VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?)";
		ps = ConnectionHandler.getPreparedStatement(query);
		ConnectionHandler.setInt(ps, 1, orderId);
		ConnectionHandler.setInt(ps, 2, productId);
		ConnectionHandler.setInt(ps, 3, itemInOrder.getQuantity());
		ConnectionHandler.setFloat(ps, 4, itemInOrder.getWeight());
		ConnectionHandler.setFloat(ps, 5, itemInOrder.getPriceOfProducts());
		ConnectionHandler.setFloat(ps, 6, itemInOrder.getPriceOfShipping());
		ConnectionHandler.setFloat(ps, 7, itemInOrder.getPriceInTotal());
		ConnectionHandler.setString(ps, 8, "Y");
		ConnectionHandler.executeUpdate(ps);
		
		
		// get the id of the ItemInOrder object that we inserted
		// into the database just now
		query = "SELECT * FROM order_product WHERE order_id = ? "
				+ "AND product_id = ?";
		ps = ConnectionHandler.getPreparedStatement(query);
		ConnectionHandler.setInt(ps, 1, orderId);
		ConnectionHandler.setInt(ps, 2, productId);
		rs = ConnectionHandler.executeQuery(ps);
		try {
			orderProductId = rs.getString("id");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		order.setId(orderProductId);
		
		Shipping shipping = Shipping.buildDefaultShipping(itemInOrder);
		itemInOrder.setShipping(shipping);
		
		return itemInOrder;
		
	}
	
	public static ArrayList<ItemInOrder> 
		buildItemInOrders(ArrayList<ItemInCart> itemsInCart) {
		
		ArrayList<ItemInOrder> itemsInOrder = new ArrayList();
		
		for (ItemInCart itemInCart : itemsInCart)
			itemsInOrder.add(buildItemInOrder(itemInCart));
		
		return itemsInOrder;
	}
}
