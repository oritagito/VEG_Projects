package com.gis.oim.veg.login;

import oracle.iam.identity.exception.UserNameGenerationException;
import oracle.iam.identity.usermgmt.api.UserNamePolicy;
import oracle.iam.identity.usermgmt.utils.UserNameGenerationUtil;
import oracle.iam.identity.usermgmt.utils.UserNamePolicyUtil;

import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by vasilev-e on 09.03.2016.
 */
public class VEGUserName implements UserNamePolicy{

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String s;

    @Override
    public String getUserNameFromPolicy(Map<String, String> map) throws UserNameGenerationException {

        logger.finest("VEGUserName.getUserNamefromPolicy");
        String userName;

        String firstName = map.get("First Name");
        String lastName = map.get("Last Name");
        String middleName = map.get("Middle Name");

        boolean isFirstName = (firstName == null || firstName.length() == 0) ? false : true;
        boolean isMiddleName = (middleName == null || middleName.length() == 0) ? false : true;

        if (isFirstName) {
            if (isContainInvalidCharacters(firstName)) {
                UserNameGenerationException exception = new UserNameGenerationException("First Name is Invalid", "INVALIDFIRSTNAME");
                throw exception;
            }
        }

        if (isMiddleName) {
            if (isContainInvalidCharacters(middleName)) {
                UserNameGenerationException exception = new UserNameGenerationException("Middle Name is Invalid", "INVALIDMIDDLENAME");
                throw exception;
            }
        }

        if (isContainInvalidCharacters(lastName)) {
            UserNameGenerationException exception = new UserNameGenerationException("Last Name is Invalid", "INVALIDLASTNAME");
            throw exception;
        }

        lastName = cyrillicToLatinResult(lastName.toUpperCase());

        if (isFirstName && isMiddleName) {
            String firstInitial = cyrillicToLatinResult((firstName.substring(0, 1)).toUpperCase());
            String middleInitial = cyrillicToLatinResult((middleName.substring(0, 1)).toUpperCase());
            userName = ((lastName.concat(".")).concat(firstInitial)).concat(middleInitial);
            userName = UserNameGenerationUtil.trimWhiteSpaces(userName);

            if (UserNamePolicyUtil.isUserExists(userName) || (UserNamePolicyUtil.isUserNameReserved(userName))) {
                String baseName = userName;
                boolean userNameGenerated = false;
                for (int i = 1; i < 100; i++) {
                    userName = generateNextName(baseName, firstName, middleName, i);
                    if (UserNameGenerationUtil.isUserNameExistingOrReserved(userName))
                        continue;
                    userNameGenerated = true;
                    break;
                }
                if (!userNameGenerated)
                    throw  new UserNameGenerationException("Failed To Generate User Name", "GENERATEUSERNAMEFAILED");
            }
        }
        else if (!isFirstName && isMiddleName) {
            String middleInitial = cyrillicToLatinResult((middleName.substring(0, 1)).toUpperCase());
            userName = (lastName.concat(".")).concat(middleInitial);
            userName = UserNameGenerationUtil.trimWhiteSpaces(userName);

            if (UserNamePolicyUtil.isUserExists(userName) || (UserNamePolicyUtil.isUserNameReserved(userName))) {
                String baseName = userName;
                boolean userNameGenerated = false;
                for (int i = 1; i < 100; i++) {
                    userName = generateNextName(baseName, null, middleName, i);
                    if (UserNameGenerationUtil.isUserNameExistingOrReserved(userName))
                        continue;
                    userNameGenerated = true;
                    break;
                }
                if (!userNameGenerated)
                    throw  new UserNameGenerationException("Failed To Generate User Name", "GENERATEUSERNAMEFAILED");
            }
        }
        else if (isFirstName && !isMiddleName) {
            String firstInitial = cyrillicToLatinResult((firstName.substring(0, 1)).toUpperCase());
            userName = (lastName.concat(".")).concat(firstInitial);
            userName = UserNameGenerationUtil.trimWhiteSpaces(userName);

            if (UserNamePolicyUtil.isUserExists(userName) || (UserNamePolicyUtil.isUserNameReserved(userName))) {
                String baseName = userName;
                boolean userNameGenerated = false;
                for (int i = 1; i < 10; i++) {
                    userName = generateNextName(baseName, firstName, null, i);
                    if (UserNameGenerationUtil.isUserNameExistingOrReserved(userName))
                        continue;
                    userNameGenerated = true;
                    break;
                }
                if (!userNameGenerated)
                    throw  new UserNameGenerationException("Failed To Generate User Name", "GENERATEUSERNAMEFAILED");
            }
        }
        else {
            userName = lastName;
            userName = UserNameGenerationUtil.trimWhiteSpaces(userName);

            if (UserNamePolicyUtil.isUserExists(userName) || (UserNamePolicyUtil.isUserNameReserved(userName))) {
                String baseName = userName;
                boolean userNameGenerated = false;
                for (int i = 1; i < 100; i++) {
                    userName = generateNextName(baseName, null, null, i);
                    if (UserNameGenerationUtil.isUserNameExistingOrReserved(userName))
                        continue;
                    userNameGenerated = true;
                    break;
                }
                if (!userNameGenerated)
                    throw  new UserNameGenerationException("Failed To Generate User Name", "GENERATEUSERNAMEzFAILED");
            }
        }
        return userName;
    }

    private String cyrillicToLatinMapping(char c){
        switch (c){
            case 'А': return "A";
            case 'Б': return "B";
            case 'В': return "V";
            case 'Г': return "G";
            case 'Д': return "D";
            case 'Е': return "E";
            case 'Ё': return "E";
            case 'Ж': return "ZH";
            case 'З': return "Z";
            case 'И': return "I";
            case 'Й': return "J";
            case 'К': return "K";
            case 'Л': return "L";
            case 'М': return "M";
            case 'Н': return "N";
            case 'О': return "O";
            case 'П': return "P";
            case 'Р': return "R";
            case 'С': return "S";
            case 'Т': return "T";
            case 'У': return "U";
            case 'Ф': return "F";
            case 'Х': return "H";
            case 'Ц': return "TS";
            case 'Ч': return "CH";
            case 'Ш': return "SH";
            case 'Щ': return "SH";
            case 'Ъ': return "";
            case 'Ы': return "Y";
            case 'Ь': return "";
            case 'Э': return "E";
            case 'Ю': return "YU";
            case 'Я': return "YA";
            default: return String.valueOf(c);
        }
    }

    private String cyrillicToLatinResult(String s){
        this.s = s;
        StringBuilder sb = new StringBuilder(s.length()*2);
        for(char c : s.toCharArray()){
            sb.append(cyrillicToLatinMapping(c));
        }
        return sb.toString();
    }

    private String generateNextName (String baseName, String firstName, String middleName, int index) {

        if ((firstName != null && middleName != null) || (firstName == null && middleName != null)) {
            if (index == 1)
                return baseName.concat(cyrillicToLatinResult((middleName.substring(1, 2)).toUpperCase()));
            else
                return baseName.concat(cyrillicToLatinResult((middleName.substring(1, 2)).toUpperCase())).concat(Integer.toString(index));
        }
        else if (firstName != null && middleName == null) {
            if (index == 1)
                return baseName.concat(cyrillicToLatinResult((firstName.substring(1, 2)).toUpperCase()));
            else
                return baseName.concat(cyrillicToLatinResult((firstName.substring(1, 2)).toUpperCase())).concat(Integer.toString(index));
        }
        else
            return baseName.concat(Integer.toString(index));
    }

    private boolean isContainInvalidCharacters(String inputString) {

        if (inputString.startsWith("-") || inputString.startsWith(" ") || inputString.substring(1,2) == "-" || inputString.substring(1,2) == " ")
            return true;

        // Valid only "-" and " "
        String[] invalidCharacters = { "{", "}", "[", "]",
                                       ":", "\"", ";", "№", "'", "+", "=",
                                       "<", ">", "?", ",", ".", "/", "_",
                                       "!", "@", "#", "$", "%", "^", "&", "*", "(", ")",
                                       "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",};

        for (String s : invalidCharacters) {
            if (inputString.contains(s))
                return true;
        }

        return false;
    }

    @Override
    public boolean isUserNameValid(String s, Map<String, String> map) {
        String firstName = map.get("First Name");
        String lastName = map.get("Last Name");
        String middleName = map.get("Middle Name");

        boolean isFirstName = (firstName == null || firstName.length() == 0) ? false : true;
        boolean isMiddleName = (middleName == null || middleName.length() == 0) ? false : true;

        if (isFirstName) {
            if (isContainInvalidCharacters(firstName))
                return false;
        }

        if (isMiddleName) {
            if (isContainInvalidCharacters(middleName))
                return false;
        }

        if (lastName == null || lastName.length() == 0)
            return false;

        if (isContainInvalidCharacters(lastName))
            return false;

        return true;
    }

    @Override
    public String getDescription(Locale locale) {
        return "Last Name First Initial Middle Initial Random Character UserNamePolicy";
    }
}
