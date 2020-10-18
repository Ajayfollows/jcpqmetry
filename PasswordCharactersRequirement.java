package com.jcp.automation.common.requirements;

import com.jcp.automation.common.ErrorCodes;

/**
 * @author ykrasnopolskiy
 * @since 3/19/17.
 */
public class PasswordCharactersRequirement extends AbstractRequirement {

    private String passwordValidCharacters = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z0-9!@#$*_ ]{8,16}";

    public String getPasswordValidCharacters() {
        return passwordValidCharacters;
    }

    public void setPasswordValidCharacters(String passwordValidCharacters) {
        this.passwordValidCharacters = passwordValidCharacters;
    }

    /**
     * The constructor is planned to be used only to instantiate Requirement inside Requirements enum
     */
    PasswordCharactersRequirement(ErrorCodes associatedErrorIfNotMeetRequiement){
        setErrorCode(associatedErrorIfNotMeetRequiement);
    }


    @Override
    public boolean meets(Object objectToCheck) {
        return objectToCheck == null || objectToCheck.toString().matches(passwordValidCharacters);
    }
}
