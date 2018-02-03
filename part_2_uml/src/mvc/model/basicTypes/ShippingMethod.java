package mvc.model.basicTypes;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import mvc.controller.ConnectionHandler;

public class ShippingMethod {

	private static final HashMap<String, Pair<Float, Integer>> MAP;
	private static final HashMap<String, String> MAP_2;

	private static final class Pair<L, R> {
		private final L left;
		private final R right;
		public Pair(L left, R right) {
			this.left = left;
			this.right = right;
		}
		public L getLeft() {
			return left;
		}
		public R getRight() {
			return right;
		}
		@Override
		public int hashCode() {
			return left.hashCode() ^ right.hashCode();
		}
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Pair)) {
				return false;
			}
			@SuppressWarnings("unchecked")
			Pair<L, R> pairo = (Pair<L, R>) o;
			return this.left.equals(pairo.getLeft()) && this.right.equals(pairo.getRight());
		}
	}
	
	// static block
	static {
		MAP = new HashMap();
		MAP_2 = new HashMap();
		ResultSet rs = ConnectionHandler
				.getResultSet("SELECT * FROM shipping_method");
		try {
			while (rs.next()) {
				String name = rs.getString("name");
				float percentage = rs.getFloat("percentage");
				int shipping_days = rs.getInt("shipping_days");
				String id = rs.getString("id");
				MAP.put(name, new Pair(percentage, shipping_days));
				MAP_2.put(name, id);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	} // static block ends

	// every shipping method has a percentage, we use this percentage
	// to compute the shipping cost of the products of the shopping list
	public static float getPercentage(String shippingMethod) {
		return MAP.get(shippingMethod).getLeft();
	}
	
	// every shipping method has a shipping limit, for example, if the
	// shipping limit is equal to 2, then the product can be deleverd withi
	// 2 days;
	public static int getShippingDays(String shippingMethod) {
		return MAP.get(shippingMethod).getRight();
	}

	public static String getDefaultShippingMethod() {
		Map.Entry<String, Pair<Float, Integer>> entry
				= MAP.entrySet().iterator().next();
		String key = entry.getKey();
		return key;
	}
	
	public static String getShippingMethodId(String shippingMethod) {
		return MAP_2.get(shippingMethod);
	}
	
	public static String getDefaultShippingMethodId() {
		Map.Entry<String, String> entry = MAP_2.entrySet().iterator().next();
		String key = entry.getValue();
		return key;
	}
}
