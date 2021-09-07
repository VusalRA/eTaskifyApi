package az.code.etaskifyapi.services;

import az.code.etaskifyapi.dto.UserDto;
import az.code.etaskifyapi.exceptions.EmailAlreadyTakenException;
import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Organization;
import az.code.etaskifyapi.models.User;
import az.code.etaskifyapi.repositories.AppUserRepo;
import az.code.etaskifyapi.repositories.OrganizationRepo;
import az.code.etaskifyapi.repositories.TaskRepo;
import az.code.etaskifyapi.repositories.UserRepo;
import az.code.etaskifyapi.services.interfaces.ETaskifyService;
import az.code.etaskifyapi.util.LoginValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

@Service(value = "eTaskifyService")
public class ETaskifyServiceImpl implements ETaskifyService, UserDetailsService {

    private AppUserRepo appUserRepo;
    private OrganizationRepo organizationRepo;
    private UserRepo userRepo;
    private TaskRepo taskRepo;
    private LoginValidator loginValidator;
    private BCryptPasswordEncoder bcryptEncoder;

    public ETaskifyServiceImpl(AppUserRepo appUserRepo, OrganizationRepo organizationRepo, UserRepo userRepo, TaskRepo taskRepo, LoginValidator loginValidator, BCryptPasswordEncoder bcryptEncoder) {
        this.appUserRepo = appUserRepo;
        this.organizationRepo = organizationRepo;
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
        this.loginValidator = loginValidator;
        this.bcryptEncoder = bcryptEncoder;
    }

    @Override
    public AppUser addAppUser(AppUser appUser) {
        loginValidator.checkEmailAndPassword(appUser.getEmail(), appUser.getPassword());

        if (appUserRepo.findByEmail(appUser.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException();
        }
        appUser.setPassword(bcryptEncoder.encode(appUser.getPassword()));
        return appUserRepo.save(appUser);
    }

    @Override
    public User addUser(UserDto userDto,AppUser userFromToken) {
        User user = User.builder().name(userDto.getName()).surname(userDto.getSurname()).appUserOrganization_id(userFromToken.getId()).build();
        AppUser appUser = AppUser.builder().role("user").email(userDto.getEmail()).password(userDto.getPassword()).build();
        AppUser addAppUser = addAppUser(appUser);
        user.setAppUser(addAppUser);
        return userRepo.save(user);
    }

    @Override
    public Organization addOrganization(Organization organization) {
        return organizationRepo.save(organization);
    }

    @Override
    public AppUser findOne(String username) {
        return appUserRepo.findByName(username);
    }

    @Override
    public List<AppUser> appUsers(AppUser appUser) {
        User user = userRepo.findByAppUser(appUser);
        List<User> users = userRepo.findByAppUserOrganization_id(user.getAppUserOrganization_id());
        List<AppUser> appUsers = new ArrayList<>();
        for (User user1 : users) {
            AppUser userss = appUserRepo.findById(user1.getAppUser().getId()).get();
            appUsers.add(userss);
        }
        return appUsers;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepo.findByName(email);
        if (appUser == null) {
            throw new UsernameNotFoundException("Invalid email or password.");
        }
        return new org.springframework.security.core.userdetails.User(appUser.getEmail(), appUser.getPassword(), getAuthority(appUser));
    }

    private Set<SimpleGrantedAuthority> getAuthority(AppUser appUser) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + appUser.getRole()));
        return authorities;
    }

    public AppUser getAppUserFromToken() {
        String tokenPayload = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        String token = tokenPayload.substring(7, tokenPayload.length());
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readValue(payload, JsonNode.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String email = jsonNode.get("sub").asText();
        return appUserRepo.findByName(email);
    }

}
