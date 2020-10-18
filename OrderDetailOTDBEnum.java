package com.jcp.automation.common.utils;

public enum OrderDetailOTDBEnum {

	
	OTDB_ORDER_DETAIL_ORDERNUMBER("$..historyResults[0].OrderNo"),
	OTDB_ORDER_DETAIL_ORDERDATE("$..historyResults[0].OrderDate"),
	OTDB_ORDER_DETAIL_ORDERTIME("$..historyResults[0].OrderTime"),
	OTDB_ORDER_DETAIL_CHANNEL("$..historyResults[0].SourceSystem"),
	OTDB_ORDER_DETAIL_STATUS("$..historyResults[0].OrderStatus"),
	OTDB_ORDER_DETAIL_BILLING_FIRSTNAME("$..historyResults[0].PersonInfoBillTo.FirstName"),
	OTDB_ORDER_DETAIL_BILLING_LASTNAME("$..historyResults[0].PersonInfoBillTo.LastName"),
	OTDB_ORDER_DETAIL_BILLING_MIDDLENAME("$..historyResults[0].PersonInfoBillTo.MiddleName"),
	OTDB_ORDER_DETAIL_BILLING_ADDRESSLINE1("$..historyResults[0].PersonInfoBillTo.AddressLine1"),
	OTDB_ORDER_DETAIL_BILLING_ADDRESSLINE2("$..historyResults[0].PersonInfoBillTo.AddressLine2"),
	OTDB_ORDER_DETAIL_BILLING_ADDRESSLINE3("$..historyResults[0].PersonInfoBillTo.AddressLine3"),
	OTDB_ORDER_DETAIL_BILLING_COUNTRY("$..historyResults[0].PersonInfoBillTo.Country"),
	OTDB_ORDER_DETAIL_BILLING_CITY("$..historyResults[0].PersonInfoBillTo.City"),
	OTDB_ORDER_DETAIL_BILLING_STATE("$..historyResults[0].PersonInfoBillTo.State"),
	OTDB_ORDER_DETAIL_BILLING_ZIPCODE("$..historyResults[0].PersonInfoBillTo. ZipCode"),
	OTDB_ORDER_DETAIL_PHONE("$..historyResults[0].Extn.DayPhone"),
	OTDB_ORDER_DETAIL_PAYMENT_CREDITCARD("$..historyResults[0].PaymentMethods[*]"),
	OTDB_ORDER_DETAIL_PAYMENT_CREDITCARD_TYPE("$..historyResults[0].PaymentMethods[*].CreditCardType"),
	OTDB_ORDER_DETAIL_PAYMENT_CREDITCARD_NO("$..historyResults[0].PaymentMethods[*].CreditCardNo"),
	OTDB_ORDER_DETAIL_CUSTOMERVID("$..historyResults[0].Extn.CustomerVID"),
	OTDB_ORDER_DETAIL_SVCNO("$..historyResults[0].PaymentMethods[*].SvcNo"),
	OTDB_ORDER_DETAIL_LINE_TAXAMOUNT("$..historyResults[0].OrderLines[*].LineTaxes[0].Tax"),
	OTDB_ORDER_DETAIL_LINE_NOTES_CONTACTUSER("$..historyResults[0].OrderLines[*].Notes[0].ContactUser"),
	OTDB_ORDER_DETAIL_LINE_NOTES_CONTACTREFERENCE("$..historyResults[0].OrderLines[*].Notes[0].ContactReference"),
	OTDB_ORDER_DETAIL_LINE_NOTES_NOTETEXT("$..historyResults[0].OrderLines[*].Notes[0].NoteText"),
	OTDB_ORDER_DETAIL_ADDADDR_ADDRESSTYPE("$..historyResults[0].AdditionalAddresses.AddressType"),
	OTDB_ORDER_DETAIL_ADDADDR_PERSONALINFO("$..historyResults[0].PersonInfo.FirstName"),
	OTDB_ORDER_DETAIL_LINE_SHIPSTATUS("$..historyResults[0].OrderLines[*].ShipStatus"),
	OTDB_ORDER_DETAIL_LINE_FULFILLMENTTYPE("$..historyResults[0].OrderLines[*].FulfillmentType"),
	OTDB_ORDER_DETAIL_LINE_DELIVEREDDATE("$..historyResults[0].OrderLines[*].DeliveredDate"),
	OTDB_ORDER_DETAIL_LINE_EXPECTEDFULFILLMENTTYPE("$..historyResults[0].OrderLines[*].ExpectedFulfillmentDate"),
	OTDB_ORDER_DETAIL_LINE_INVOCIENO("$..historyResults[0].OrderLines[*].InvoiceNo"),
	OTDB_ORDER_DETAIL_LINE_ITEMRESERVATIONSTATUS("$..historyResults[0].OrderLines[*].Extn.ItemReservationStatus"),
	OTDB_ORDER_DETAIL_LINE_ITEMID("$..historyResults[0].OrderLines[*].Item.ItemId"),
	OTDB_ORDER_DETAIL_LINE_ITEMDESC("$..historyResults[0].OrderLines[*].Item.ItemDesc"),
	OTDB_ORDER_DETAIL_LINE_ORDEREDQTY("$..historyResults[0].OrderLines[*].OrderedQty"),
	OTDB_ORDER_DETAIL_LINE_VDATA("$..historyResults[0].OrderLines[*].Vdata"),
	OTDB_ORDER_DETAIL_LINE_CHARGE_NAME("$..historyResults[0].OrderLines[*].LineCharges[*].ChargeName"),
	OTDB_ORDER_DETAIL_LINE_CHARGE_CATEGORY("$..historyResults[0].OrderLines[*].LineCharges[*].ChargeCategory"),
	OTDB_ORDER_DETAIL_LINE_CHARGE_AMOUNT("$..historyResults[0].OrderLines[*].LineCharges[*].ChargeAmount"),
	OTDB_ORDER_DETAIL_LINE_ORDERLINEADJUSTMENT("$..historyResults[0].OrderLines[*].OrderLineAdjustmentList"),
	OTDB_ORDER_DETAIL_LINE_TRACKINGNO("$..historyResults[0].OrderLines[*].ShipmentList[*].Containers[0].TrackingNo");


    private String path;
	OrderDetailOTDBEnum(String value){
      this.path = value;
    }

    public String getPath() {
      return path.toString();
    }
}
