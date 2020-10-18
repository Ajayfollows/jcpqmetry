package com.jcp.automation.common.requirements;


import com.jcp.automation.common.ErrorCodes;

/**
 * @author ykrasnopolskiy
 * @since 3/19/17.
 */
public class LengthRequirement extends AbstractRequirement {

    private Integer minNameLength;
    private Integer maxNameLength;

    public int getMinNameLength() {
        return minNameLength;
    }

    public void setMinNameLength(Integer minNameLength) {
        validateArguments(minNameLength, maxNameLength);
        this.minNameLength = minNameLength;
    }

    public int getMaxNameLength() {
        return maxNameLength;
    }

    public void setMaxNameLength(int maxNameLength) {
        validateArguments(minNameLength, maxNameLength);
        this.maxNameLength = maxNameLength;
    }

    /**
     * The constructor is planned to be used only to instantiate Requirement inside Requirements enum
     */
    LengthRequirement(ErrorCodes associatedErrorIfNotMeetRequirement){
        setErrorCode(associatedErrorIfNotMeetRequirement);
    }

    LengthRequirement(int minNameLength, int maxNameLength, ErrorCodes associatedErrorIfNotMeetRequirement){
        validateArguments(minNameLength, maxNameLength);
        this.minNameLength = minNameLength;
        this.maxNameLength = maxNameLength;
        setErrorCode(associatedErrorIfNotMeetRequirement);
    }

    private void validateArguments(Integer minNameLength, Integer maxNameLength){
        if (minNameLength != null && maxNameLength != null && minNameLength>maxNameLength)
            throw new RuntimeException("Minimum value of the length have to be less or equal that the maximum one");
    }

    @Override
    public boolean meets(Object objectToCheck) {
        boolean result = true;
        if (objectToCheck == null){
            if (minNameLength != null || maxNameLength != null) result = false;
        } else {
            result = (minNameLength == null || objectToCheck.toString().length() >= minNameLength)
                     && (maxNameLength == null || objectToCheck.toString().length() <= maxNameLength);
        }

        return result;
    }
}
