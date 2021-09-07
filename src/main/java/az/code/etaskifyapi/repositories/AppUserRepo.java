package az.code.etaskifyapi.repositories;

import az.code.etaskifyapi.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String Email);

    @Query("SELECT appUser FROM AppUser appUser where appUser.email=:email")
    AppUser findByName(String email);


}
