package org.ylab.infrastructure.utils;

import org.ylab.exceptions.EmailFormatException;
import org.ylab.exceptions.PasswordLengthException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {
    private static final String EMAIL_REGEXP = "^[a-zA-Z0-9_]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEXP);
    public static void checkIsNumericValuePositive(double number) {
        if(number < 0 )
            throw new NumberFormatException("Numeric value: " + number + " is not positive");
    }

    public static void checkIsNumericValuePositive(int number) {
        if(number < 0 )
            throw new NumberFormatException("Numeric value: " + number + " is not positive");

    }
    public static void checkIsEmail(String email){
        Matcher matcher = emailPattern.matcher(email);
        if(!matcher.matches())
            throw new EmailFormatException(email);
    }
    public static void checkIsPasswordLongEnough(String password){
        if(password.length() < 8 )
            throw new PasswordLengthException();
    }
}
