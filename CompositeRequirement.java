package com.jcp.automation.common.requirements;


import com.jcp.automation.common.ErrorCodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ykrasnopolskiy
 * @since 3/20/17.
 */
public class CompositeRequirement implements Requirement {
    List<Requirement> requirementsList;

    public List<Requirement> getRequirementsList() {
        return requirementsList;
    }

    public void setRequirementsList(List<Requirement> requirementsList) {
        this.requirementsList = requirementsList;
    }

    CompositeRequirement(Requirement... requirements){
        requirementsList = new ArrayList<>(Arrays.asList(requirements));
    }

    @Override
    public boolean meets(Object objectToCheck) {
        boolean result = true;
        for (Requirement requirement : requirementsList){
            result = result && requirement.meets(objectToCheck);
            if (!result) break;
        }
        return result;
    }

    @Override
    public ErrorCodes getAssociatedError(Object objectToCheck) {
        ErrorCodes errorCode = null;
        for (Requirement requirement : requirementsList){
            errorCode = requirement.getAssociatedError(objectToCheck);
            if (errorCode != null) break;
        }
        return errorCode;
    }
}
