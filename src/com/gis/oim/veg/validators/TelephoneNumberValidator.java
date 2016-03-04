package com.gis.oim.veg.validators;

import oracle.core.ojdl.logging.ODLLevel;
import oracle.core.ojdl.logging.ODLLogger;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.platform.context.ContextAware;
import oracle.iam.platform.kernel.ValidationException;
import oracle.iam.platform.kernel.ValidationFailedException;
import oracle.iam.platform.kernel.spi.ValidationHandler;
import oracle.iam.platform.kernel.vo.BulkOrchestration;
import oracle.iam.platform.kernel.vo.Orchestration;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by vasilev-e on 25.02.2016.
 */
public class TelephoneNumberValidator implements ValidationHandler {
    private static final ODLLogger logger = ODLLogger.getODLLogger(TelephoneNumberValidator.class.getName());
    private static final String TELEPHONE_NUMBER_REGEX = "^(\\([0-9]{3}\\)|[0-9]{3}-)[0-9]{3}-[0-9]{4}$";

    public void validate(long l, long l1, Orchestration orchestration) {
        logger.log(ODLLevel.NOTIFICATION, "Enter validate() with parameters: [Process Id: {0}], [Event Id: {1}]", new Object[]{l, l1});

        HashMap<String, Serializable> contextParams = orchestration.getParameters();
        logger.log(ODLLevel.NOTIFICATION, "Changed parameters: [{0}]", new Object[]{contextParams});

        String newTelephoneNumber = getParamaterValue(contextParams, UserManagerConstants.AttributeName.PHONE_NUMBER.getId());
        logger.log(ODLLevel.NOTIFICATION, "Telephone Number Regex: [{0}]", new Object[]{TELEPHONE_NUMBER_REGEX});
        logger.log(ODLLevel.NOTIFICATION, "New Telephone Number: [{0}]", new Object[]{newTelephoneNumber});

        // Perform validation on the new telephone number
        if (newTelephoneNumber != null && !newTelephoneNumber.equalsIgnoreCase("")) {
            boolean isTelephoneNumberValidate = newTelephoneNumber.matches(TELEPHONE_NUMBER_REGEX);
            logger.log(ODLLevel.NOTIFICATION, "Validation Passed?: [{0}]", new Object[]{isTelephoneNumberValidate});

            // Throw Validation Exception since phone number is invalid
            if (!isTelephoneNumberValidate) {
                // Error message is displayed in UI dialog box
                throw new ValidationFailedException("Format of Telephone Number must be: " + TELEPHONE_NUMBER_REGEX);
            }
        }
    }

    private String getParamaterValue(HashMap<String, Serializable> parameters, String key) {
        if (parameters.containsKey(key)) {
            String value = (parameters.get(key) instanceof ContextAware) ? (String) ((ContextAware) parameters.get(key)).getObjectValue() : (String) parameters.get(key);
            return value;
        } else {
            return null;
        }
    }

    public void validate(long l, long l1, BulkOrchestration bulkOrchestration) throws ValidationException, ValidationFailedException {
    }

    public void initialize(HashMap<String, String> hashMap) {
    }
}