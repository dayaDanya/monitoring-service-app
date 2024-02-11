package org.ylab.infrastructure.utils;

import org.ylab.exceptions.EmailFormatException;
import org.ylab.exceptions.PasswordLengthException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс валидирующий входящие DTO
 */
public class ValidationUtil {

    private static final String EMAIL_REGEXP = "^[a-zA-Z0-9_]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEXP);

    /**
     * Метод проверяющий что число неотрицательно
     * @param number число
     * @exception NumberFormatException выбрасывается если число отрицательное
     */
    public static void checkIsNumericValuePositive(double number) {
        if(number < 0 )
            throw new NumberFormatException("Numeric value: " + number + " is not positive");
    }
    /**
     * Метод проверяющий, что число неотрицательно
     * @param number число
     * @exception NumberFormatException выбрасывается если число отрицательное
     */
    public static void checkIsNumericValuePositive(int number) {
        if(number < 0 )
            throw new NumberFormatException("Numeric value: " + number + " is not positive");

    }

    /**
     * Метод проверяющий email с помощью RegExp
     * @param email проверяемая строка
     * @exception EmailFormatException выбрасывается
     * если строка не соответствует регулярному выражению
     */
    public static void checkIsEmail(String email){
        Matcher matcher = emailPattern.matcher(email);
        if(!matcher.matches())
            throw new EmailFormatException(email);
    }

    /**
     * Метод проверяющий длину пароля
     * @param password проверяемая строка
     * @exception PasswordLengthException выбрасывается если строка короче 8 символов
     */
    public static void checkIsPasswordLongEnough(String password){
        if(password.length() < 8 )
            throw new PasswordLengthException();
    }
}
