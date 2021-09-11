package az.code.etaskifyapi.services;

import az.code.etaskifyapi.dto.UserDto;
import az.code.etaskifyapi.exceptions.EmailAlreadyTakenException;
import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Organization;
import az.code.etaskifyapi.models.Task;
import az.code.etaskifyapi.models.User;
import az.code.etaskifyapi.repositories.AppUserRepo;
import az.code.etaskifyapi.repositories.OrganizationRepo;
import az.code.etaskifyapi.repositories.TaskRepo;
import az.code.etaskifyapi.repositories.UserRepo;
import az.code.etaskifyapi.services.interfaces.ETaskifyService;
import az.code.etaskifyapi.util.LoginValidator;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class ETaskifyServiceImplTest {

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ETaskifyServiceImpl eTaskifyServiceImpl;

    @Mock
    private AppUserRepo appUserRepo;
    @Mock
    private OrganizationRepo organizationRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private TaskRepo taskRepo;
    @Mock
    private LoginValidator loginValidator;
    @Mock
    private BCryptPasswordEncoder bcryptEncoder;
    private ETaskifyService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ETaskifyServiceImpl(appUserRepo, organizationRepo, userRepo, taskRepo, loginValidator, bcryptEncoder);
    }

    @Order(1)
    @Test
    void addAppUser() {
        AppUser appUser = AppUser.builder().role("admin").email("vusalra@code.edu.az").password("333").build();
        underTest.addAppUser(appUser);
        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepo).save(appUserArgumentCaptor.capture());
        AppUser capturedUser = appUserArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(appUser);
    }

    @Order(2)
    @Test
    void addOrganization() {
        Organization organization = Organization.builder().address("address").email("email").username("username").
                phoneNumber("0776090606").build();
        underTest.addOrganization(organization);
        ArgumentCaptor<Organization> organizationArgumentCaptor = ArgumentCaptor.forClass(Organization.class);
        verify(organizationRepo).save(organizationArgumentCaptor.capture());
        Organization capturedOrganization = organizationArgumentCaptor.getValue();
        assertThat(capturedOrganization).isEqualTo(organization);
    }

    @Order(3)
    @Test
    void findOne() {
        AppUser appUser = AppUser.builder().role("admin").email("vusalra@code.edu.az").password("333").build();
        when(appUserRepo.findByName("vusalra@code.edu.az")).thenReturn(appUser);
        AppUser find = underTest.findOne("vusalra@code.edu.az");
        assertThat(appUser).isEqualTo(find);
        verify(appUserRepo).findByName(appUser.getEmail());
    }

}