package com.jcp.automation.common.requirements;


import com.jcp.automation.common.ErrorCodes;

/**
 * @author ykrasnopolskiy
 * @since 3/19/17.
 */
public class NonEmptyRequirement extends AbstractRequirement {

    /**
     * The constructor is planned to be used only to instantiate Requirement inside Requirements enum
     */
    NonEmptyRequirement(ErrorCodes associatedErrorIfNotMeetRequirement){
        setErrorCode(associatedErrorIfNotMeetRequirement);
    }


    @Override
    public boolean meets(Object objectToCheck) {
        return objectToCheck != null && objectToCheck.toString().length()>0;
    }
}
