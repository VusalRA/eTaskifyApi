package az.code.etaskifyapi.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Something bad happened.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }

    @ExceptionHandler(value = {PasswordNotValidException.class})
    public ResponseEntity<Object> handlePasswordNotValid(PasswordNotValidException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_ACCEPTABLE,
                ex.getLocalizedMessage(), "Password is not valid.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }

    @ExceptionHandler(value = {AuthenticationFailedException.class})
    public ResponseEntity<Object> handleAuthenticationFailed(AuthenticationFailedException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.UNAUTHORIZED,
                ex.getLocalizedMessage(), "Authentication Failed. Username or Password not valid.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }

    @ExceptionHandler(value = {BearerStringException.class})
    public ResponseEntity<Object> handleBearerString(BearerStringException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.UNAUTHORIZED,
                ex.getLocalizedMessage(), "Couldn't find bearer string, header will be ignored");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }

    @ExceptionHandler(value = {ErrorFethcingUsernameException.class})
    public ResponseEntity<Object> handleErrorFetchingUsername(ErrorFethcingUsernameException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.UNAUTHORIZED,
                ex.getLocalizedMessage(), "An error occurred while fetching Username from Token");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }

    @ExceptionHandler(value = {TokenExpiredException.class})
    public ResponseEntity<Object> handleTokenExpired(TokenExpiredException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.UNAUTHORIZED,
                ex.getLocalizedMessage(), "The token has expired");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }

    @ExceptionHandler(value = {EmailNotValidException.class})
    public ResponseEntity<Object> handlePasswordNotValid(EmailNotValidException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_ACCEPTABLE,
                ex.getLocalizedMessage(), "Email is not valid.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }

    @ExceptionHandler(value = {EmailAlreadyTakenException.class})
    public ResponseEntity<Object> handleEmailAlreadyTaken(EmailAlreadyTakenException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_ACCEPTABLE,
                ex.getLocalizedMessage(), "Email is already taken");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }

    @ExceptionHandler(value = {StatusNotValidException.class})
    public ResponseEntity<Object> handleStatusNotValid(StatusNotValidException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_ACCEPTABLE,
                ex.getLocalizedMessage(), "Status does not exist or you entered in lower case");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }


    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<Object> handleNullPointerException(NullPointerException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Please fill out all of the fields.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }


}
