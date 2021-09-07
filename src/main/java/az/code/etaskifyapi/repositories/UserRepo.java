package az.code.etaskifyapi.repositories;

import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByAppUser(AppUser appUser);

    @Query("SELECT user FROM User user where user.appUserOrganization_id=:id")
    List<User> findByAppUserOrganization_id(Long id);


}
