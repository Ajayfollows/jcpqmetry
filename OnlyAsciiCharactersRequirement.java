package com.jcp.automation.common.requirements;

import com.google.common.base.CharMatcher;
import com.jcp.automation.common.ErrorCodes;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ykrasnopolskiy
 * @since 3/19/17.
 */
public class OnlyAsciiCharactersRequirement extends AbstractRequirement {

    /**
     * The constructor is planned to be used only to instantiate Requirement inside Requirements enum
     */
    OnlyAsciiCharactersRequirement(ErrorCodes associatedErrorIfNotMeetRequiement){
        setErrorCode(associatedErrorIfNotMeetRequiement);
    }


    @Override
    public boolean meets(Object objectToCheck) {
        String stringToCheck = objectToCheck != null ? objectToCheck.toString() : null;
        return StringUtils.isEmpty(stringToCheck) || CharMatcher.ASCII.matchesAllOf(stringToCheck);
    }
}
