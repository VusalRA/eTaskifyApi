package az.code.etaskifyapi.controllers;

import az.code.etaskifyapi.dto.*;
import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Organization;
import az.code.etaskifyapi.models.Task;
import az.code.etaskifyapi.models.User;
import az.code.etaskifyapi.models.security.LoginUser;
import az.code.etaskifyapi.repositories.AppUserRepo;
import az.code.etaskifyapi.repositories.TaskRepo;
import az.code.etaskifyapi.security.AuthenticateService;
import az.code.etaskifyapi.services.AppUserService;
import az.code.etaskifyapi.services.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")

public class ETaskifyController {

    private AuthenticateService authenticateService;
    private AppUserService appUserService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private AppUserRepo appUserRepo;

    public ETaskifyController(AuthenticateService authenticateService, AppUserService appUserService) {
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
        AppUser userFromToken = appUserService.getAppUserFromToken();
        User user = User.builder().name(userDto.getName()).surname(userDto.getSurname()).appUserOrganization_id(userFromToken.getId()).build();
        AppUser appUser = AppUser.builder().role("user").email(userDto.getEmail()).password(userDto.getPassword()).build();
        AppUser user1 = appUserService.addAppUser(appUser);
        user.setAppUser(user1);
        appUserService.addUser(user);
        return userDto;
    }

    @PostMapping("/createtask")
    public ResponseEntity<Task> saveTask(@RequestBody TaskDto taskDto) {
        AppUser appUser = appUserService.findOne(appUserService.getAppUserFromToken().getEmail());
        return ResponseEntity.ok(appUserService.addTask(taskDto, appUser));
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getTask() {
        AppUser appUser = appUserService.getAppUserFromToken();
        return ResponseEntity.ok(appUserService.getTasks(appUser));
    }

    @PostMapping("/assaintask")
    public ResponseEntity<?> edit(@RequestBody AssignDto assignDto) {
        Task task = taskRepo.findById(assignDto.getTaskId()).get();
        task.getAssigned().add(appUserRepo.findByName(assignDto.getEmail()));
        emailService.send(assignDto.getEmail(), "You have new Task: " + task.getTitle());
        return ResponseEntity.ok(taskRepo.save(task));
    }

    @GetMapping("/assignments")
    public ResponseEntity<List<Task>> getAssignList() {
        AppUser appUser = appUserService.getAppUserFromToken();
        List<Task> tasks = taskRepo.findTaskByAssigned(appUser);
        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<Task> changeStatus(@PathVariable Long id, @RequestBody StatusDto statusDto) {
        return ResponseEntity.ok(appUserService.changeStatus(id, statusDto));
    }
}
