package com.jcp.automation.common.utils;

public enum OrderListEnum {

	    CORE_STATUS("$.orders[0].coreStatus"),
		DISPLAY_STATUS("$.orders[0].displayStatus");
	
	    private String path;
	    OrderListEnum(String value){
	      this.path = value;
	    }

	    public String getPath() {
	      return path.toString();
	    }
	  


}
