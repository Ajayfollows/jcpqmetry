package com.jcp.automation.common.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.google.gson.JsonElement;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.QAFTestBase;
import com.jcp.transformer.automation.util.JSONUtil;
import com.jcp.transformer.automation.util.StringUtil;
import org.apache.commons.configuration.Configuration;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

public class Utils {

  /**
   * Load one resource from the current package as a {@link JsonNode}
   *
   * @return a JSON document
   */
  public static JsonNode getJsonNode(String value) {
    JsonNode jsonNode = null;
    try {
      jsonNode = JsonLoader.fromString(value);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return jsonNode;
  }

  public static String getJSONString(Object datakey) {
    if (datakey == null) {
      return null;
    }
    JSONObject jsonObject = new JSONObject();
    if (datakey instanceof String) {
      JsonElement element = JSONUtil.getGsonElement((String) datakey);
      if ((null != element) && (element.isJsonArray() || element.isJsonObject())) {
        return element.toString();
      }
    }
    Map<String, String> hm = new HashMap<String, String>();
    if (!(datakey instanceof LinkedHashMap)) {
      String str = String.valueOf(datakey);
      Configuration data = getBundle().subset(str);
      if (!data.isEmpty()) {
        Iterator<String> iter = data.getKeys();
        while (iter.hasNext()) {
          String key = iter.next();
          hm.put(key, data.getString(key));
        }
      }
    }
    if (datakey instanceof LinkedHashMap) {
      @SuppressWarnings("unchecked")
      Map<String, String> data = (LinkedHashMap<String, String>) datakey;
      if (!data.isEmpty()) {
        for (String key : data.keySet()) {
          hm.put(key, data.get(key));
        }
      }
    }
    for (String key : hm.keySet()) {
      jsonObject.put(key, hm.get(key));
    }
    return jsonObject.toString();
  }


  public static String generateRandomValidEmailAddress() {
    return "random-" + UUID.randomUUID().toString() + "@jcp.test.com";
  }
}
