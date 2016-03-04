package com.gis.oim.veg.validators;

import oracle.core.ojdl.logging.ODLLevel;
import oracle.core.ojdl.logging.ODLLogger;
import oracle.iam.platform.kernel.ValidationFailedException;

/**
 * Created by vasilev-e on 25.02.2016.
 */

public class MobileNumberADValidator {
    private static final ODLLogger logger = ODLLogger.getODLLogger(MobileNumberADValidator.class.getName());
    private static final String TELEPHONE_NUMBER_REGEX = "^(\\([0-9]{3}\\)|[0-9]{3}-)[0-9]{3}-[0-9]{4}$";

    public void checkAttr(String input) throws Exception {
        logger.log(ODLLevel.NOTIFICATION, "Enter validate() with parameters: [input: {0}]", new Object[]{input});

        if (input != null && !input.equalsIgnoreCase("")) {
            boolean isTelephoneNumberValidate = input.matches(TELEPHONE_NUMBER_REGEX);
            logger.log(ODLLevel.NOTIFICATION, "Validation Passed?: [{0}]", new Object[]{isTelephoneNumberValidate});

            // Throw Validation Exception since phone number is invalid
            if (!isTelephoneNumberValidate) {
                // Error message is displayed in UI dialog box
                throw new ValidationFailedException("Format of Mobile Number must be: " + TELEPHONE_NUMBER_REGEX);
            }
        }
    }
}