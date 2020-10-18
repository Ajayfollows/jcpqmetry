package com.jcp.automation.common.utils;

/**
 * Author gkarabut
 * since 9/11/17.
 */
public enum JsonPathEnum {
    BASE_TYPE("[0].type"),
    TYPE("$.type"),
    BASE_NAME("[0].name"),
    BASE_DESCRIPTION("[0].desc"),
    BASE_PPURL("[0].ppUrl"),
    BASE_IMAGES("[0].images"),
    BASE_ITEMS("[0].items"),
    ITEMS_PATH("$.items"),
    PRICING_LOTS("[0].pricing.lots.data"),
    REVIEWS("[0].valuation.reviews"),
    VALUATION("[0].valuation"),
    MAXQTY("$.maxQuantity"),
    OPTIONS("$.options"),
    ID("$.id"),
    PRODUCT_IMAGE("$.productImage.url"),
    VALUE("$.value"),
    NAME("$.name"),
    ISDEFAULT("$.isDefault"),
    BASE_LOTS("[0].lots"),
    MANADVERTISED("[0].pricing.root.manufacturerAdvertised"),
    URL("$.url"),
    RESTRICTIONS("[0].restrictions"),
    WARRANTY_PRICE("$.price"),
    WARRANTY_ID("$.itemId"),
    WARRANTY_NAME("$.name"),
    WARRANTY_SELECTED("$.selected"),
    BASE_PROTECTION_PLAN("$.warranties"),
    BASE_DIMENSIONS("[0].dimensions"),
    BASE_PRICING_MARKETING("[0].pricing.root.marketingLabel"),
    REQUEST_ITEMID("$..id"),
    ITEMID("$.itemId"),
    REQUEST_PRODUCT_ID("$.product.id"),
    PRODUCT_ID("$.product.id"),
    RESPONSE_ITEM_ID("[0].id"),
    REQUEST_MOST_WANTED("$.mostWanted"),
    REQUEST_QTY("$.quantity"),
    REQUEST_WARRANTY_ID("$.warrantyId"),
    RESPONSE_ACCOUNT_ID("$.account_id"),
    RESPONSE_ITEMID_SYSTEM_PATTERN("$.items[?(@.itemId==%s)]");
//    RESPONSE_ITEMID_SYSTEM_PATTERN("$.items[?(@.itemId==%s)].id");
//    RESPONSE_ITEMID_SYSTEM("$.items.[0].id");



    private String path;
    JsonPathEnum(String value){
      this.path = value;
    }

    public String getPath() {
      return path.toString();
    }
  }

