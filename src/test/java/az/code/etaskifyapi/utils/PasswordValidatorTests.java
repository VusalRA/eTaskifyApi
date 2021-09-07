package az.code.etaskifyapi.utils;

import az.code.etaskifyapi.util.PasswordValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class PasswordValidatorTests {

    PasswordValidator passwordValidator = new PasswordValidator();

    @Test
    void passwordValidatorFalse() {
        //given
        String password = "31212";

        //when
        boolean result = passwordValidator.test(password);
        //then
        boolean expected = false;
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void passwordValidatorTrue() {
        //given
        String password = "A3213123";

        //when
        boolean result = passwordValidator.test(password);
        //then
        boolean expected = true;
        assertThat(result).isEqualTo(expected);
    }


}
