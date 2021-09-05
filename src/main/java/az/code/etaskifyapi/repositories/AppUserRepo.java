package az.code.etaskifyapi.repositories;

import az.code.etaskifyapi.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {

    @Query("SELECT appUser FROM AppUser appUser where appUser.email=:email")
    AppUser findByName(String email);
}
