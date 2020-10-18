package com.jcp.automation.common.utils;

public enum CatalogEnum {
	
	ATTRIBUTE_NAMES("$..products[0]..optionValues[*].name"),
    ATTRIBUTE_VALUES("$..products[0]..optionValues[*].value"),
    ATTRIBUTE_PPID("$..products[0].id"),
    ATTRIBUTE_NAME("$..products[0].name"),
    ATTRIBUTE_PROD_IMAGEURL("$..optionValues[*].productImage.url"),
    ATTRIBUTE_IMAGEURL("$..products[0].images[0].url"),
    ATTRIBUTE_URL("$..links.url"),
    ATTRIBUTE_TYPE("$..products[0].type"),
	ATTRIBUTE_BRANDNAME("$[0].products[0].brand.name"),
	ATTRIBUTE_BRANDURL("$[0].products[0].brand.url");

    private String path;
    CatalogEnum(String value){
      this.path = value;
    }

    public String getPath() {
      return path.toString();
    }
  
}
