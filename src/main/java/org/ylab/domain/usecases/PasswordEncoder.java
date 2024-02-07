package org.ylab.domain.usecases;

public interface PasswordEncoder {
    String encrypt(String password);
    boolean isPswCorrect(String plainTextPassword, String hashedPassword);
}
