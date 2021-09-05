package az.code.etaskifyapi.controllers;

//import az.code.etaskifyapi.config.TokenProvider;

import az.code.etaskifyapi.config.TokenProvider;
import az.code.etaskifyapi.dto.OrganizationDto;
import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Organization;
import az.code.etaskifyapi.models.security.AuthToken;
import az.code.etaskifyapi.models.security.LoginUser;
import az.code.etaskifyapi.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    private AppUserService appUserService;

    public UserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    //    @Resource(name = "userService")
//    private UserService userService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody LoginUser loginUser) throws AuthenticationException {
        System.out.println(loginUser.getEmail());
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getEmail(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok(new AuthToken(token));
    }

    //    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @PostMapping("/register")
    public OrganizationDto saveUser(@RequestBody OrganizationDto organization) {
        Organization createOrganization = Organization.builder().username(organization.getUsername()).email(organization.getEmail()).phoneNumber(organization.getPhoneNumber())
                .name(organization.getName()).address(organization.getAddress()).build();
        AppUser user = AppUser.builder().role("admin").password(organization.getPassword()).email(organization.getEmail()).build();
        appUserService.addAppUser(user);
        appUserService.addOrganization(createOrganization);
        return organization;
    }
}
