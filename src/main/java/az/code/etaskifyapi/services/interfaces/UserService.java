package az.code.etaskifyapi.services.interfaces;

import az.code.etaskifyapi.models.User;

public interface UserService {

    User addUser(User user);

    void deleteUser(User user);

    User findUserByEmail(String email);

}
