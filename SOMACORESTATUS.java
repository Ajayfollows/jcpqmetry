package com.jcp.automation.common.utils;

public enum SOMACORESTATUS {

	SOMA_1200("In Process"),
	SOMA_1300("Checking Inventory"),
	SOMA_1310("In Process"),
	SOMA_1400("In Process"),
	SOMA_1500("In Process"),
	SOMA_2060("Transfer in Process"),
	SOMA_2160("Transfer in Process"),
	SOMA_3200("In Process"),
	SOMA_3350("Pending fulfillment"),
	SOMA_335005("Released to Fulfillment"),
	SOMA_335010("Pending fulfillment"),
	SOMA_3350100("Pending fulfillment"),
	SOMA_335020("Released to Fulfillment"),
	SOMA_3700("Shipped"),
	SOMA_370001("Ready for Customer Pick"),
	SOMA_3700300("Picked by Customer"),
	SOMA_3700500("Return Created"),
	SOMA_9000("Cancel"),
	SOMA_3700100("Shipped to Store"),
	SOMA_37009000("Post Invoice Cancel");
	
	 private String path;
	 SOMACORESTATUS(String value){
	    this.path = value;
	 }

	 public String getPath() {
	    return path.toString();
	 }
}
