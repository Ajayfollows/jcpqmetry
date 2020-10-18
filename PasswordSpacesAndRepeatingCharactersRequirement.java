package com.jcp.automation.common.requirements;

import com.jcp.automation.common.ErrorCodes;

import java.util.regex.Pattern;

/**
 * @author ykrasnopolskiy
 * @since 3/19/17.
 */
public class PasswordSpacesAndRepeatingCharactersRequirement extends AbstractRequirement {

    private Pattern spaces = Pattern.compile("[\\s\\t]");
    private String repeatingCharactersRegex = ".*(.)\\1{2}.*";


    public Pattern getSpaces() {
        return spaces;
    }

    public void setSpaces(Pattern spaces) {
        this.spaces = spaces;
    }

    public String getRepeatingCharactersRegex() {
        return repeatingCharactersRegex;
    }

    public void setRepeatingCharactersRegex(String repeatingCharactersRegex) {
        this.repeatingCharactersRegex = repeatingCharactersRegex;
    }


    /**
     * The constructor is planned to be used only to instantiate Requirement inside Requirements enum
     */
    PasswordSpacesAndRepeatingCharactersRequirement(ErrorCodes associatedErrorIfNotMeetRequiement){
        setErrorCode(associatedErrorIfNotMeetRequiement);
    }


    @Override
    public boolean meets(Object objectToCheck) {
        return objectToCheck == null
                || !spaces.matcher(objectToCheck.toString()).find()
                || !objectToCheck.toString().matches(repeatingCharactersRegex);
    }
}
