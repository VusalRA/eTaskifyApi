package az.code.etaskifyapi.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import az.code.etaskifyapi.util.EmailValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailValidatorTest {
    @Autowired
    private EmailValidator emailValidator;

    @Test
    public void testEmail() {
        assertTrue(this.emailValidator.test("vusalra@code.edu.az"));
        assertFalse(this.emailValidator
                .test("vusal"));
    }
}

