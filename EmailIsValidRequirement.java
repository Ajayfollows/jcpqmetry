package com.jcp.automation.common.requirements;

import com.jcp.automation.common.ErrorCodes;

/**
 * @author ykrasnopolskiy
 * @since 3/19/17.
 */
public class EmailIsValidRequirement extends AbstractRequirement {

    private String validEmailRegex = "[_A-Za-z0-9\\-+]+(\\.[_A-Za-z0-9\\-]+)*@[A-Za-z0-9\\-]+([\\-_.][A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
    private String pciCompliantEmailRegex = "[a-zA-Z0-9@.\\-/+=_ ]*";

    public String getPciCompliantEmailRegex() {
        return pciCompliantEmailRegex;
    }

    public void setPciCompliantEmailRegex(String pciCompliantEmailRegex) {
        this.pciCompliantEmailRegex = pciCompliantEmailRegex;
    }

    public String getValidEmailRegex() {
        return validEmailRegex;
    }

    public void setValidEmailRegex(String validEmailRegex) {
        this.validEmailRegex = validEmailRegex;
    }


    /**
     * The constructor is planned to be used only to instantiate Requirement inside Requirements enum
     */
    EmailIsValidRequirement(ErrorCodes associatedErrorIfNotMeetRequiement){
        setErrorCode(associatedErrorIfNotMeetRequiement);
    }


    @Override
    public boolean meets(Object objectToCheck) {
        String stringToCheck = objectToCheck != null ? objectToCheck.toString() : null;

        return org.apache.commons.lang.StringUtils.isEmpty(stringToCheck)
                || (validateEmailPCICompliant(stringToCheck) && validateEmailAddress(stringToCheck));
    }

    private boolean validateEmailAddress(String value) {
        return value.matches(validEmailRegex);
    }

    private boolean validateEmailPCICompliant(String value){
        return value.matches(pciCompliantEmailRegex);
    }
}
