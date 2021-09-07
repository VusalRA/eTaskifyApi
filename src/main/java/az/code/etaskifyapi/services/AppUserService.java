package az.code.etaskifyapi.services;


import az.code.etaskifyapi.dto.StatusDto;
import az.code.etaskifyapi.dto.TaskDto;
import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Organization;
import az.code.etaskifyapi.models.Task;
import az.code.etaskifyapi.models.User;

import java.util.List;

public interface AppUserService {

    AppUser addAppUser(AppUser appUser);

    User addUser(User user);

    Organization addOrganization(Organization organization);

    AppUser findOne(String username);

    Task addTask(TaskDto taskDto, AppUser appUser);

    List<Task> getTasks(AppUser appUser);

    AppUser getAppUserFromToken();

    List<AppUser> appUsers(AppUser appUser);

    Task changeStatus(Long id, StatusDto statusDto);

}
