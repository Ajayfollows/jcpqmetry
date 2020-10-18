package com.jcp.automation.common.matchers;

import java.util.regex.Pattern;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class PatternMatcher extends BaseMatcher<String> {
	  private final Pattern pattern;
	  
	  public PatternMatcher(Pattern pattern) {
	    this.pattern = pattern;
	  }

	  @Override
	  public boolean matches(Object item) {
	    return pattern.matcher(item.toString()).matches();
	  }

	  @Override
	  public void describeTo(Description description) {
		  description.appendText("match pattern: ").appendText(pattern.toString());
	  }
	}