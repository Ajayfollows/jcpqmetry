package com.jcp.automation.common.requirements;

import com.google.common.base.CharMatcher;
import com.jcp.automation.common.ErrorCodes;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ykrasnopolskiy
 * @since 3/19/17.
 */
public class NameValidCharactersRequirement extends AbstractRequirement {

    private String nameAlphabet = ".*[a-zA-Z]+.*";
    private String repeatingCharacters = ".*(.)\\1\\1+.*";

    public String getNameAlphabet() {
        return nameAlphabet;
    }

    public void setNameAlphabet(String nameAlphabet) {
        this.nameAlphabet = nameAlphabet;
    }

    public String getRepeatingCharacters() {
        return repeatingCharacters;
    }

    public void setRepeatingCharacters(String repeatingCharacters) {
        this.repeatingCharacters = repeatingCharacters;
    }


    /**
     * The constructor is planned to be used only to instantiate Requirement inside Requirements enum
     */
    NameValidCharactersRequirement(ErrorCodes associatedErrorIfNotMeetRequiement){
        setErrorCode(associatedErrorIfNotMeetRequiement);
    }


    @Override
    public boolean meets(Object objectToCheck) {
        String nameToCheck = objectToCheck != null ? objectToCheck.toString() : null;
        return StringUtils.isEmpty(nameToCheck)
                || (CharMatcher.ASCII.matchesAllOf(nameToCheck)
                && nameToCheck.matches(nameAlphabet)
                && !nameToCheck.toLowerCase().matches(repeatingCharacters));
    }
}
