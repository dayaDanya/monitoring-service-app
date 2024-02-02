package org.ylab.usecases;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class PasswordUseCaseTest {
    @InjectMocks
    PasswordUseCase passwordUseCase;


    @Test
    void isPswCorrect_true() {
        Assertions.assertTrue(passwordUseCase.isPswCorrect("admin",
                "agSlkyvn99dVB6pKeD6MVA==:EvJ6J8w/LwveTJ+WA5AgTu9OzI7+FNakNFuZO8JqDCM2OUWL/82UXOAwVjPEpkxH5Apw65Pdp8iogVfDwIXbDQ=="));
    }@Test
    void isPswCorrect_false() {
        Assertions.assertFalse(passwordUseCase.isPswCorrect("bad_password",
                "agSlkyvn99dVB6pKeD6MVA==:EvJ6J8w/LwveTJ+WA5AgTu9OzI7+FNakNFuZO8JqDCM2OUWL/82UXOAwVjPEpkxH5Apw65Pdp8iogVfDwIXbDQ=="));
    }
}