package com.jcp.automation.common.requirements;


import com.jcp.automation.common.ErrorCodes;

/**
 * @author ykrasnopolskiy
 * @since 3/20/17.
 */
public abstract class AbstractRequirement implements Requirement{
    private ErrorCodes errorCode;

    public ErrorCodes getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCodes errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCodes getAssociatedError(Object objectToCheck) {
        return meets(objectToCheck) ? null : getErrorCode();
    }
}
