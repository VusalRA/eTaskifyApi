package az.code.etaskifyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ETaskifyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ETaskifyApiApplication.class, args);
    }

}
