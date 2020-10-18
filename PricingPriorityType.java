package com.jcp.automation.common.utils;

/**
 * Author gkarabut
 * since 9/20/17.
 */
public enum PricingPriorityType {
  CLEARANCE(1),
  SALE(2),
  NOW(3),
  ORIGINAL(4),
  DEFAULT(5);

  private Integer priority;

  PricingPriorityType(Integer priority){
    this.priority = priority;
  }

  public Integer getPriority() {
    return priority;
  }
}
