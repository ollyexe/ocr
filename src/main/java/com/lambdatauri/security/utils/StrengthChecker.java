package com.lambdatauri.security.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrengthChecker {

    public static boolean isValidPassword(String password) {
        return isLengthValid(password) && isStrengthValid(password);
    }

    private static boolean isLengthValid(String password) {
        // Minimum length requirement (adjust as needed)
        int minLength = 10;
        return password.length() >= minLength;
    }

    private static boolean isStrengthValid(String password) {
        // Strength requirements (adjust as needed)
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
