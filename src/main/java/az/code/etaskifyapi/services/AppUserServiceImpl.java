package az.code.etaskifyapi.services;

import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Organization;
import az.code.etaskifyapi.models.User;
import az.code.etaskifyapi.repositories.AppUserRepo;
import az.code.etaskifyapi.repositories.OrganizationRepo;
import az.code.etaskifyapi.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service(value = "appUserService")
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private AppUserRepo appUserRepo;
    private OrganizationRepo organizationRepo;
    private UserRepo userRepo;


    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;


    public AppUserServiceImpl(AppUserRepo appUserRepo, OrganizationRepo organizationRepo, UserRepo userRepo) {
        this.appUserRepo = appUserRepo;
        this.organizationRepo = organizationRepo;
        this.userRepo = userRepo;
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
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepo.findByName(email);
//        System.out.println(appUser.getPassword());
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
}
