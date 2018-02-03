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
import javax.swing.JOptionPane;
import mvc.controller.ConnectionHandler;

/**
 *
 * @author bohao
 */
public class Cart {

	private String id;
	private float weight;
	private float priceOfProducts;
	private int numberOfItems;
	private ArrayList items;
	
	private Cart() {
	}
	
	private static class SingletonHolder {
		private static final Cart CART = new Cart();
	}

	public static Cart getInstance() {
		return SingletonHolder.CART;
	}
	
	private static Cart getNewInstance() {
		reset();
		return getInstance();
	}
	
	private static void reset() {
		Cart cart = Cart.getInstance();
		cart.setId(null);
		cart.setWeight(.0f);
		cart.setPriceOfProducts(.0f);
		cart.setNumberOfItems(0);
		cart.setItems(null);
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

	public int getNumberOfItems() {
		return numberOfItems;
	}

	public ArrayList getItems() {
		return items;
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

	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
	}

	public void setItems(ArrayList items) {
		this.items = items;
	}

	public static void buildBasicInfo(ResultSet rs, Cart cart)
			throws SQLException {
		// 1. Set id, weight, priceOfProducts and numberOfItems
		//    for the Cart object
		cart.setId(rs.getString("id"));
		cart.setWeight(Float.valueOf(rs.getString("weight")));
		cart.setPriceOfProducts(Float.valueOf(rs.getString(
				"price_of_products")));
		cart.setNumberOfItems(Integer.valueOf(rs.getString(
				"number_of_items")));
	}

	public static void buildExtraInfo(ResultSet rs, Cart cart)
			throws SQLException {
		// 2. Set all the items in the Cart object
		ResultSet itemInCartSet = ConnectionHandler.getResultSet(
				"SELECT * FROM cart_product WHERE cart_id='" 
						+ cart.getId() + "'");
		ArrayList<ItemInCart> items = new ArrayList();
		while (itemInCartSet.next()) {
			items.add(ItemInCart.buildItemInCart(itemInCartSet));
		}
		cart.setItems(items);
	}

	public static Cart buildCart(ResultSet rs) throws SQLException {
		Cart cart = Cart.getNewInstance();
		buildBasicInfo(rs, cart);
		buildExtraInfo(rs, cart);
		return cart;
	}
	
	@Override
	public String toString() {
		String result = "Cart:\n"
				+ "cart id: " + id + "\n"
				+ "weight: " + weight + "\n"
				+ "price of products: " + priceOfProducts + "\n"
				+ "number of items: " + numberOfItems + "\n\n";
		for (Object o : items)
			result += (ItemInCart)o + "\n";
		return result;
	}
	
	public void deleteAnItem(ItemInCart item) {
		items.remove(item);
		weight -= item.getWeight();
		priceOfProducts -= item.getPriceOfProducts();
		numberOfItems -= 1;
		
		PreparedStatement ps;
		String query;
		String itemInCartId = item.getId();
		Customer customer = Customer.getInstance();
		String cartId = getId();
		
		// delete the ItemInOrder record in the database
		query = "DELETE FROM cart_product WHERE id = ?";
		ps = ConnectionHandler.getPreparedStatement(query);
		ConnectionHandler.setInt(ps, 1, Integer.valueOf(itemInCartId));
		ConnectionHandler.executeUpdate(ps);
		
		// don't forget to set null to the reference to the Cart Object
		// in the customer Object

		if (numberOfItems == 0) {
			// if now the cart is empty, delete the record of the Cart object
			// in the database and set null to the reference to the Cart Obeject
			// in the customer Object
			query = "DELETE FROM cart WHERE id = ?";
			ps = ConnectionHandler.getPreparedStatement(query);
			ConnectionHandler.setInt(ps, 1, Integer.valueOf(cartId));
			ConnectionHandler.executeUpdate(ps);
			String customerId = Customer.getInstance().getId();

			// now the customer doesn't have shopping list anymore
			customer.setCart(null);
			setItems(null);
			
			// and right now the customer doesn't have shopping list(cart)
			// anymore, so we update the record concerning the Customer
			// object in the database;
			query = "UPDATE customer SET has_shopping_list = 'N' "
					+ "WHERE id = ?";
			ps = ConnectionHandler.getPreparedStatement(query);
			ConnectionHandler.setInt(ps, 1, Integer.valueOf(customerId));
			ConnectionHandler.executeUpdate(ps);
			
		} else {
			// if the cart is not empty, we just need to refresh the object
			refresh();
		}
	}
	

	public void refresh() {
		float newPrice = .0f;
		float newWeight = .0f;
		for (Object obj : items) {
			newPrice += ((ItemInCart)obj).getPriceOfProducts();
			newWeight += ((ItemInCart)obj).getWeight();
		}
		
		int cartId = Integer.valueOf(id);
		
		String query = "UPDATE cart SET weight = ?, price_of_products = ?, number_of_items = ? "
				+ "WHERE id = ?";
		PreparedStatement ps = ConnectionHandler.getPreparedStatement(query);
		ConnectionHandler.setFloat(ps, 1, newWeight);
		ConnectionHandler.setFloat(ps, 2, newPrice);
		ConnectionHandler.setInt(ps, 3, items.size());
		ConnectionHandler.setInt(ps, 4, cartId);
		ConnectionHandler.executeUpdate(ps);
	}
		
	public ItemInCart addAnItem(Product product) {
		if (items == null) {
			// create item in cart
			//************************************************
			// we haven't set the id of the ItemInCart object yet !
			// we haven't set the id of the cart Object yet
			//************************************************
			ItemInCart item = new ItemInCart();
			item.build(product);
			items = new ArrayList();
			items.add(item);
			numberOfItems = 1;
			weight = item.getWeight();
			priceOfProducts = item.getPriceOfProducts();
			
			PreparedStatement ps;
			String query;
			int customerId = Integer.valueOf(Customer.getInstance().getId());
			ResultSet rs;
			String cartId;
			String productId;
			String itemInCartId = null;
			productId = product.getId();
			
			// insert a record into the cart table in the databse
			query = "INSERT INTO cart VALUES(NULL, ?, ?, ?, ?, ?)";
			ps = ConnectionHandler.getPreparedStatement(query);
			ConnectionHandler.setInt(ps, 1, customerId);
			ConnectionHandler.setFloat(ps, 2, priceOfProducts);
			ConnectionHandler.setInt(ps, 3, 1);
			ConnectionHandler.setFloat(ps, 4, weight);
			ConnectionHandler.setString(ps, 5, "Y");
			ConnectionHandler.executeUpdate(ps);
			
			// get the id of the cart that is generated by phpmyadmin just now
			query = "SELECT * FROM cart WHERE customer_id = ?";
			ps = ConnectionHandler.getPreparedStatement(query);
			ConnectionHandler.setInt(ps, 1, customerId);
			rs = ConnectionHandler.executeQuery(ps);
			try {
				rs.next();
				cartId = rs.getString("id");
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
			// set the id of the cart object
			this.setId(cartId);
			
			
			// the customer now has a shopping list
			query = "UPDATE customer SET has_shopping_list = 'Y' WHERE id = ?";
			ps = ConnectionHandler.getPreparedStatement(query);
			ConnectionHandler.setInt(ps, 1, customerId);
			ConnectionHandler.executeUpdate(ps);
			
			// insert a record into cart_prodcut table in the database
			query = "INSERT INTO cart_product VALUES(NULL, ?, ?, ?, ?, ?, ?)";
			ps = ConnectionHandler.getPreparedStatement(query);
			ConnectionHandler.setInt(ps, 1, Integer.valueOf((cartId)));
			ConnectionHandler.setInt(ps, 2, Integer.valueOf(productId));
			ConnectionHandler.setInt(ps, 3, 1);
			ConnectionHandler.setFloat(ps, 4, weight);
			ConnectionHandler.setFloat(ps, 5, priceOfProducts);
			ConnectionHandler.setString(ps, 6, "Y");
			ConnectionHandler.executeUpdate(ps);
			
			// get the id of the ItemInCart object that we inserted to the
			// database just now
			query = "SELECT * FROM cart_product WHERE product_id = ?";
			ps = ConnectionHandler.getPreparedStatement(query);
			ConnectionHandler.setInt(ps, 1, Integer.valueOf(productId));
			rs = ConnectionHandler.executeQuery(ps);
			try{
				rs.next();
				itemInCartId = rs.getString("id");
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
			item.setId(itemInCartId);
			
			return item;
		} else {
			// make sure of no duplicate of products in the shopping list
			for (Object item : items) {
				if (((ItemInCart) item).getProduct()
						.getId().equals(product.getId())) {
					JOptionPane.showMessageDialog(null, 
							"this product alreay exists in shopping list!",
							"Error", JOptionPane.ERROR_MESSAGE);
					return null;
				}
			}
			
			Customer customer = Customer.getInstance();
			Cart cart = customer.getCart();
			
			// create item in cart
			ItemInCart item = new ItemInCart();
			item.build(product);
			items.add(item);
			numberOfItems += 1;
			weight += item.getWeight();
			priceOfProducts += item.getPriceOfProducts();
			int cartId = Integer.valueOf(cart.getId());
			int productId = Integer.valueOf(product.getId());
			
			String query;
			PreparedStatement ps;
			// insert a record into cart_prodcut table in the database
			query = "INSERT INTO cart_product VALUES(NULL, ?, ?, ?, ?, ?, ?)";
			ps = ConnectionHandler.getPreparedStatement(query);
			ConnectionHandler.setInt(ps, 1, cartId);
			ConnectionHandler.setInt(ps, 2, productId);
			ConnectionHandler.setInt(ps, 3, 1);
			ConnectionHandler.setFloat(ps, 4, item.getWeight());
			ConnectionHandler.setFloat(ps, 5, item.getPriceOfProducts());
			ConnectionHandler.setString(ps, 6, "Y");
			ConnectionHandler.executeUpdate(ps);
			
			// set the id of the ItemInCart object
			query = "SELECT * FROM cart_product WHERE product_id = ?";
			ps = ConnectionHandler.getPreparedStatement(query);
			ConnectionHandler.setInt(ps, 1, productId);
			ResultSet rs = ConnectionHandler.executeQuery(ps);
			String itemInCartId = null;
			try{
				rs.next();
				itemInCartId = rs.getString("id");
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
			item.setId(itemInCartId);
			
			refresh();		
			return item;
		}
	}
	
	public static void removeRecordInDatabase() {
		Cart cart = Cart.getInstance();
		
		// remove all the ItemInCart object in the data base
		ArrayList<ItemInCart> itemsInCart = cart.getItems();
		for (ItemInCart itemInCart : itemsInCart)
			itemInCart.removeRecordInDatabase();
		
		String query;
		PreparedStatement ps;
		int cartId = Integer.valueOf(cart.getId());
		
		query = "DELETE FROM cart WHERE id = ?";
		ps = ConnectionHandler.getPreparedStatement(query);
		ConnectionHandler.setInt(ps, 1, cartId);
		ConnectionHandler.executeUpdate(ps);
		
		// the customer doesn't have shopping list anymore, so we set
		// the flag has_shopping_list of the customer to 'N' (which
		// means no)
		query = "UPDATE customer SET has_shopping_list = 'N'";
		ps = ConnectionHandler.getPreparedStatement(query);
		ConnectionHandler.executeUpdate(ps);
		
		//**********************************************************************
		// super mega important
		// don't forget to set the flag have shopping list to 'N'
		// because the customer doesn't have shopping lists anymore
		//**********************************************************************
	}
}
