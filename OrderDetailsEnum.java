package com.jcp.automation.common.utils;

public enum OrderDetailsEnum {

	

    CREDIT_CARD_TYPE_LIST("$.billing.creditCards[*].type"),
    CREDIT_CARD_AMOUNT_LIST("$.billing.creditCards[*].amount"),
	ORDER_ADJUSTMENTS("$.adjustments[*].amount"),
	ORDER_LINE_ADJUSTMENTS("$.lines[*].adjustments[*].amount");

    private String path;
    OrderDetailsEnum(String value){
      this.path = value;
    }

    public String getPath() {
      return path.toString();
    }
  
}
