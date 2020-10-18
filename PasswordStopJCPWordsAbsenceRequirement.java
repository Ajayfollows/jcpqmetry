package com.jcp.automation.common.requirements;

import com.jcp.automation.common.ErrorCodes;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author ykrasnopolskiy
 * @since 3/19/17.
 */
public class PasswordStopJCPWordsAbsenceRequirement extends AbstractRequirement {

    //TODO: move all words to config
    private Set<String> stopAndJcpWords = new TreeSet<>(Arrays.asList("password", "123456", "1234567", "12345678", "123456789", "qwerty"
            , "asdfgh", "zxcvbn", "098765", "secret", "jcpenney", "jcpenneys", "jcpenneyy", "jcpenny", "jcpennys", "penney"
            , "penneys", "penny", "pennys", "jcpenney.com", "jcp"));


    public Set<String> getStopAndJcpWords() {
        return stopAndJcpWords;
    }

    public void setStopAndJcpWords(Set<String> stopAndJcpWords) {
        this.stopAndJcpWords = stopAndJcpWords;
    }


    /**
     * The constructor is planned to be used only to instantiate Requirement inside Requirements enum
     */
    PasswordStopJCPWordsAbsenceRequirement(ErrorCodes associatedErrorIfNotMeetRequiement){
        setErrorCode(associatedErrorIfNotMeetRequiement);
    }


    @Override
    public boolean meets(Object objectToCheck) {
        String password = objectToCheck == null ? null : objectToCheck.toString();

        if (password == null) {
            return true;
        }

        String passwordToLowerCase=password.toLowerCase();
        // Should not contain forbidden words (e.g. jcp ...).
        Set<String> shouldNotContain = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

        shouldNotContain.addAll(stopAndJcpWords);

        boolean contains =
                shouldNotContain.stream().filter(passwordToLowerCase::contains).findFirst().isPresent();

        return !contains;

    }
}
