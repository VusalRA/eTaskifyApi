package az.code.etaskifyapi.utils;

import az.code.etaskifyapi.util.PasswordValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PasswordValidatorTest {
    @Autowired
    private PasswordValidator passwordValidator;

    @Test
    public void testPassword() {
        assertTrue(this.passwordValidator.test("AVusal609"));
        assertFalse(this.passwordValidator.test("vusal"));
    }
}

