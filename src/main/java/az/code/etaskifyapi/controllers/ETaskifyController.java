package az.code.etaskifyapi.controllers;

import az.code.etaskifyapi.dto.OrganizationDto;
import az.code.etaskifyapi.dto.UserDto;
import az.code.etaskifyapi.exceptions.IncorrectNameAndSurnameException;
import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Organization;
import az.code.etaskifyapi.models.security.LoginUser;
import az.code.etaskifyapi.security.AuthenticateService;
import az.code.etaskifyapi.services.interfaces.ETaskifyService;
import az.code.etaskifyapi.util.RegistrationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")

public class ETaskifyController {

    private AuthenticateService authenticateService;
    private ETaskifyService eTaskifyService;

    @Autowired
    private RegistrationValidator registrationValidator;

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
        if (!registrationValidator.checkNameAndSurname(organization.getName())) {
            throw new IncorrectNameAndSurnameException();
        }

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
        if (!registrationValidator.checkNameAndSurname(userDto.getName()) || !registrationValidator.checkNameAndSurname(userDto.getSurname())) {
            throw new IncorrectNameAndSurnameException();
        }
        AppUser userFromToken = eTaskifyService.getAppUserFromToken();
        eTaskifyService.addUser(userDto, userFromToken);
        return userDto;
    }

}
