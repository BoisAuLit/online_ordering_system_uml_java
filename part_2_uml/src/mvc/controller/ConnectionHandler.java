/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.controller;

import java.sql.*;

/**
 *
 * @author bohao
 */
public class ConnectionHandler {

	private static final String USERNAME = "root";
	private static final String PASSWORD = "Libohao130237";
	private static Connection connection;
	private static ResultSet resultSet;

	// static block
	static {
		try {
			connection = DriverManager.getConnection("jdbc:mysql:"
					+ "//localhost/grocery_ordering_system"
					+ "?autoReconnect=true&useSSL=false", USERNAME, PASSWORD);
		} catch (Exception e) {
			System.out.println("error: " + e);
		}
	}

	private static Statement getNewStatement() throws SQLException {
		return connection.createStatement();
	}
	
	public static ResultSet getResultSet(String query) {
		try {
			resultSet = getNewStatement().executeQuery(query);
		} catch (Exception e) {
			System.out.println("error executing sql query: " + e);
		}
		return resultSet;
	}
	
	public static void executeUpdate(String query) {
		try {
			getNewStatement().executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	public static void execute(String query) {
		try {
			getNewStatement().execute(query);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	public static boolean confirmUser(String userName, String password,
			String userType) {
		ResultSet rs = getResultSet("SELECT user_name,password,name FROM account"
				+ ",type_account where type_account_id=type_account.id");

		try {
			while (rs.next()) {
				if (rs.getString("user_name").equals(userName)
						&& rs.getString("password").equals(password)
						&& rs.getString("name").equalsIgnoreCase(userType)) {
					return true;
				}
			}
		} catch (Exception e) {
			System.out.println("error confirming user: " + e);
		}
		return false;
	}
	
	// return a prepared statement
	public static PreparedStatement getPreparedStatement(String statement) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(statement);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return ps;
	}
	
	public static void setInt(PreparedStatement ps, int index, int i) {
		try {
			ps.setInt(index, i);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	public static void setFloat(PreparedStatement ps, int index, float f) {
		try {
			ps.setFloat(index, f);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	public static void setString(PreparedStatement ps, int index, String s){
		try {
			ps.setString(index, s);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	public static void setDate(PreparedStatement ps, int index, Date date) {
		try {
			ps.setDate(index, date);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} 
		
	}
	
	public static int executeUpdate(PreparedStatement ps) {
		int result = 0;
		try {
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return result;
	}
	
	public static ResultSet executeQuery(PreparedStatement ps) {
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
		return rs;
	}
	
	public static boolean execute(PreparedStatement ps) {
		boolean result = false;
		try {
			result = ps.execute();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return result;
	}
}
