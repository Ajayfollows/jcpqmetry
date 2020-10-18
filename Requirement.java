package com.jcp.automation.common.requirements;


import com.jcp.automation.common.ErrorCodes;

/**
 * @author ykrasnopolskiy
 * @since 3/19/17.
 */
public interface Requirement {
    /**
     * Check if the object passed to the method meets some requirements
     * @param objectToCheck object which needs to be checked
     * @return true if the object meets requirements imposed on it
     */
    boolean meets(Object objectToCheck);

    /**
     * Returns an ErrorCodes enum variable with the description of error in case when the original requirement does
     * not meet
     * @param objectToCheck object which needs to be checked
     * @return ErrorCodes enum variable if object does not meet the requirements, null - otherwise
     */
    ErrorCodes getAssociatedError(Object objectToCheck);
}
