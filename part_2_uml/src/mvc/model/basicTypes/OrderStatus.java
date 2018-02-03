/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model.basicTypes;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import mvc.controller.ConnectionHandler;

/**
 *
 * @author bohao
 */
public class OrderStatus {
	private static final HashMap<String, String> NAME_ID_MAP;
	// static block
	static {
		NAME_ID_MAP = new HashMap();
		ResultSet rs = ConnectionHandler
				.getResultSet("SELECT * FROM order_status");
		try {
			while (rs.next()) {
				String name = rs.getString("name");
				String id = rs.getString("id");
				NAME_ID_MAP.put(name, id);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	// get the id of the first order status
	public static String getDefaultOrderStatusId() {
		Map.Entry<String, String> entry 
				= NAME_ID_MAP.entrySet().iterator().next();
		String key = entry.getValue();
		return key;
	}
	// get the name of the first order status
	public static String getDefaultOrderStatus() {

		Map.Entry<String, String> entry
				= NAME_ID_MAP.entrySet().iterator().next();
		String key = entry.getKey();
		return key;
	}
}
