package com.jcp.automation.common.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcp.transformer.automation.util.Reporter;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.ws.rest.RestTestBase;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.jcp.automation.common.CommonUtils.RESOURCE_VERSION;
import static com.jcp.automation.common.utils.Utils.getJSONString;
import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

/**
 * Rest client for test purposes. Prepares payload data, sends request and processes response.
 */
public class RestClient {

  private static final Logger logger = Logger.getLogger(RestClient.class);
  private static final String PATCH_OVERRIDE_HEADER_NAME = "X-HTTP-Method-Override";
  public static final String AUTH_HEADER = "Authorization";
  public static final String ORIGIN = "Origin";
  private static final String PATCH = "PATCH";
  private static final String CONTENT_TYPE = "Content-Type";
  private static final String BASE_URL = "env.baseurl";
  public static final String PREVIOUS_RESPONSE_STATUS = "last.response.status";
  public static final String PREVIOUS_RESPONSE_MESSAGEBODY="last.response.message";
  public static final String PREVIOUS_RESPONSE = "last.response";
  public static final String PREVIOUS_REQUEST = "last.request";
  public static final String LOCATION = "request.header.location";
  public static final String AGGREGATOR_URL = "api.browse.aggregator.post";
  public static final String LOCATION_HEADER = "Location";
  public static final String GUEST_USER_HEADER = "X-GUEST-USER";
  public static final String GUEST_ACCOUNT_ID_HEADER = "X-GUEST-ACCOUNT-ID";
  public static final String GUEST_USER_ACCOUNT = "guest.account.id";
  public static  String GUEST_USER_HEADER_VALUE = null;
  private static RestTestBase base;
  private static MediaType mediaType = MediaType.APPLICATION_JSON_TYPE;
  public static final String AUTHORIZATION_TOKEN = "authorization.token";
  private static final String ACCEPT_VERSION = "accept.version";
  private static String TOKEN = "Bearer " + getBundle().getString(AUTHORIZATION_TOKEN);
  private static String ACCEPT_VERSION_VALUE = getBundle().getString(ACCEPT_VERSION).toString().replaceAll("[a-zA-Z]", "");
  public static  String ACCEPT_VERSION_HEADER = "Accept-Version";
  private static  String BODY = "BODY";



  /**
   * Get base test instance.
   *
   * @return Rest base test instance.
   */
  public static RestTestBase getBase() {
    if (base == null) {
      base = new RestTestBase();
    }
    return base;
  }

  public static void setGuestHeader(){
    GUEST_USER_HEADER_VALUE = "true";

  }

  /**
   * Sets auth token to desired value.
   *
   * @param token Desired token.
   */
  public static void setAuthorizationToken(String token) {
    TOKEN = token;
  }

  /**
   * Sets Accept-Version value.
   *
   * @param version Desired token.
   */
  public static void setAcceptVersion(String version) {
    ACCEPT_VERSION_VALUE = version;
  }

  /**
   * Resets auth token to default one (specified in properties).
   */
  public static void resetAuthToken() {
    TOKEN = "Bearer " + getBundle().getString(AUTHORIZATION_TOKEN);
  }

  /**
   * Resets auth token to null (needed for guest user and acc creation).
   */
  public static void eliminateAuthToken() {
    setAuthorizationToken(null);
  }

  /**
   * Performs GET REST call to desired service.
   *
   * @param resourcePath Desired resource path.
   */
  public static void get(String resourcePath,  HashMap<String, String> headers ) {
    String resource = getBundle().getString(resourcePath, resourcePath);

    logInput(null,resourcePath,"GET");
    headers.put(AUTH_HEADER, getBundle().getString(AUTHORIZATION_TOKEN));
    headers.put(ORIGIN, getBundle().getString(BASE_URL));
    
    WebResource.Builder builder = getRequestBuilder(resource, mediaType)
            .accept(MediaType.APPLICATION_JSON_TYPE);
           

    initializeHeaders(builder, headers);
    long startTime = System.currentTimeMillis();
    ClientResponse response = builder.get(ClientResponse.class);
    long endTime = System.currentTimeMillis();
   
    setPreviousRequestResponse(response, resource);
    reportTheWSCall(startTime,endTime);
  }
  
  public static void getitemdetails(String resourcePath,  HashMap<String, String> headers ) {
	    String resource = getBundle().getString(resourcePath, resourcePath);

	    logInput(null,resourcePath,"GET");
	    headers.put(AUTH_HEADER, getBundle().getString(AUTHORIZATION_TOKEN));
	    headers.put(ORIGIN, getBundle().getString(BASE_URL));
	    
	    WebResource.Builder builder = getRequestBuilder(resource, mediaType)
	            .accept(MediaType.APPLICATION_JSON_TYPE);
	           

	    initializeHeaders(builder, headers);
	    long startTime = System.currentTimeMillis();
	    ClientResponse response = builder.get(ClientResponse.class);
	    long endTime = System.currentTimeMillis();
	   
	    setPreviousRequestResponse(response, resource);
	   // reportTheWSCall(startTime,endTime);
	  }
  public static void get(String resourcePath) {
	  get(resourcePath, new HashMap<>());
  }
  
  /**
   * Performs DELETE REST call to desired service.
   *
   * @param resourcePath Desired resource path.
   */
  public static void delete(String resourcePath) {
    String resource = getBundle().getString(resourcePath, resourcePath);

    logInput(null,resourcePath,"DELETE");
    long startTime = System.currentTimeMillis();
    ClientResponse response = getRequestBuilder(resource, mediaType)
        .accept(MediaType.APPLICATION_JSON_TYPE)
      //  .header(AUTH_HEADER, TOKEN)
        .header(ORIGIN,getBundle().getString(BASE_URL) )
        .header(ACCEPT_VERSION_HEADER, ACCEPT_VERSION_VALUE)
        .delete(ClientResponse.class);
    long endTime = System.currentTimeMillis();
    setPreviousRequestResponse(response, resource);
    reportTheWSCall(startTime,endTime);
  }

  /**
   * Performs POST REST call to desired service.
   *
   * @param payload      Desired POST payload.
   * @param resourcePath Desired resource path.
   * @param headers      Desired headers.
   */
  public static void post(Object payload, String resourcePath,
                          HashMap<String, String> headers) {
    String resource = getBundle().getString(resourcePath, resourcePath);
    String jsonPayload = getJSONString(payload);
    logInput(payload,resourcePath,"POST");
    String authorization=getBundle().getString(AUTHORIZATION_TOKEN);
    if(authorization!=null)
    headers.put(AUTH_HEADER, getBundle().getString(AUTHORIZATION_TOKEN));
    
    if(resourcePath.contains("v5/accounts")) {
    	headers.remove(AUTH_HEADER);
    }
    //headers.put(ORIGIN, getBundle().getString(BASE_URL));

    //WebResource.Builder builder = getRequestBuilder(resource);
        //.header(AUTH_HEADER, TOKEN)
       // .header(ORIGIN, (ConfigurationManager.getBundle().getString(BASE_URL)).replaceAll("/v\\d", ""))
        //.header(ACCEPT_VERSION_HEADER, ACCEPT_VERSION_VALUE)
        //.accept(MediaType.APPLICATION_JSON_TYPE);
    

    
   
    long startTime = System.currentTimeMillis();
    ClientResponse response=initializeHeaders(getRequestBuilder(resource,mediaType), headers).post(ClientResponse.class, jsonPayload);
    long endTime = System.currentTimeMillis();
    
    setPreviousRequestResponse(response, resource);
    reportTheWSCall(startTime,endTime);
  }
  
  public static void postforBOPUS(Object payload, String resourcePath,
          HashMap<String, String> headers) {
String resource = getBundle().getString(resourcePath, resourcePath);
//JsonString jsonPayload = getJsonString(payload);
System.out.println("BOPUS URL WE ARE HITTING is"+resource);
String jsonPayload = payload.toString();
//getJSONString(payload);
System.out.println("JSON PAYLOAD DATA"+jsonPayload);
logInput(payload,resourcePath,"POST");
String authorization=getBundle().getString(AUTHORIZATION_TOKEN);
if(authorization!=null)
headers.put(AUTH_HEADER, getBundle().getString(AUTHORIZATION_TOKEN));

headers.put("x-client-origin","SOMA_USER");
headers.put("x-campaign-code","BOPUS_PICKUP");
headers.put("Content-Type","application/json");
WebResource.Builder builder = getRequestBuilder(resource, mediaType)
.accept(MediaType.APPLICATION_JSON_TYPE);

long startTime = System.currentTimeMillis();
ClientResponse response=initializeHeaders(getRequestBuilder(resource,mediaType), headers).entity(jsonPayload,mediaType).post(ClientResponse.class, jsonPayload);
long endTime = System.currentTimeMillis();

setPreviousRequestResponse(response, resource);
reportTheWSCall(startTime,endTime);
}
  
  /**
   * Performs POST REST call with content type as x-www-form-urlencoded to desired service.
   *
   * @param payload      Desired POST payload.
   * @param resourcePath Desired resource path.
   */
  public static void postURLEncoded(String resourcePath, 
        HashMap<String, String> headers,MultivaluedMap<String, String> formData) {
		String resource = getBundle().getString(resourcePath, resourcePath);
        
		Reporter.log(resource);
		logInput(null,resourcePath,"POST");
		
		WebResource webResource = getBase().getClient().resource(getRequestUrl(resource));
		//webResource.setProperty("Content-Type", "application/x-www-form-urlencoded");
	    webResource.accept("application/json");
	
	    String oauthtoken=null;
		for(String key: headers.keySet()) {
			if(key.equalsIgnoreCase("authorization"))
				oauthtoken=headers.get(key);
		}

		ClientResponse response=null;
	    long startTime = System.currentTimeMillis();
	    if(oauthtoken!=null)
			 response = webResource
					    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
					    .header("authorization",oauthtoken)
					    .post(ClientResponse.class, formData);
	    else
	    	response = webResource
					    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
					    .post(ClientResponse.class, formData);
		long endTime = System.currentTimeMillis();
		
		setPreviousRequestResponse(response, resource);
		reportTheWSCall(startTime,endTime);
}
  public static void postURLEncodedforoauth(String resourcePath,
	        HashMap<String, String> headers,Object payload ) {
	  String resource = getBundle().getString(resourcePath, resourcePath);
	    String jsonPayload = getJSONString(payload);
	    logInput(payload,resourcePath,"POST");
	    String oauthtoken=null;
		for(String key: headers.keySet()) {
			if(key.equalsIgnoreCase("authorization"))
				oauthtoken=headers.get(key);
		}
		headers.put("authorization", oauthtoken);
	    //headers.put(ORIGIN, getBundle().getString(BASE_URL));

	    //WebResource.Builder builder = getRequestBuilder(resource);
	        //.header(AUTH_HEADER, TOKEN)
	       // .header(ORIGIN, (ConfigurationManager.getBundle().getString(BASE_URL)).replaceAll("/v\\d", ""))
	        //.header(ACCEPT_VERSION_HEADER, ACCEPT_VERSION_VALUE)
	        //.accept(MediaType.APPLICATION_JSON_TYPE);
	    

	    
	   
	    long startTime = System.currentTimeMillis();
	    ClientResponse response=initializeHeaders(getRequestBuilder(resource,mediaType), headers).post(ClientResponse.class, jsonPayload);
	    long endTime = System.currentTimeMillis();
	    
	    setPreviousRequestResponse(response, resource);
	    reportTheWSCall(startTime,endTime);
	}
	  
  public static void postURLEncodedforBOPUS(String resourcePath,
	        HashMap<String, String> headers,MultivaluedMap<String, String> formData) {
			String resource = getBundle().getString(resourcePath, resourcePath);
			logInput(null,resourcePath,"POST");
			
			WebResource webResource = getBase().getClient().resource(getRequestUrl(resource));
			webResource.setProperty("Content-Type", "application/x-www-form-urlencoded");
			webResource.setProperty("XCLIENTORIGIN", "SOMA_USER");
			webResource.setProperty("xcampaigncode", "BOPUS_PICKUP");
		    webResource.accept("application/json");
		
		    String oauthtoken=null;
			for(String key: headers.keySet()) {
				if(key.equalsIgnoreCase("authorization"))
					oauthtoken=headers.get(key);
			}

			ClientResponse response=null;
		    long startTime = System.currentTimeMillis();
		    if(oauthtoken!=null)
				 response = webResource
						    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
						    .header("authorization",oauthtoken)
						    .post(ClientResponse.class, formData);
		    else
		    	response = webResource
						    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
						    .post(ClientResponse.class, formData);
			long endTime = System.currentTimeMillis();
			
			setPreviousRequestResponse(response, resource);
			reportTheWSCall(startTime,endTime);
	}
	  
  /**
   * Performs POST REST call to desired service.
   *
   * @param payload      Desired POST payload.
   * @param resourcePath Desired resource path.
   */
  public static void post(Object payload, String resourcePath) {
    post(payload, resourcePath, new HashMap<>());
  }

  /**
   * Performs PUT REST call to desired service.
   *
   * @param payload      Desired PUT payload.
   * @param resourcePath Desired resource path.
   */
  public static void put(Object payload, String resourcePath, HashMap<String, String> headers) {
    String resource = getBundle().getString(resourcePath, resourcePath);

    logInput(payload,resourcePath,"PUT");
    
    headers.put(AUTH_HEADER, getBundle().getString(AUTHORIZATION_TOKEN));
    //headers.put(ORIGIN, getBundle().getString(BASE_URL));

    long startTime = System.currentTimeMillis();
    ClientResponse response = initializeHeaders(getRequestBuilder(resource,mediaType), headers)
    						  .put(ClientResponse.class, getJSONString(payload));
    long endTime = System.currentTimeMillis();
    
    ConfigurationManager.getBundle().setProperty(BODY, getJSONString(payload));
    setPreviousRequestResponse(response, resource);
    reportTheWSCall(startTime,endTime);
  }
  
  /**
   * Performs PUT REST call to desired service.
   *
   * @param payload      Desired PUT payload.
   * @param resourcePath Desired resource path.
   */
  public static void put(Object payload, String resourcePath) {
	  put(payload, resourcePath, new HashMap<>());
  }

  /**
   * Performs PATCH REST call to desired service.
   *
   * @param payload      Desired PATCH payload.
   * @param resourcePath Desired resource path.
   */
  public static void patch(Object payload, String resourcePath) {
    patch(payload, resourcePath, new HashMap<>());
  }

  /**
   * Performs PATCH REST call to desired service.
   *
   * @param payload      Desired PATCH payload.
   * @param resourcePath Desired resource path.
   * @param headers      Desired headers map.
   */
  public static void patch(Object payload, String resourcePath,
                           HashMap<String, String> headers) {
    String resource = getBundle().getString(resourcePath, resourcePath);
    headers.put(AUTH_HEADER, getBundle().getString(AUTHORIZATION_TOKEN));
    headers.put(ORIGIN, getBundle().getString(BASE_URL));
    headers.put("X-HTTP-Method-Override", "PATCH");
    logInput(payload,resourcePath,"PATCH");

    WebResource.Builder builder = getRequestBuilder(resource)
        .accept(MediaType.APPLICATION_JSON_TYPE);

    long startTime = System.currentTimeMillis();
    ClientResponse response =  initializeHeaders(builder, headers).put(ClientResponse.class, getJSONString(payload));
    long endTime = System.currentTimeMillis();
    
    setPreviousRequestResponse(response, resource);
    reportTheWSCall(startTime,endTime);
  }

  
  
  public static int httppatch(Object payload, String resourcePath,
          HashMap<String, String> headers) {
	  try {
		  String resource = getBundle().getString(resourcePath, resourcePath);
	 logInput(payload,resourcePath,"PATCH");
	  headers.put(AUTH_HEADER, getBundle().getString(AUTHORIZATION_TOKEN));
	  headers.put(ORIGIN, getBundle().getString(BASE_URL));
      //CloseableHttpClient http = HttpClientBuilder.create().build();

      DefaultHttpClient httpClient = new DefaultHttpClient();
      HttpPatch request = new HttpPatch(getRequestUrl(resource));
      request.setEntity(new StringEntity(payload.toString(), ContentType.APPLICATION_JSON));
      
      for (Map.Entry<String, String> entry : headers.entrySet()) {
    	  request.setHeader(entry.getKey(), entry.getValue());
          Reporter.log(entry.getKey()+" : "+entry.getValue());
          System.out.println("Request header name: " + entry.getKey() + ", value: " + entry.getValue());
        }
      //updateRequest.setHeader("SECRET", AUTHTOKEN);
      long startTime = System.currentTimeMillis();
      HttpResponse response = httpClient.execute(request);
      long endTime = System.currentTimeMillis();
      
		String entityResponse = EntityUtils.toString(response.getEntity()); 
		int responseCode=response.getStatusLine().getStatusCode();
		
		
		Reporter.log("Response Code: " + responseCode, MessageTypes.Info);
		Reporter.log("Response Body: " + entityResponse, MessageTypes.Info);
		Reporter.log(
	            "Time taken to execute the api call is:" + ((endTime - startTime) - 1) / 1000 + " Secs");
		
		getBundle().setProperty("last.response.statuscode", response.getStatusLine().getStatusCode());
		getBundle().setProperty("last.response.message", entityResponse);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		return responseCode;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  return 0;
  }
  
  
  /**
   * Creates request builder for sending REST calls.
   *
   * @param url Base url.
   * @return Request builder.
   */
  private static WebResource getResource(String url) {
    WebResource webResource = getBase().getClient().resource(url);
    webResource.setProperty(CONTENT_TYPE, "application/json;charset=utf-8");
//    webResource.setProperty("Content-Encoding", "text/html");
    webResource.accept("application/json");//charset=utf-8");
    return webResource;
  }

  /**
   * Creates request builder for desired media type.
   *
   * @param url        Base url.
   * @param mediaTypes Desired media types.
   * @return Request builder.
   */
  public static WebResource.Builder getRequestBuilder(String url, MediaType... mediaTypes) {
    WebResource webResource = getResource(getRequestUrl(url));
    if (mediaTypes == null || mediaTypes.length <= 0)
      return webResource.type(MediaType.APPLICATION_JSON);
    else {
      return webResource.type(mediaTypes[0]);
    }
  }

  /**
   * Prepares request url based on base url.
   *
   * @param url Resource path.
   * @return Request url.
   */
  public static String getRequestUrl(String url) {
    boolean isAggregatorFlag = url.equals(getBundle().getString(AGGREGATOR_URL));
    if (!url.contains("http")) {
      String base = getBundle().getString(BASE_URL);
      String apiVersion = getBundle().getString(RESOURCE_VERSION);
      if (apiVersion != null && !apiVersion.isEmpty()) {
        return base.replace("v1", apiVersion) + url;
      }
      return isAggregatorFlag ? base.replaceAll("/v\\d", "") + url : base + url;
    }
    return url;
  }

  /**
   * Saves request/response data.
   *
   * @param response Response data.
   * @param resource Request path.
   */
  private static void setPreviousRequestResponse(ClientResponse response, String resource) {
	getBundle().setProperty(PREVIOUS_RESPONSE_STATUS, response.getStatus());  
	getBundle().setProperty(PREVIOUS_RESPONSE, response);
    getBundle().setProperty(PREVIOUS_REQUEST, resource);
    resetAuthToken();
  }

  /**
   * Logs response data to report.
   */
  public static void reportTheWSCall(long endTime,long startTime) {
    Reporter.log("Response Code: " + getBase().getResponse().getStatus().getStatusCode(), MessageTypes.Info);
    Reporter.log(
            "Time taken to execute the api call is:" + ((endTime - startTime) - 1) / 1000 + " Secs");
    if (null != getBase().getResponse().getMessageBody()) {
      String encodedStr = getBase().getResponse().getMessageBody();
      encodedStr = encodedStr.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
      logger.info("Message Body : " + getBase().getResponse().getMessageBody());
      Object json = new JSONTokener(getBase().getResponse().getMessageBody()).nextValue();
      if (json instanceof JSONObject) {
        try {
          Reporter.log("Response : <?prettify?><pre class=\"prettyprint\">"
              + new JSONObject(encodedStr).toString(4) + "</pre>", MessageTypes.Info);
        } catch (Exception e) {
          e.printStackTrace();
        }
        // you have an object
      } else if (json instanceof org.json.JSONArray) {
        // you have an array
        try {
          Reporter.log("Response: <?prettify?><pre class=\"prettyprint\">"
              + new org.json.JSONArray(encodedStr).toString(4) + "</pre>", MessageTypes.Info);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static WebResource.Builder initializeHeaders(WebResource.Builder builder, HashMap<String, String> headers) {
	Reporter.log("Request Headers:");
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      builder.header(entry.getKey(), entry.getValue());
      Reporter.log(entry.getKey()+" : "+entry.getValue());
      System.out.println("Request header name: " + entry.getKey() + ", value: " + entry.getValue());
    }
    return builder;
  }
  
  public static void logInput(Object payload, String resourcePath, String methodName) {
	  String resource = getBundle().getString(resourcePath, resourcePath);
	  	logger.info("Method: "+methodName);
	    logger.info("Endpoint: " + getRequestUrl(resource));
	  
	    Reporter.log("Method: "+methodName, MessageTypes.Info);
	    Reporter.log("Endpoint: " + getRequestUrl(resource));
	    if(payload!=null) {
		    logger.info("Data: " + getJSONString(payload));
		    Reporter.log("Data: <pre class=\"prettyprint\">"
		        + getJSONString(payload) + "</pre>", MessageTypes.Info);
	    }
  }
}
