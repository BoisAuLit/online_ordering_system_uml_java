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
public class ItemInCart {

	private String id;
	private Product product;
	private float weight;
	private int quantity;
	private float priceOfProducts;

	public ItemInCart() {
		id = null;
		product = null;
		weight = .0f;
		quantity = 0;
		priceOfProducts = .0f;
	}
	
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

	public static void buildBasicInfo(ResultSet rs, ItemInCart itemInCart) throws SQLException {
		// 1. Set id, weight, quantity, priceOfProducts of ItemInCart
		itemInCart.setId(rs.getString("id"));
		itemInCart.setWeight(Float.valueOf(rs.getString("weight")));
		itemInCart.setQuantity(Integer.valueOf(rs.getString("quantity")));
		itemInCart.setPriceOfProducts(Float.valueOf(rs.getString(
				"price_of_products")));
	}

	public static void buildExtraInfo(ResultSet rs, ItemInCart itemInCart) throws SQLException {
		// 2. Set the product of the ItemInCart
		ResultSet productSet = ConnectionHandler.getResultSet(
				"SELECT * FROM product WHERE id='" 
						+ rs.getString("product_id") + "'");
		productSet.next();
		itemInCart.setProduct(Product.buildProduct(productSet));
	}

	public static ItemInCart buildItemInCart(ResultSet rs) throws SQLException {
		ItemInCart itemInCart = new ItemInCart();
		buildBasicInfo(rs, itemInCart);
		buildExtraInfo(rs, itemInCart);
		return itemInCart;
	}
	
	@Override
	public String toString() {
		return "Item in cart: \n" 
				+ "id: " + id + "\n"
				+ "product_id: " + product.getId() + "\n"
				+ "weight: " + weight + "\n"
				+ "quantity: " + quantity +"\n"
				+ "price of products: " + priceOfProducts + "\n";
	}
	
	public void changeQuantity(int newQuantity) {
		quantity = newQuantity;
		priceOfProducts = product.getPrice() * newQuantity;
		weight = product.getWeight() * newQuantity;
		
		int itemInCartId = Integer.valueOf(id);
		
		// change the quantity in the database
		String query = "UPDATE cart_product SET quantity = ?, "
				+ "weight = ?, price_of_products = ? WHERE id = ?";
		PreparedStatement ps = ConnectionHandler.getPreparedStatement(query);
		ConnectionHandler.setInt(ps, 1, newQuantity);
		ConnectionHandler.setFloat(ps, 2, weight);
		ConnectionHandler.setFloat(ps, 3, priceOfProducts);
		ConnectionHandler.setInt(ps, 4, itemInCartId);
		ConnectionHandler.executeUpdate(ps);
	}
	
	public void build(Product product) {
		id = null;
		this.product = product;
		weight = product.getWeight();
		quantity = 1;
		priceOfProducts = product.getPrice();
	}
	
	public void removeRecordInDatabase() {
		Cart cart = Cart.getInstance();
		
		String query;
		PreparedStatement ps;
		int cartId = Integer.valueOf(cart.getId());
		
		query = "DELETE FROM cart_product WHERE cart_id = ?";
		ps = ConnectionHandler.getPreparedStatement(query);
		ConnectionHandler.setInt(ps, 1, cartId);
		ConnectionHandler.executeUpdate(ps);
	}
}
