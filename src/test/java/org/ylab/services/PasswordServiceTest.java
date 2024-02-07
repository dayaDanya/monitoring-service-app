package org.ylab.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class PasswordServiceTest {
    @InjectMocks
    PasswordService passwordUseCase;


    @Test
    @DisplayName("проверяет то, что метод возвращает true при совпадении")
    void isPswCorrect_true() {
        Assertions.assertTrue(passwordUseCase.isPswCorrect("admin",
                "agSlkyvn99dVB6pKeD6MVA==:EvJ6J8w/LwveTJ+WA5AgTu9OzI7+FNakNFuZO8JqDCM2OUWL/82UXOAwVjPEpkxH5Apw65Pdp8iogVfDwIXbDQ=="));
    }
    @Test
    @DisplayName("проверяет то, что метод возвращает false при несовпадении")
    void isPswCorrect_false() {
        Assertions.assertFalse(passwordUseCase.isPswCorrect("bad_password",
                "agSlkyvn99dVB6pKeD6MVA==:EvJ6J8w/LwveTJ+WA5AgTu9OzI7+FNakNFuZO8JqDCM2OUWL/82UXOAwVjPEpkxH5Apw65Pdp8iogVfDwIXbDQ=="));
    }
}