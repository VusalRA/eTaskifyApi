package az.code.etaskifyapi.services.interfaces;


import az.code.etaskifyapi.dto.StatusDto;
import az.code.etaskifyapi.dto.TaskDto;
import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Organization;
import az.code.etaskifyapi.models.Task;
import az.code.etaskifyapi.models.User;

import java.util.List;

public interface ETaskifyService {

    AppUser addAppUser(AppUser appUser);

    User addUser(User user);

    Organization addOrganization(Organization organization);

    AppUser findOne(String username);

    AppUser getAppUserFromToken();

    List<AppUser> appUsers(AppUser appUser);

}
