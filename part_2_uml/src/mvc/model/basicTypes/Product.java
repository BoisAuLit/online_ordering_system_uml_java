/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model.basicTypes;

import java.sql.ResultSet;
import java.sql.SQLException;
import mvc.controller.ConnectionHandler;

/**
 *
 * @author bohao
 */
public class Product {

	private String id;
	private String name;
	private String shortDescription;
	private String longDescription;
	private String smallImageUrl;
	private String bigImageUrl;
	private String store;
	private String category;
	private float weight;
	private float price;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public String getSmallImageUrl() {
		return smallImageUrl;
	}

	public String getBigImageUrl() {
		return bigImageUrl;
	}

	public String getStore() {
		return store;
	}

	public String getCategory() {
		return category;
	}

	public float getWeight() {
		return weight;
	}

	public float getPrice() {
		return price;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}

	public void setBigImageUrl(String bigImageUrl) {
		this.bigImageUrl = bigImageUrl;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public static void buildBasicInfo(ResultSet rs, Product product) throws SQLException {
		product.setId(rs.getString("id"));
		product.setName(rs.getString("name"));
		product.setShortDescription(rs.getString("short_description"));
		product.setLongDescription(rs.getString("long_description"));
		product.setSmallImageUrl(rs.getString("small_image_url"));
		product.setBigImageUrl(rs.getString("big_image_url"));
		product.setWeight(Float.valueOf(rs.getString("weight")));
		product.setPrice(Float.valueOf(rs.getString("price")));
	}

	public static void buildExtraInfo(ResultSet rs, Product product) throws SQLException {
		// set store
		String storeId = rs.getString("store_id");
		ResultSet storeSet = ConnectionHandler.getResultSet(
				"SELECT * FROM store WHERE id='" + storeId + "'");
		storeSet.next();
		product.setStore(storeSet.getString("name"));

		// set category
		ResultSet categorySet = ConnectionHandler.getResultSet("SELECT "
				+ "name FROM category where id='"
				+ rs.getString("category_id") + "'");
		categorySet.next();
		product.setCategory(categorySet.getString("name"));
	}

	public static Product buildProduct(ResultSet rs) throws SQLException {
		Product product = new Product();
		buildBasicInfo(rs, product);
		buildExtraInfo(rs, product);
		return product;
	}
	
	public void display() {
		System.out.println("\tProduct information:\n"
				+ "id: " + this.getId() + "\n"
				+ "name " + this.getName() + "\n"
				+ "short description: " + this.getShortDescription() + "\n"
				+ "small image url: " + this.getSmallImageUrl() + "\n"
				+ "big image url: " + this.getBigImageUrl());
	}
}
