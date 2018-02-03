/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.model.basicTypes;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author bohao
 */
public class Address {

	private String id;
	private String addressLineOne;
	private String addressLineTwo;
	private String city;
	private String country;
	private String zipCode;
	private String addressComplement;

	public String getId() {
		return id;
	}

	public String getAddressLineOne() {
		return addressLineOne;
	}

	public String getAddressLineTwo() {
		return addressLineTwo;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getAddressComplement() {
		return addressComplement;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}

	public void setAddressLineTwo(String addressLineTwo) {
		this.addressLineTwo = addressLineTwo;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setAddressComplement(String addressComplement) {
		this.addressComplement = addressComplement;
	}

	public static void buildBasicInfo(ResultSet rs, Address address) throws SQLException {
		address.setId(rs.getString("id"));
		address.setAddressLineOne(rs.getString("address_line_1"));
		address.setAddressLineTwo(rs.getString("address_line_2"));
		address.setCity(rs.getString("city"));
		address.setCountry(rs.getString("country"));
		address.setZipCode(rs.getString("zip_code"));
		address.setAddressComplement(rs.getString("address_complement"));
	}

	public static Address buildAddress(ResultSet rs) throws SQLException {
		Address address = new Address();
		buildBasicInfo(rs, address);
		return address;
	}
	
	@Override
	public String toString() {
		return    "Address line one : " + this.getAddressLineOne() + "\n"
			    + "Address line two : " + this.getAddressLineTwo() + "\n"
				+ "Zip code         : " + this.getZipCode() + "\n"
				+ "City             : " + this.getCity() + "\n"
				+ "Country          : " + this.getCountry() + "\n";
	}
}
