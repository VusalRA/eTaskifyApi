package az.code.etaskifyapi.controllers;

import az.code.etaskifyapi.dto.OrganizationDto;
import az.code.etaskifyapi.dto.UserDto;
import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Organization;
import az.code.etaskifyapi.models.Task;
import az.code.etaskifyapi.models.User;
import az.code.etaskifyapi.models.security.LoginUser;
import az.code.etaskifyapi.repositories.UserRepo;
import az.code.etaskifyapi.security.AuthenticateService;
import az.code.etaskifyapi.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {

    private AuthenticateService authenticateService;
    private AppUserService appUserService;

    @Autowired
    private UserRepo userRepo;

    public UserController(AuthenticateService authenticateService, AppUserService appUserService) {
        this.authenticateService = authenticateService;
        this.appUserService = appUserService;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody LoginUser loginUser) throws AuthenticationException {
        return ResponseEntity.ok(authenticateService.getAuthToken(loginUser));
    }

    @PostMapping("/register")
    public OrganizationDto saveOrganization(@RequestBody OrganizationDto organization) {
        Organization createOrganization = Organization.builder().username(organization.getUsername())
                .email(organization.getEmail()).phoneNumber(organization.getPhoneNumber())
                .name(organization.getName()).address(organization.getAddress()).build();
        AppUser user = AppUser.builder().role("admin").password(organization.getPassword()).email(organization.getEmail()).build();
        appUserService.addAppUser(user);
        appUserService.addOrganization(createOrganization);
        return organization;
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/register/user")
    public UserDto saveUser(@RequestBody UserDto userDto) {
        AppUser appUsers = appUserService.findOne(appUserService.getAppUserFromToken().getEmail());
        User user = User.builder().name(userDto.getName()).surname(userDto.getSurname()).appUser(appUsers).build();
        AppUser appUser = AppUser.builder().role("user").email(userDto.getEmail()).password(userDto.getPassword()).build();
        appUserService.addAppUser(appUser);
        appUserService.addUser(user);
        return userDto;
    }

    @PostMapping("/createtask")
    public ResponseEntity<Task> saveTask(@RequestBody Task task) {
        AppUser appUser = appUserService.findOne(appUserService.getAppUserFromToken().getEmail());
        return ResponseEntity.ok(appUserService.addTask(task, appUser));
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks() {
        AppUser appUser = appUserService.findOne(appUserService.getAppUserFromToken().getEmail());
        AppUser user = userRepo.findByAppUser(appUser);
        System.out.println("User email: " + user.getEmail());

        return ResponseEntity.ok(appUserService.getTasks(appUser));
    }

}
