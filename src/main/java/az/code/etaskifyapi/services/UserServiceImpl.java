package az.code.etaskifyapi.services;

import az.code.etaskifyapi.exceptions.ErrorFethcingUsernameException;
import az.code.etaskifyapi.models.User;
import az.code.etaskifyapi.repositories.AppUserRepo;
import az.code.etaskifyapi.repositories.UserRepo;
import az.code.etaskifyapi.services.interfaces.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private AppUserRepo appUserRepo;

    public UserServiceImpl(UserRepo userRepo, AppUserRepo appUserRepo) {
        this.userRepo = userRepo;
        this.appUserRepo = appUserRepo;
    }

    @Override
    public User addUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepo.delete(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepo.findByAppUser(appUserRepo.findByEmail(email).orElseThrow(ErrorFethcingUsernameException::new));
    }

}
