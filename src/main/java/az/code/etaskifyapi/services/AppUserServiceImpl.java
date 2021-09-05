package az.code.etaskifyapi.services;

import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Organization;
import az.code.etaskifyapi.models.Task;
import az.code.etaskifyapi.models.User;
import az.code.etaskifyapi.repositories.AppUserRepo;
import az.code.etaskifyapi.repositories.OrganizationRepo;
import az.code.etaskifyapi.repositories.TaskRepo;
import az.code.etaskifyapi.repositories.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service(value = "appUserService")
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private AppUserRepo appUserRepo;
    private OrganizationRepo organizationRepo;
    private UserRepo userRepo;
    private TaskRepo taskRepo;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    public AppUserServiceImpl(AppUserRepo appUserRepo, OrganizationRepo organizationRepo, UserRepo userRepo, TaskRepo taskRepo) {
        this.appUserRepo = appUserRepo;
        this.organizationRepo = organizationRepo;
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
    }

    @Override
    public AppUser addAppUser(AppUser appUser) {
        appUser.setPassword(bcryptEncoder.encode(appUser.getPassword()));
        return appUserRepo.save(appUser);
    }

    @Override
    public User addUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public Organization addOrganization(Organization organization) {
        return organizationRepo.save(organization);
    }

    @Override
    public List<AppUser> findAll() {
        return appUserRepo.findAll();
    }

    @Override
    public AppUser findOne(String username) {
        return appUserRepo.findByName(username);
    }

    @Override
    public Task addTask(Task task, AppUser appUser) {
//        List<AppUser> appUsers = findAll();
//        task.getAppUsers().se
        task.setAppUsers(appUser);
        return taskRepo.save(task);
    }

    @Override
    public List<Task> getTasks(AppUser appUser) {
        return taskRepo.findTaskByAppUsers(appUser);
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

        System.out.println(payload);
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
