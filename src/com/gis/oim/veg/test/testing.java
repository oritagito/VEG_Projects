package com.gis.oim.veg.test;

import java.util.Locale;
import java.util.Map;

/**
 * Created by vasilev-e on 11.03.2016.
 */
public class testing {

    public static void main(String[] args) {

        String userName;
        String firstName = "";
        String lastName = "Фамилия";
        String middleName = "Отчество9";

        System.out.println("FN: " + firstName + " " + firstName.length() + " LN: " + lastName + " MN: " + middleName);

        boolean isFirstName = (firstName == null || firstName.length() == 0) ? false : true;
        //boolean isFirstName = true;
        boolean isMiddleName = (middleName != null || middleName.length() != 0) ? true : false;

        System.out.println(firstName.length());
        //  if (firstName == null || firstName.length() == 0)
        //     isFirstName = false;

        System.out.println("isFN: " + Boolean.toString(isFirstName) + " isMN: " + isMiddleName);

        if (isFirstName) {
            if (isContainInvalidCharacters(firstName)) {
                System.out.println("isContainInvalidCharacters(firstName) TRUE");
            }
            firstName = cyrillicToLatinResult(firstName.toUpperCase());
            System.out.println("cyrillicToLatinResult(FN): " + firstName);
        }

        if (isMiddleName) {
            if (isContainInvalidCharacters(middleName)) {
                System.out.println("isContainInvalidCharacters(middleName) TRUE");
            }
            middleName = cyrillicToLatinResult(middleName.toUpperCase());
            System.out.println("cyrillicToLatinResult(MN): " + middleName);
        }

        if (isContainInvalidCharacters(lastName)) {
            System.out.println("isContainInvalidCharacters(lastName) TRUE");
        }
        lastName = cyrillicToLatinResult(lastName.toUpperCase());
        System.out.println("cyrillicToLatinResult(LN): " + lastName);

        if (isFirstName && isMiddleName) {
            String firstInitial = firstName.substring(0, 1);
            String middleInitial = middleName.substring(0, 1);
            userName = (lastName.concat(firstInitial)).concat(middleInitial);
            System.out.println(userName);
        } else if (!isFirstName && isMiddleName) {
            String middleInitial = middleName.substring(0, 1);
            userName = lastName.concat(middleInitial);
            System.out.println(userName);
        } else if (isFirstName && !isMiddleName) {
            String firstInitial = firstName.substring(0, 1);
            userName = lastName.concat(firstInitial);
            System.out.println(userName);
        } else {
            userName = lastName;
            System.out.println(userName);
        }

        String baseName = userName;
        boolean userNameGenerated = false;
        int index = 1;
        for (; !userNameGenerated; ) {
            System.out.println(index);
            if (index == 1 || index == 2) {
                System.out.println("continue");
                index++;
            }
            userNameGenerated = true;
        }
    }

    private static String cyrillicToLatinMapping(char c) {
        switch (c) {
            case 'А':
                return "A";
            case 'Б':
                return "B";
            case 'В':
                return "V";
            case 'Г':
                return "G";
            case 'Д':
                return "D";
            case 'Е':
                return "E";
            case 'Ё':
                return "E";
            case 'Ж':
                return "ZH";
            case 'З':
                return "Z";
            case 'И':
                return "I";
            case 'Й':
                return "J";
            case 'К':
                return "K";
            case 'Л':
                return "L";
            case 'М':
                return "M";
            case 'Н':
                return "N";
            case 'О':
                return "O";
            case 'П':
                return "P";
            case 'Р':
                return "R";
            case 'С':
                return "S";
            case 'Т':
                return "T";
            case 'У':
                return "U";
            case 'Ф':
                return "F";
            case 'Х':
                return "H";
            case 'Ц':
                return "TS";
            case 'Ч':
                return "CH";
            case 'Ш':
                return "SH";
            case 'Щ':
                return "SH";
            case 'Ъ':
                return "";
            case 'Ы':
                return "Y";
            case 'Ь':
                return "";
            case 'Э':
                return "E";
            case 'Ю':
                return "YU";
            case 'Я':
                return "YA";
            default:
                return String.valueOf(c);
        }
    }

    private static String cyrillicToLatinResult(String s) {
        StringBuilder sb = new StringBuilder(s.length() * 2);
        for (char c : s.toCharArray()) {
            sb.append(cyrillicToLatinMapping(c));
        }
        return sb.toString();
    }

    private static String generateNextName(String baseName, String firstName, String middleName, int index) {
        if ((firstName != null && middleName != null) || (firstName == null && middleName != null)) {
            if (index > 1)
                return baseName.concat(middleName.substring(1, 2));
            else
                return baseName.concat(Integer.toString(index));
        } else if (firstName != null && middleName == null) {
            if (index > 1)
                return baseName.concat(firstName.substring(1, 2));
            else
                return baseName.concat(Integer.toString(index));
        } else
            return baseName.concat(Integer.toString(index));
    }

    private static boolean isContainInvalidCharacters(String inputString) {

        if (inputString.startsWith("-") || inputString.startsWith(" ") || inputString.substring(1, 2) == "-" || inputString.substring(1, 2) == " ")
            return true;

        // Valid only "-" and " "
        String[] invalidCharacters = {"{", "}", "[", "]",
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

    public static boolean isUserNameValid(String s, Map<String, String> map) {
        return true;
    }

    public static String getDescription(Locale locale) {
        return "Last Name First Initial Middle Initial Random Character UserNamePolicy";
    }
}
