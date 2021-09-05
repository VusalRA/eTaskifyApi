package az.code.etaskifyapi.services;


import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Organization;
import az.code.etaskifyapi.models.Task;
import az.code.etaskifyapi.models.User;

import java.util.List;

public interface AppUserService {

    AppUser addAppUser(AppUser appUser);

    User addUser(User user);

    Organization addOrganization(Organization organization);

    List<AppUser> findAll();

    AppUser findOne(String username);

    Task addTask(Task task, AppUser appUser);

    List<Task> getTasks(AppUser appUser);

    AppUser getAppUserFromToken();

}
