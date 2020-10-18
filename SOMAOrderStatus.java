package com.jcp.automation.common.utils;

import static com.jcp.automation.common.rest.RestClient.getitemdetails;
import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jcp.automation.api.impulse.orderdetaildto.Product;
import com.jcp.automation.api.impulse.db.SqlQueiresSOMA;
import com.jcp.automation.api.impulse.orderdetaildto.Adjustment;
import com.jcp.automation.api.impulse.orderdetaildto.Charge;
import com.jcp.automation.api.impulse.orderdetaildto.Delivery;
import com.jcp.automation.api.impulse.orderdetaildto.Delivery.DeliveryMethod;
import com.jcp.automation.api.impulse.orderdetaildto.Note;
import com.jcp.automation.api.impulse.orderdetaildto.OmniMessages;
import com.jcp.automation.api.impulse.orderdetaildto.OrderDetail;
import com.jcp.automation.api.impulse.orderdetaildto.OrderHold;
import com.jcp.automation.api.impulse.orderdetaildto.OrderLine;
import com.jcp.automation.api.impulse.orderdetaildto.OrderLine.FulfillmentType;
import com.jcp.automation.api.impulse.steps.WebServiceTestSteps;
import com.jcp.transformer.automation.util.Reporter;
import com.jcp.transformer.automation.util.Validator;

public class SOMAOrderStatus extends WebServiceTestSteps {

	public static HashMap<String, String> headers = new HashMap<>();
	public static final String CATALOG_ENDPOINT = "api.catalog.url";
	public static final String CLIENT_ORIGIN = "x-client-origin";

	public static String getCoreStatus(String key) {

		switch (key) {
		case "1100":
			return "Created";
		case "1200":
			return "In Process";
		case "1300":
			return "Checking Inventory";
		case "1310":
			return "In Process";
		case "1400":
			return "In Process";
		case "1500":
			return "In Process";
		case "2060":
			return "Transfer in Process";
		case "2100":
			return "Transfer in Process";
		case "2160":
			return "Transfer in Process";
		case "3200":
			return "In Process";
		case "3350":
			return "Pending fulfillment";
		case "3350.05":
			return "Pending fulfillment";
		case "3350.10":
			return "Pending fulfillment";
		case "3350.100":
			return "Released to Fulfillment";
		case "3350.20":
			return "Released to Fulfillment";
		case "3700":
			return "Shipped";
		case "3700.01":
			return "Return Created";
		case "3700.300":
			return "Ready for Customer Pick";
		case "3700.500":
			return "Picked by Customer";
		case "9000":
			return "Cancel";
		case "3700.100":
			return "Shipped to Store";
		case "3700.9000":
			return "Post Invoice Cancel";
		}
		return key;
	}

	public static String getDisplayStatus(String key) {

		switch (key) {
		case "1100":
			return "Created";
		case "1200":
			return "In Process";
		case "1300":
			return "Backordered";
		case "1310":
			return "In Process";
		case "1400":
			return "In Process";
		case "1500":
			return "In Process";
		case "2060":
			return "Transfer in Process";
		case "2100":
			return "Transfer in Process";
		case "2160":
			return "Transfer in Process";
		case "3200":
			return "In Process";
		case "3350":
			return "Pending fulfillment";
		case "3350.05":
			return "Pending fulfillment";
		case "3350.10":
			return "Picked";
		case "3350.100":
			return "Released to Fulfillment";
		case "3350.20":
			return "Packed";
		case "3700":
			return "Shipped";
		case "3700.01":
			return "Return Created";
		case "3700.300":
			return "Ready for Customer Pick";
		case "3700.500":
			return "Picked by Customer";
		case "9000":
			return "Cancelled";
		case "3700.100":
			return "Shipped to Store";
		case "3700.9000":
			return "Post Invoice Cancel";
		}
		return key;
	}

	public static String getSOMACoreStatus(String key) {

		switch (key) {
		case "Created":
			return "1100";
		case "Checking Inventory":
			return "1300";
		case "In Process":
			return "1500";
		case "Transfer in Process":
			return "2060";
		case "Pending fulfillment":
			return "3350.10";
		case "Released to Fulfillment":
			return "3350.100";
		case "Shipped":
			return "3700";
		case "Return Created":
			return "3700.01";
		case "Ready for Customer Pick":
			return "3700.300";
		case "Picked by Customer":
			return "3700.500";
		case "Cancel":
			return "9000";
		case "Shipped to Store":
			return "3700.100";
		case "Post Invoice Cancel":
			return "3700.9000";
		}
		return key;
	}

	public static String getSOMADisplayStatus(String key) {

		switch (key) {
		case "Created":
			return "1100";
		case "Checking Inventory":
			return "1300";
		case "Backordered":
			return "1300";
		case "In Process":
			return "1500";
		case "Transfer in Process":
			return "2060";
		case "Pending fulfillment":
			return "3350";
		case "Picked":
			return "3350.10";
		case "Packed":
			return "3350.20";
		case "Released to Fulfillment":
			return "3350.100";
		case "Shipped":
			return "3700";
		case "Return Created":
			return "3700.01";
		case "Ready for Customer Pick":
			return "3700.300";
		case "Picked by Customer":
			return "3700.500";
		case "Cancelled":
			return "9000";
		case "Shipped to Store":
			return "3700.100";
		case "Post Invoice Cancel":
			return "3700.9000";
		}
		return key;
	}

	@SuppressWarnings("static-access")
	public static HashMap<String, Set<String>> getAllowedDetailActions(OrderDetail orderDetail,
			ArrayList<HashMap<String, String>> statusMapping, String usertype) throws Throwable {

		HashMap<String, Set<String>> allowedActions = new HashMap<String, Set<String>>();
		Set<String> headerActions = new HashSet<String>();

		String minKey = "";
		String maxKey = "";
		// String Adjustmentcode = "";
		String Invoiceno = "";
		String shipment_invoiceno = "";
		String deliverymtd = "";
		Charge charge = new Charge();

		for (HashMap<String, String> statusMap : statusMapping) {

			String key = statusMap.get("status");
			if (minKey.compareToIgnoreCase(key) > 0) {
				minKey = key;
			}
			if (maxKey.compareToIgnoreCase(key) < 0) {
				maxKey = key;
			}
		}
		for (HashMap<String, String> statusMap : statusMapping) {

			Set<String> lineActions = new HashSet<String>();

			String Adjustmentcode;
			int AdjustmentQty = 0;

			BigDecimal tax = new BigDecimal("0.00");
			BigDecimal shipping = new BigDecimal("0.00");
			BigDecimal adjAmount = new BigDecimal("0.00");
			BigDecimal others = new BigDecimal("0.00");
			BigDecimal returnAmount = new BigDecimal("0.00");

			List<OrderLine> oslList = orderDetail.getLines();
			for (OrderLine orderLine : oslList) {
				System.out.println(orderLine.getId());
				System.out.println(orderLine.getCoreStatus());
				System.out.println(orderLine.getDisplayStatus());

				List<Note> notes = orderDetail.getNotes();
				int notesize = notes.size();
				if (notesize < 4) {
					headerActions.add("ADD_NOTES");
				}

				if (orderLine.getId().equalsIgnoreCase(statusMap.get("lineid").trim())) {

					if (!orderLine.getAdjustments().isEmpty()) {

						List<Adjustment> Adj = orderLine.getAdjustments();
						for (Adjustment adjustment : Adj) {

							Adjustmentcode = adjustment.getCode();
							Invoiceno = adjustment.getInvoiceNo();
							String AdjQty = adjustment.getQuantity().trim();
							AdjustmentQty = Integer.parseInt(AdjQty);
							if (Adjustmentcode.contains("E01")) {

								tax = adjustment.getTax();

							}
							if (Adjustmentcode.contains("E01")) {

								shipping = adjustment.getShipping();

							}
							String[] codes = { "G01", "G02", "G05", "G06", "P03", "H19", "G04", "G03", "H02", "C19",
									"H13", "H28" };
							if (Arrays.asList(codes).contains(Adjustmentcode)) {

								others = adjustment.getTotal();

							}
							if (Adjustmentcode.contains("A01") || Adjustmentcode.contains("A05")
									|| Adjustmentcode.contains("CPOS")) {

								returnAmount = adjustment.getTotal();
							}

						}

						adjAmount = adjAmount.add(returnAmount).add(others).add(shipping).add(tax);
					}

					Product prod = orderLine.getProduct();
					System.out.println(prod);
					String itemid = prod.getId();
					String prodType = "";
					String pName = prod.getName();
					System.out.println(prodType);
					String status = orderLine.getDisplayStatus().trim();
					String key = getSOMADisplayStatus(status);

					headers.clear();
					setHeaders("Associate");
					getitemdetails(String.format(getBundle().getString(CATALOG_ENDPOINT), itemid), headers);
					Configuration conf = Configuration.defaultConfiguration()
							   .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
					try {
						prodType = JsonPath.using(conf).parse(base.getResponse().getMessageBody()).read( "$.[0].products.[0].type");
					} catch (Exception e) {
						prodType= "REGULAR";
						e.printStackTrace();
					}

					if (status.equalsIgnoreCase("In Process") && statusMap.get("status").equalsIgnoreCase("3200")) {
						key = "3200";
					}

					Double ikey = Double.parseDouble(key);
					String holdType = statusMap.get("holdtype");
					String isFacShipped = statusMap.get("isfactshipped");
					String fulfillmentType = statusMap.get("fulfillmentType");
					String orderQty = statusMap.get("orderedQty").trim();
					int iorderqty = Integer.parseInt(orderQty);
					String totamnt = statusMap.get("linetotal").trim();
					String prime = statusMap.get("lineid").trim();

					String InvoiceStatusDesc = statusMap.get("InvoiceStatusDesc");
					BigDecimal linetotal = new BigDecimal(totamnt);
					int sum = linetotal.compareTo(adjAmount);

					String getcreatets = statusMap.get("createDate").substring(0, 10);
					Date createts = new SimpleDateFormat("yyyy-MM-dd").parse(getcreatets);
					Date CopyOrder = new Date();
					Date daysCopyOrder = new DateTime(CopyOrder).minusDays(365).toDate();
					Date ResendEmail = new Date();
					Date daysResendEmail = new DateTime(ResendEmail).minusDays(45).toDate();
					Date ReOrder = new Date();
					Date daysReOrder = new DateTime(ReOrder).minusDays(30).toDate();

					if (daysReOrder.before(createts)) {
						headerActions.add("PRINT_ORDER");
						headerActions.add("VIEW_ORDER");
						headerActions.add("COPY_ORDER");
						headerActions.add("RE_ORDER");
						headerActions.add("RESEND_CONFIRMATION_EMAIL");

					} else if (daysResendEmail.before(createts) && daysReOrder.after(createts)
							|| daysReOrder.equals(createts)) {
						headerActions.add("PRINT_ORDER");
						headerActions.add("VIEW_ORDER");
						headerActions.add("COPY_ORDER");
						headerActions.add("RESEND_CONFIRMATION_EMAIL");

					} else if (daysCopyOrder.before(createts) && daysResendEmail.after(createts)
							|| daysResendEmail.equals(createts)) {
						headerActions.add("PRINT_ORDER");
						headerActions.add("VIEW_ORDER");
						headerActions.add("COPY_ORDER");
						// headerActions.add("RESEND_CONFIRMATION_EMAIL");

					} else if (daysCopyOrder.after(createts) || daysCopyOrder.equals(createts)) {
						headerActions.add("PRINT_ORDER");
						headerActions.add("VIEW_ORDER");
					}

					if (ikey >= 3350 && ikey != 9000) {
						headerActions.add("FRAUD_INTERCEPT");
					}

					if ((ikey <= 1500 || key.equalsIgnoreCase("3200"))
							&& !statusMap.get("kitHeader").equalsIgnoreCase("Y")) {
						lineActions.add("CANCEL");
						headerActions.add("CANCEL");

					}

					if (key.equalsIgnoreCase("2060") || key.equalsIgnoreCase("2160")) {
						lineActions.add("CANCEL");
						headerActions.add("CANCEL");

					}

					for (OrderHold Hold : orderDetail.getHoldTypes()) {
						if (Hold.getType().equalsIgnoreCase("PAYMENT_RTRYERR_HOLD")
								&& Hold.getStatus().equalsIgnoreCase("Active")) {
							headerActions.add("PAYMENT_ERROR_RETRY");
						}
					}

					if (holdType.equalsIgnoreCase("Y") && key.equalsIgnoreCase("3200")
							&& usertype.equalsIgnoreCase("CAM")) {
						headerActions.add("CUSTOMER_CARE_PAYMENT");
						headerActions.add("CANCEL");
						lineActions.add("CANCEL");
					}
					for (OmniMessages omni : orderDetail.getomnimessages()) {
						if (omni.getMessageText().contains("REWARDS REDEMPTION FAILED")) {
							for (OrderHold Hold : orderDetail.getHoldTypes()) {
								if (Hold.getType().equalsIgnoreCase("PAYMENT_ERROR_HOLD")
										&& Hold.getStatus().equalsIgnoreCase("Active")) {
									headerActions.add("CC_REWARD_REDEMPTION_FAIL");

								}
							}
						} else {
							for (OrderHold Hold : orderDetail.getHoldTypes()) {
								if ((Hold.getType().equalsIgnoreCase("PAYMENT_ERROR_HOLD")
										|| Hold.getType().equalsIgnoreCase("PAYMENT_ER_REAU_HOLD"))
										&& Hold.getStatus().equalsIgnoreCase("Active")) {
									headerActions.add("CHANGE_PAYMENT");
								}
							}
						}
					}

					if (headerActions.contains("CC_REWARD_REDEMPTION_FAIL")) {
						headerActions.remove("CHANGE_PAYMENT");
					}

					for (OrderLine orderLine1 : oslList) {
						String itemfulfillment = orderLine1.getFulfillmentType().toString();
						if (itemfulfillment.equalsIgnoreCase("BOPUS")) {
							headerActions.remove("COPY_ORDER");
							headerActions.remove("RE_ORDER");
						}
					}

					int primeline = 0;
					for (OrderLine orderLine1 : oslList) {
						int primeline1 = Integer.parseInt(orderLine1.getId());
						if (primeline1 > primeline) {
							primeline = primeline1;
						}
					}
					Product p = new Product();
					if (primeline == 1 && pName.equals("E47 Restock Fee")) {
						headerActions.remove("COPY_ORDER");
						headerActions.remove("RE_ORDER");
					}
					shipment_invoiceno = orderLine.getShipmentReference();
					List<Delivery> orderdelivery = orderDetail.getDelivery();
					for(Delivery delivery:orderdelivery){
						 deliverymtd = delivery.getMethod().toString();
					}
					if (ikey >= 3700 && !key.equalsIgnoreCase("3700.9000") && !key.equalsIgnoreCase("3700.100")
							&& ikey != 9000 && !statusMap.get("kitHeader").equalsIgnoreCase("Y")
							&& !(shipment_invoiceno.equalsIgnoreCase("1") || shipment_invoiceno.equalsIgnoreCase("") || deliverymtd.equalsIgnoreCase("SHIPTOSTORE"))
							&& !(prodType.equalsIgnoreCase("GIFT_CARD") || prodType.equalsIgnoreCase("SERVICE")
									|| prodType.startsWith("LARGEAPPLIANCE") || prodType.equalsIgnoreCase("RECYCLE"))
							&& (fulfillmentType.equalsIgnoreCase("TRUCK")
									|| fulfillmentType.equalsIgnoreCase("FACTORYSHIP")
									|| fulfillmentType.startsWith("PARCEL"))) {
						if (orderLine.getAdjustments() != null || orderLine.getAdjustments().size() > 0) {
							if (iorderqty == AdjustmentQty && sum == 0) {

								lineActions.add("ADJUSTMENT_HISTORY");
								headerActions.add("ADJUSTMENT_HISTORY");
								lineActions.add("RETURNS");
								lineActions.add("EXCHANGES");
								lineActions.add("CHARGE_BACK");
								headerActions.add("RETURNS");
								headerActions.add("EXCHANGES");
								headerActions.add("CHARGE_BACK");

							} else if (iorderqty == AdjustmentQty && sum != 0) {

								lineActions.add("ADJUSTMENTS");
								lineActions.add("RETURNS");
								lineActions.add("EXCHANGES");
								lineActions.add("CHARGE_BACK");
								headerActions.add("ADJUSTMENTS");
								headerActions.add("RETURNS");
								headerActions.add("EXCHANGES");
								headerActions.add("CHARGE_BACK");
							} else {

								lineActions.add("ADJUSTMENTS");
								lineActions.add("RETURNS");
								lineActions.add("EXCHANGES");
								lineActions.add("CHARGE_BACK");
								headerActions.add("ADJUSTMENTS");
								headerActions.add("RETURNS");
								headerActions.add("EXCHANGES");
								headerActions.add("CHARGE_BACK");
							}

						}
					}
					if (ikey >= 3700.5 && ikey != 9000
							&& !(key.equalsIgnoreCase("3700.9000") || key.equalsIgnoreCase("3700.01")
									|| statusMap.get("kitHeader").equalsIgnoreCase("Y"))
							&& !(shipment_invoiceno.equalsIgnoreCase("1") || shipment_invoiceno.equalsIgnoreCase(""))
							&& (fulfillmentType.equalsIgnoreCase("WILLCALL")
									|| fulfillmentType.equalsIgnoreCase("FACTORYSHIP")
									|| fulfillmentType.equalsIgnoreCase("BOPUS")
									|| fulfillmentType.equalsIgnoreCase("WILLCALL_EXCLUDE_STORE"))) {
						if (orderLine.getAdjustments() != null && orderLine.getAdjustments().size() > 0) {
							if (iorderqty == AdjustmentQty && sum == 0) {

								lineActions.add("ADJUSTMENT_HISTORY");
								headerActions.add("ADJUSTMENT_HISTORY");
								lineActions.add("RETURNS");
								lineActions.add("EXCHANGES");
								lineActions.add("CHARGE_BACK");
								headerActions.add("RETURNS");
								headerActions.add("EXCHANGES");

							} else if (iorderqty == AdjustmentQty && sum != 0) {

								lineActions.add("ADJUSTMENTS");
								lineActions.add("RETURNS");
								lineActions.add("EXCHANGES");
								lineActions.add("CHARGE_BACK");
								headerActions.add("ADJUSTMENTS");
								headerActions.add("RETURNS");
								headerActions.add("EXCHANGES");
							} else {
								lineActions.add("ADJUSTMENTS");
								lineActions.add("RETURNS");
								lineActions.add("EXCHANGES");
								lineActions.add("CHARGE_BACK");
								headerActions.add("ADJUSTMENTS");
								headerActions.add("RETURNS");
								headerActions.add("EXCHANGES");
							}

						} else {
							lineActions.add("ADJUSTMENTS");
							lineActions.add("RETURNS");
							lineActions.add("EXCHANGES");
							lineActions.add("CHARGE_BACK");
							headerActions.add("ADJUSTMENTS");
							headerActions.add("RETURNS");
							headerActions.add("EXCHANGES");

						}

						// Packed,Picked,RTF,STS
					}
					if ((key.equalsIgnoreCase("3350.100") && (fulfillmentType.equalsIgnoreCase("TRUCK")
							|| fulfillmentType.equalsIgnoreCase("FACTORYSHIP")
							|| fulfillmentType.equalsIgnoreCase("PARCEL_FUTURE")
							|| fulfillmentType.equalsIgnoreCase("PARCEL")))
							&& !(ikey == 9000 || ikey == 3700.900 || ikey == 3700.100 || ikey == 3350.20)
							&& !(statusMap.get("kitHeader").equalsIgnoreCase("Y") || deliverymtd.equalsIgnoreCase("SHIPTOSTORE")
							|| prodType.equalsIgnoreCase("GIFT_CARD"))) {

						lineActions.add("NON_INVOICE_ADJUSTMENTS");
						headerActions.add("NON_INVOICE_ADJUSTMENTS");

					}
					if (usertype.equalsIgnoreCase("Associate") && ikey > 3200 && ikey < 3700
							&& !(ikey == 9000 || ikey == 3700.900 || ikey == 3700.100 || ikey == 3350.10
									|| ikey == 3350.20)
							&& !(statusMap.get("kitHeader").equalsIgnoreCase("Y") || deliverymtd.equalsIgnoreCase("SHIPTOSTORE")
									|| prodType.equalsIgnoreCase("GIFT_CARD"))
							&& (fulfillmentType.equalsIgnoreCase("TRUCK")
									|| fulfillmentType.equalsIgnoreCase("FACTORYSHIP")
									|| fulfillmentType.startsWith("PARCEL"))) {

						lineActions.add("NON_INVOICE_ADJUSTMENTS");
						headerActions.add("NON_INVOICE_ADJUSTMENTS");

					}
					if (usertype.equalsIgnoreCase("CAM") && ikey > 1500 && ikey < 3700
							&& !(ikey == 9000 || ikey == 3700.900 || ikey == 3700.100 || ikey == 3350.10
									|| ikey == 3350.20)
							&& !(statusMap.get("kitHeader").equalsIgnoreCase("Y") || deliverymtd.equalsIgnoreCase("SHIPTOSTORE")
									|| prodType.equalsIgnoreCase("GIFT_CARD"))
							&& (fulfillmentType.equalsIgnoreCase("TRUCK")
									|| fulfillmentType.equalsIgnoreCase("FACTORYSHIP")
									|| fulfillmentType.startsWith("PARCEL"))) {

						lineActions.add("NON_INVOICE_ADJUSTMENTS");
						headerActions.add("NON_INVOICE_ADJUSTMENTS");

					}
					if ((key.equalsIgnoreCase("9000") || ikey <= 3700.500 && ikey >= 3700 || ikey == 3350.10
							|| ikey == 3350.20)
							&& shipment_invoiceno.equalsIgnoreCase("1") && !(statusMap.get("kitHeader").equalsIgnoreCase("Y") || deliverymtd.equalsIgnoreCase("SHIPTOSTORE")
									|| prodType.equalsIgnoreCase("GIFT_CARD"))
							&& (fulfillmentType.equalsIgnoreCase("TRUCK")
									|| fulfillmentType.equalsIgnoreCase("FACTORYSHIP")
									|| fulfillmentType.startsWith("PARCEL"))) {

						lineActions.add("NON_INVOICE_ADJUSTMENTS");
						headerActions.add("NON_INVOICE_ADJUSTMENTS");
					}
					if (ikey <= 3700.500
							&& (fulfillmentType.equalsIgnoreCase("BOPUS")
									|| fulfillmentType.equalsIgnoreCase("WILLCALL"))
							&& (shipment_invoiceno.equalsIgnoreCase("1"))) {

						lineActions.remove("NON_INVOICE_ADJUSTMENTS");
						headerActions.remove("NON_INVOICE_ADJUSTMENTS");
						lineActions.remove("CHARGE_BACK");
						headerActions.remove("CHARGE_BACK");
					}

					if (usertype.equalsIgnoreCase("CAM") && ikey <= 1500 || (ikey == 2060 || ikey == 2160)) {
						lineActions.add("CANCEL");
						headerActions.add("CANCEL");
					}

					if (usertype.equalsIgnoreCase("Associate") && ikey <= 3200.0) {
						lineActions.add("CANCEL");
						headerActions.add("CANCEL");
					}

					if ((ikey == 3700 || ikey == 3700.500 || ikey == 3700.01)
							&& !(shipment_invoiceno.equalsIgnoreCase("1"))) {
						headerActions.add("POLICY_ADJUSTMENTS");
					}

					if (ikey < 3700.300
							&& (fulfillmentType.equalsIgnoreCase("BOPUS") || fulfillmentType.contains("WILLCALL"))) {

						headerActions.remove("POLICY_ADJUSTMENTS");
					}
					if ((orderLine.getAdjustments() != null && orderLine.getAdjustments().size() > 0)
							|| key.equalsIgnoreCase("3700.01")) {
						lineActions.add("ADJUSTMENT_HISTORY");
						headerActions.add("ADJUSTMENT_HISTORY");

					}

					if (ikey >= 3700 && !(fulfillmentType.equalsIgnoreCase("BOPUS")
							|| fulfillmentType.contains("WILLCALL") || shipment_invoiceno.equalsIgnoreCase(""))
							&& ikey != 3700.100 && !statusMap.get("kitHeader").equalsIgnoreCase("Y")) {
						lineActions.add("CHARGE_BACK");
						headerActions.add("CHARGE_BACK");
					}
					if (ikey >= 3700.500 && !(shipment_invoiceno.equalsIgnoreCase("")) && ikey != 3700.100 && !statusMap.get("kitHeader").equalsIgnoreCase("Y")
							&& (fulfillmentType.equalsIgnoreCase("BOPUS") || fulfillmentType.contains("WILLCALL"))) {
						lineActions.add("CHARGE_BACK");
						headerActions.add("CHARGE_BACK");
					}

					// Fetch Adjustment Response

					for (OrderLine orderLine1 : oslList) {

						List<Adjustment> Adj = orderLine.getAdjustments();

						BigDecimal resultamount = new BigDecimal(0.0);
						BigDecimal resulttax = new BigDecimal(0.0);
						BigDecimal resultothercharges = new BigDecimal(0.0);

						BigDecimal a = new BigDecimal(0.0);
						BigDecimal b = new BigDecimal(0.0);
						BigDecimal c = new BigDecimal(0.0);

						for (Adjustment adjustment : Adj) {

							System.out.println("Sequence---------------" + adjustment.getSequence());

							a = adjustment.getAmount();
							resultamount = a.add(resultamount);

							b = adjustment.getTax();
							resulttax = b.add(resulttax);

							c = adjustment.getShipping();
							resultothercharges = c.add(resultothercharges);

						}

						String Sresultamount = String.valueOf(resultamount.intValue());
						String Sresulttax = String.valueOf(resulttax.intValue());
						String Sresultothercharges = String.valueOf(resultothercharges.intValue());

						// Calculate line total - discount

						String tottax = statusMap.get("tax");
						String totunitprice = statusMap.get("unitprice");
						String DBdiscountamount = statusMap.get("discountamount");
						String DBshippingamount = statusMap.get("shippingamount");

						Double summ = Double.parseDouble(totunitprice) - Double.parseDouble(DBdiscountamount);
						Double ship = Double.parseDouble(DBshippingamount);
						Double taxx = Double.parseDouble(tottax);

						String sumtotal = String.valueOf(summ.intValue());
						String sumship = String.valueOf(ship.intValue());
						String sumtax = String.valueOf(taxx.intValue());

						System.out.println("prime line no---------------" + prime);
						System.out.println(sumtotal + " = " + Sresultamount + "      " + sumtax + " = " + Sresulttax
								+ "     " + sumship + " = " + Sresultothercharges);

						if ((ikey >= 3700 || ikey >= 3700.500) && !key.equalsIgnoreCase("3700.9000")
								&& !key.equalsIgnoreCase("3700.100") && ikey != 9000 && ikey != 3700.01
								&& sumtotal.equals(Sresultamount) && sumtax.equals(Sresulttax)
								&& sumship.equals(Sresultothercharges)) {
							lineActions.add("CHARGE_BACK");
							headerActions.add("CHARGE_BACK");
							lineActions.remove("ADJUSTMENTS");
							headerActions.remove("ADJUSTMENTS");
						}

						for (Adjustment adjustment : Adj) {
							Adjustmentcode = adjustment.getCode();
							if ((Adjustmentcode.contains("A01") || Adjustmentcode.contains("A05")
									|| Adjustmentcode.contains("CPOS") || Adjustmentcode.contains("RET"))
									&& iorderqty == AdjustmentQty) {
								lineActions.remove("RETURNS");
								headerActions.remove("RETURNS");
								lineActions.remove("ADJUSTMENTS");
								headerActions.remove("ADJUSTMENTS");
							}
						}

					}

					for (HashMap<String, String> statusMap1 : statusMapping) {

						String key1 = statusMap1.get("status");
						int ikey1 = Integer.parseInt(statusMap1.get("status").substring(0, 4));

						String Applianceitems = statusMap1.get("silveritems");

						// if (ikey < 1500 &&
						// !statusMap.get("kitHeader").equalsIgnoreCase("Y")) {
						// headerActions.add("ADD_PROMOTIONS");
						//
						// }

						if (ikey1 > 3200 && !statusMap1.get("kitHeader").equalsIgnoreCase("Y")
								&& !Applianceitems.equalsIgnoreCase(" ") && Applianceitems != null) {

							if (lineActions.contains("CANCEL") || headerActions.contains("CANCEL")) {
								lineActions.remove("CANCEL");
								headerActions.remove("CANCEL");
							}
						}
					}

				}
			}

			allowedActions.put(statusMap.get("lineid"), lineActions);

		}
		allowedActions.put("header", headerActions);

		return allowedActions;

	}

	public static HashMap<java.lang.String, java.lang.String> setHeaders(String userType) throws Exception {
		// headers.put("x-mode", "CANCEL_HOLD");
		headers.clear();
		if (userType.equalsIgnoreCase("CAM") || userType.equalsIgnoreCase("Guest")) {
			headers.put(CLIENT_ORIGIN, "CAM");
			headers.put("X-ACCOUNT-ID", getBundle().getString("loggedin.user.accountid"));
			headers.put("X-PHONE-NO", getBundle().getString("last.created.phoneNumber"));
		} else if (userType.equalsIgnoreCase("Associate") || userType.contains("Level")) {
			headers.put(CLIENT_ORIGIN, "CCA");
		} else if (userType.equalsIgnoreCase("Payment")) {
			headers.put(CLIENT_ORIGIN, "Payment");
		} else if (userType.equalsIgnoreCase("Invalid Client")) {
			headers.put(CLIENT_ORIGIN, "CCA");
		} else if (userType.equalsIgnoreCase("APPS")) {
			headers.put(CLIENT_ORIGIN, "APPS");
			headers.put("X-PHONE-NO", getBundle().getString("last.created.phoneNumber"));
		} else if (userType.equalsIgnoreCase("SOMA_USER")) {
			headers.put("Content-Type", "application/x-www-form-urlencoded");
			headers.put("Authorization", "Basic c29tYV91c2VyOnRpZHVhbDAxMTUyMDE4JFA=");

		}

		return headers;
	}

}
