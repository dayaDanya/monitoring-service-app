package org.ylab.domain.rules;

public interface PasswordEncoder {
    String encrypt(String password);
    boolean isPswCorrect(String plainTextPassword, String hashedPassword);
}
