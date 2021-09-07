package az.code.etaskifyapi.services.email;

public interface EmailSender {
    void send(String to, String email);
}
