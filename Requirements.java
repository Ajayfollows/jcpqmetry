package com.jcp.automation.common.requirements;


import com.jcp.automation.common.ErrorCodes;

/**
 * @author ykrasnopolskiy
 * @since 3/19/17.
 */
public enum Requirements implements Requirement {
    //Last name related requirements
    LAST_NAME_LENGTH_REQUIREMENT(new LengthRequirement(Constants.MIN_LAST_NAME_LENGTH
                                                            , Constants.MAX_LAST_NAME_LENGTH
                                                            , ErrorCodes.ACCOUNT_LASTNAME_INVALIDLENGTH)),
    LAST_NAME_NON_EMPTY_REQUIREMENT(new NonEmptyRequirement(ErrorCodes.ACCOUNT_LASTNAME_ISNULL)),
    LAST_NAME_VALID_CHARACTERS_REQUIREMENT(new NameValidCharactersRequirement(ErrorCodes.LAST_NAME_INVALID)),

    LAST_NAME_VALID_REQUIREMENTS(new CompositeRequirement(
            LAST_NAME_NON_EMPTY_REQUIREMENT.getRequirement(),
            LAST_NAME_LENGTH_REQUIREMENT.getRequirement(),
            LAST_NAME_VALID_CHARACTERS_REQUIREMENT.getRequirement()
    )),


    //First name related requirements
    FIRST_NAME_LENGTH_REQUIREMENT(new LengthRequirement(Constants.MIN_FIRST_NAME_LENGTH
                                                            , Constants.MAX_FIRST_NAME_LENGTH
                                                            , ErrorCodes.ACCOUNT_FIRSTNAME_INVALIDLENGTH)),
    FIRST_NAME_NON_EMPTY_REQUIREMENT(new NonEmptyRequirement(ErrorCodes.ACCOUNT_FIRSTNAME_ISNULL)),
    FIRST_NAME_VALID_CHARACTERS_REQUIREMENT(new NameValidCharactersRequirement(ErrorCodes.FIRST_NAME_INVALID)),

    FIRST_NAME_VALID_REQUIREMENTS(new CompositeRequirement(
            FIRST_NAME_NON_EMPTY_REQUIREMENT.getRequirement(),
            FIRST_NAME_LENGTH_REQUIREMENT.getRequirement(),
            FIRST_NAME_VALID_CHARACTERS_REQUIREMENT.getRequirement()
    )),


    //Email related requirements
    EMAIL_LENGTH_REQUIREMENT(new LengthRequirement(Constants.MIN_EMAIL_LENGTH
            , Constants.MAX_EMAIL_LENGTH
            , ErrorCodes.ACCOUNT_EMAIL_INVALIDLENGTH)),
    EMAIL_NON_EMPTY_REQUIREMENT(new NonEmptyRequirement(ErrorCodes.EVENTS_CUSTOMER_EMAIL_ISNULL)),
    EMAIL_CONTAINS_ONLY_ASCII_CHARACTERS(new OnlyAsciiCharactersRequirement(ErrorCodes.ACCOUNT_EMAIL_INVALID)),
    EMAIL_IS_VALID_EMAIL_ADDRESS(new EmailIsValidRequirement(ErrorCodes.ACCOUNT_EMAIL_INVALID)),

    EMAIL_ADDRESS_VALID_REQUIREMENTS(new CompositeRequirement(
            EMAIL_NON_EMPTY_REQUIREMENT.getRequirement()
            , EMAIL_CONTAINS_ONLY_ASCII_CHARACTERS.getRequirement()
            , EMAIL_LENGTH_REQUIREMENT.getRequirement()
            , EMAIL_IS_VALID_EMAIL_ADDRESS.getRequirement()
    )),


    //Password related requirements
    PASSWORD_CONTAINS_ONLY_ASCII_CHARACTERS(new OnlyAsciiCharactersRequirement(ErrorCodes.ACCOUNT_PASSWORD_INVALID_CHAR)),
    PASSWORD_LENGTH_REQUIREMENT(new LengthRequirement(Constants.MIN_PASSWORD_LENGTH
            , Constants.MAX_PASSWORD_LENGTH
            , ErrorCodes.ACCOUNT_PASSWORD_INVALID_LENGTH_RESTRICT)),
    PASSWORD_VALID_CHARACTERS_REQUIREMENT(new PasswordCharactersRequirement(ErrorCodes.ACCOUNT_PASSWORD_INVALID_CHAR)),
    PASSWORD_NO_SPACES_REPEATING_CHARACTERS_REQUIREMENT(
            new PasswordSpacesAndRepeatingCharactersRequirement(ErrorCodes.ACCOUNT_PASSWORD_INVALID_SPACE_RESTRICT)),
    PASSWORD_NO_STOP_JCP_WORDS_REQUIREMENT(
            new PasswordStopJCPWordsAbsenceRequirement(ErrorCodes.ACCOUNT_PASSWORD_INVALID_SPACE_RESTRICT)),

    PASSWORD_VALID_REQUIREMENTS(new CompositeRequirement(
            PASSWORD_CONTAINS_ONLY_ASCII_CHARACTERS.getRequirement()
            , PASSWORD_LENGTH_REQUIREMENT.getRequirement()
            , PASSWORD_VALID_CHARACTERS_REQUIREMENT.getRequirement()
            , PASSWORD_NO_SPACES_REPEATING_CHARACTERS_REQUIREMENT.getRequirement()
            , PASSWORD_NO_STOP_JCP_WORDS_REQUIREMENT.getRequirement()
    ));


    private Requirement requirement;

    public Requirement getRequirement() {
        return requirement;
    }

    Requirements(Requirement requirement){
        this.requirement = requirement;
    }

    @Override
    public boolean meets(Object objectToCheck) {
        return requirement.meets(objectToCheck);
    }

    @Override
    public ErrorCodes getAssociatedError(Object objectToCheck){
        return requirement.getAssociatedError(objectToCheck);
    }

    private static class Constants {
        public static final int MAX_LAST_NAME_LENGTH = 24;
        public static final int MIN_LAST_NAME_LENGTH = 1;
        public static final int MIN_FIRST_NAME_LENGTH = 1;
        public static final int MAX_FIRST_NAME_LENGTH = 17;
        public static final int MIN_EMAIL_LENGTH = 1;
        public static final int MAX_EMAIL_LENGTH = 70;
        public static final int MIN_PASSWORD_LENGTH = 8;
        public static final int MAX_PASSWORD_LENGTH = 16;
    }
}
