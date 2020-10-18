package com.jcp.automation.common.beans.databeans;

import com.qmetry.qaf.automation.data.BaseDataBean;

public class ShippingAddressBean extends BaseDataBean {

	/**
	 * this variables names are mapped with API so please dont change
	 */
	private String firstName;
	private String lastName;
	private String addressLineOne;
	private String zip;
	private String city;
	private String state;
	private String country;
	private String addressLineTwo;
	private String phone;
	private String phoneTwo;
	private String email;

	private String militaryType;
	private String militaryTypeValue;
	private String militaryStreetAddress1;
	private String militaryZipCode;
	private String countryValue;
	private String militaryState;
	private String militaryStateValue;
	private String militaryEmail;

	public String getAddressLine1() {
		return addressLineOne;
	}
	public String getAddressLine2() {
		return addressLineTwo;
	}
	public String getCity() {
		return city;
	}
	public String getPhoneTwo() {
		return phoneTwo;
	}
	public String getCountry() {
		return country;
	}
	public String getCountryValue() {
		return countryValue;
	}
	public String getEmail() {
		return email;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getMilitaryEmail() {
		return militaryEmail;
	}
	public String getMilitaryState() {
		return militaryState;
	}
	public String getMilitaryStateValue() {
		return militaryStateValue;
	}
	public String getMilitaryStreetAddress1() {
		return militaryStreetAddress1;
	}
	public String getMilitaryType() {
		return militaryType;
	}
	public String getMilitaryTypeValue() {
		return militaryTypeValue;
	}
	public String getMilitaryZipCode() {
		return militaryZipCode;
	}
	public String getPhone() {
		return phone;
	}
	public String getState() {
		return state;
	}
	public String getZip() {
		return zip;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLineOne = addressLine1;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLineTwo = addressLine2;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public void setCountryValue(String countryValue) {
		this.countryValue = countryValue;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setMilitaryEmail(String militaryEmail) {
		this.militaryEmail = militaryEmail;
	}
	public void setMilitaryState(String militaryState) {
		this.militaryState = militaryState;
	}
	public void setMilitaryStateValue(String militaryStateValue) {
		this.militaryStateValue = militaryStateValue;
	}
	public void setMilitaryStreetAddress1(String militaryStreetAddress1) {
		this.militaryStreetAddress1 = militaryStreetAddress1;
	}
	public void setMilitaryType(String militaryType) {
		this.militaryType = militaryType;
	}
	public void setMilitaryTypeValue(String militaryTypeValue) {
		this.militaryTypeValue = militaryTypeValue;
	}
	public void setMilitaryZipCode(String militaryZipCode) {
		this.militaryZipCode = militaryZipCode;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
}
