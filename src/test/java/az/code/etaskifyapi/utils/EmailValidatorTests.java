package az.code.etaskifyapi.utils;

import az.code.etaskifyapi.util.EmailValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class EmailValidatorTests {


    EmailValidator emailValidator = new EmailValidator();

    @Test
    void emailValidatorOne() {
        //given
        String emailOne = "email@email.com";
        //when
        boolean result = emailValidator.test(emailOne);
        //then
        boolean expected = true;
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void emailValidatorTwo() {
        String emailTwo = "email";
        boolean resultTwo = emailValidator.test(emailTwo);
        boolean expectedTwo = false;
        assertThat(resultTwo).isEqualTo(expectedTwo);
    }


}
