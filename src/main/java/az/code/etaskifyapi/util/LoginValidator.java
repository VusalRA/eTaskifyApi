package az.code.etaskifyapi.util;

import az.code.etaskifyapi.exceptions.EmailAlreadyTakenException;
import az.code.etaskifyapi.exceptions.EmailNotValidException;
import az.code.etaskifyapi.exceptions.PasswordNotValidException;
import az.code.etaskifyapi.repositories.AppUserRepo;
import org.springframework.stereotype.Component;

@Component
public class LoginValidator {

    private EmailValidator emailValidator;
    private PasswordValidator passwordValidator;
    private AppUserRepo appUserRepo;

    public LoginValidator(EmailValidator emailValidator, PasswordValidator passwordValidator, AppUserRepo appUserRepo) {
        this.emailValidator = emailValidator;
        this.passwordValidator = passwordValidator;
        this.appUserRepo = appUserRepo;
    }

    public void checkEmailAndPassword(String email, String password) {
        boolean isValidEmail = emailValidator.
                test(email);
        if (!isValidEmail) {
            throw new EmailNotValidException();
        }

        boolean isValidPassword = passwordValidator.
                test(password);
        if (!isValidPassword) {
            throw new PasswordNotValidException();
        }
    }

}
