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
public class BoughtItem {
	private String id;
	private Product product;
	private int quantity;
	private float weight;
	private float price;
	private Date date;

	// others can't use the constructor of this class
	// in other word, we prohibit the use of new keyword
	// outside this class
	private BoughtItem() {
		
	}
	
	private static void buildBasicInfo(ResultSet rs, BoughtItem boughtItem) throws SQLException {
		// set the BoughtItem object's id, quantity, weight, price and date_bought
		boughtItem.setId(rs.getString("id"));
		boughtItem.setQuantity(rs.getInt("quantity"));
		boughtItem.setWeight(rs.getFloat("weight"));
		boughtItem.setPrice(rs.getFloat("price"));
		boughtItem.setDate(rs.getDate("date_bought"));
	}
	
	private static void buildExtraInfo(ResultSet rs, BoughtItem boughtItem) throws SQLException {
		String productId = rs.getString("product_id");
		
		// set the Product object that the BoughtItem object references
				// delete the ItemInOrder record in the database
		String query = "SELECT * FROM product WHERE id = ?";
		PreparedStatement ps = ConnectionHandler.getPreparedStatement(query);
		ConnectionHandler.setInt(ps, 1, Integer.valueOf(productId));
		ResultSet resultSet = ConnectionHandler.executeQuery(ps);
		if (!resultSet.isBeforeFirst()) {
			System.out.println("**********************************"
					+ "error, no product is found!"
					+ "*******************************************");
			return;
		}
		resultSet.next();
		boughtItem.setProduct(Product.buildProduct(resultSet));
		
	}
	
	private static BoughtItem buildBoughtItem(ResultSet rs) throws SQLException {
		BoughtItem boughtItem = new BoughtItem();
		buildBasicInfo(rs, boughtItem);
		buildExtraInfo(rs, boughtItem);
		return boughtItem;
	}
		
	public static ArrayList<BoughtItem> buildBoughtItems(ResultSet rs) {
		ArrayList<BoughtItem> boughtItems = new ArrayList();
		try {
			while (rs.next())
				boughtItems.add(buildBoughtItem(rs));
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return boughtItems;
	}

	public String getId() {
		return id;
	}

	public Product getProduct() {
		return product;
	}

	public int getQuantity() {
		return quantity;
	}

	public float getWeight() {
		return weight;
	}

	public float getPrice() {
		return price;
	}

	public Date getDate() {
		return date;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
