package az.code.etaskifyapi.repositories;

import az.code.etaskifyapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
