package az.code.etaskifyapi.services;

import az.code.etaskifyapi.models.User;

public interface UserService {

    User addUser(User user);

    User deleteUser(User user);

}
