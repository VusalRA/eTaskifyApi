package az.code.etaskifyapi.controllers;

import az.code.etaskifyapi.dto.AssignDto;
import az.code.etaskifyapi.dto.OrganizationDto;
import az.code.etaskifyapi.dto.StatusDto;
import az.code.etaskifyapi.dto.UserDto;
import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Organization;
import az.code.etaskifyapi.models.Task;
import az.code.etaskifyapi.models.User;
import az.code.etaskifyapi.models.security.LoginUser;
import az.code.etaskifyapi.repositories.AppUserRepo;
import az.code.etaskifyapi.repositories.TaskRepo;
import az.code.etaskifyapi.security.AuthenticateService;
import az.code.etaskifyapi.services.email.EmailService;
import az.code.etaskifyapi.services.interfaces.ETaskifyService;
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
    private ETaskifyService eTaskifyService;

    public ETaskifyController(AuthenticateService authenticateService, ETaskifyService eTaskifyService) {
        this.authenticateService = authenticateService;
        this.eTaskifyService = eTaskifyService;
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
        eTaskifyService.addAppUser(user);
        eTaskifyService.addOrganization(createOrganization);
        return organization;
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/register/user")
    public UserDto saveUser(@RequestBody UserDto userDto) {
        AppUser userFromToken = eTaskifyService.getAppUserFromToken();
        User user = User.builder().name(userDto.getName()).surname(userDto.getSurname()).appUserOrganization_id(userFromToken.getId()).build();
        AppUser appUser = AppUser.builder().role("user").email(userDto.getEmail()).password(userDto.getPassword()).build();
        AppUser user1 = eTaskifyService.addAppUser(appUser);
        user.setAppUser(user1);
        eTaskifyService.addUser(user);
        return userDto;
    }

}
