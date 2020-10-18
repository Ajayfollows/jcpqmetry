package com.jcp.automation.common.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.skyscreamer.jsonassert.comparator.JSONCompareUtil.getKeys;
import static org.skyscreamer.jsonassert.comparator.JSONCompareUtil.qualify;

public class IgnorableJsonComparator extends DefaultComparator {

    private List<String> ignoreKeys;

    public IgnorableJsonComparator(JSONCompareMode mode, List<String> ignoreKeys) {
      super(mode);
      this.ignoreKeys = ignoreKeys;
    }

    @Override
    protected void checkJsonObjectKeysExpectedInActual(String prefix, JSONObject expected,
    		JSONObject actual, JSONCompareResult result) throws JSONException {
      Set<String> expectedKeys = getKeys(expected);
      for (String key : expectedKeys) {
        Object expectedValue = expected.get(key);
        if (ignoreKeys.contains(key)) {
          continue;
        }
        if (actual.has(key)) {
          Object actualValue = actual.get(key);
          compareValues(qualify(prefix, key), expectedValue, actualValue, result);
        } else {
          result.missing(prefix, key);
        }
      }
    }

  @Override
  public void compareValues(String prefix, Object expectedValue, Object actualValue, JSONCompareResult result)
      throws JSONException {

    if (isBoolean(expectedValue)) {
      if (!Boolean.valueOf(expectedValue.toString()).equals(Boolean.valueOf(actualValue.toString()))) {
        result.fail(prefix, expectedValue, actualValue);
      }
    } else if (expectedValue instanceof Number || actualValue instanceof Number) {
      if ((!Objects.equals(Double.valueOf(expectedValue.toString()), Double.valueOf(actualValue.toString())))) {
        result.fail(prefix, expectedValue, actualValue);
      }
    } else if (expectedValue.getClass().isAssignableFrom(actualValue.getClass())) {
      if (expectedValue instanceof JSONArray) {
        compareJSONArray(prefix, (JSONArray) expectedValue, (JSONArray) actualValue, result);
      } else if (expectedValue instanceof JSONObject) {
        compareJSON(prefix, (JSONObject) expectedValue, (JSONObject) actualValue, result);
      } else if (!expectedValue.equals(actualValue)) {
        result.fail(prefix, expectedValue, actualValue);
      }
    } else {
      result.fail(prefix, expectedValue, actualValue);
    }
  }

  private boolean isBoolean(Object val) {
      return String.valueOf(val).equalsIgnoreCase("true") ||
          String.valueOf(val).equalsIgnoreCase("false");
  }
}
