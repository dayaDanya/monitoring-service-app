package org.ylab.usecases;

import org.ylab.domain.rules.PasswordEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Класс, представляющий использование сущности PasswordEncoder в рамках бизнес-логики.
 * Реализует методы для шифрования пароля и проверки корректности введенного пароля.
 */
public class PasswordUseCase implements PasswordEncoder {

    /**
     * Метод для шифрования пароля с использованием соли.
     *
     * @param password Пароль, который требуется зашифровать.
     * @return Зашифрованный пароль с использованием соли.
     */
    @Override
    public String encrypt(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(), salt,
                10001, 64 * 8);

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();

            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Метод для проверки корректности введенного пароля.
     *
     * @param plainTextPassword Введенный пароль в текстовой форме.
     * @param hashedPassword    Зашифрованный пароль с использованием соли.
     * @return {@code true}, если пароль корректен, иначе {@code false}.
     */
    @Override
    public boolean isPswCorrect(String plainTextPassword, String hashedPassword) {
        String[] parts = hashedPassword.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] storedHash = Base64.getDecoder().decode(parts[1]);

        PBEKeySpec spec = new PBEKeySpec(plainTextPassword.toCharArray(), salt, 10001, 64 * 8);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] enteredHash = skf.generateSecret(spec).getEncoded();

            return MessageDigest.isEqual(storedHash, enteredHash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return false;
    }
}