package az.code.etaskifyapi.controllers;

import az.code.etaskifyapi.dto.OrganizationDto;
import az.code.etaskifyapi.dto.UserDto;
import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.User;
import az.code.etaskifyapi.security.AuthenticateService;
import az.code.etaskifyapi.services.interfaces.ETaskifyService;
import az.code.etaskifyapi.util.RegistrationValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ETaskifyController.class})
@ExtendWith(SpringExtension.class)
public class ETaskifyControllerTest {
    @MockBean
    private AuthenticateService authenticateService;

    @Autowired
    private ETaskifyController eTaskifyController;

    @MockBean
    private ETaskifyService eTaskifyService;

    @MockBean
    private RegistrationValidator registrationValidator;

    @Test
    public void saveOrganization() throws Exception {
        when(this.registrationValidator.checkNameAndSurname((String) any())).thenReturn(true);
        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setEmail("vusalra@code.edu.az");
        organizationDto.setPassword("A333333");
        organizationDto.setUsername("vusalra");
        organizationDto.setName("Vusal");
        organizationDto.setPhoneNumber("0776090606");
        organizationDto.setAddress("Address");
        String content = (new ObjectMapper()).writeValueAsString(organizationDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(eTaskifyController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"email\":\"vusalra@code.edu.az\",\"password\":\"A333333\",\"name\":\"Vusal\",\"username\":\"vusalra\",\"phoneNumber"
                                        + "\":\"0776090606\",\"address\":\"Address\"}"));
    }

    @Test
    public void saveUser() throws Exception {
        when(this.registrationValidator.checkNameAndSurname((String) any())).thenReturn(true);
        AppUser appUser = AppUser.builder().email("vusalra@code.edu.az").password("A333333")
                .role("admin").id(3L).tasks(new ArrayList<>()).build();
        AppUser appUser3 = AppUser.builder().email("theabdullayev6@gmail.com").password("A333333")
                .role("admin").id(3L).tasks(new ArrayList<>()).build();
        User user = User.builder().appUser(appUser3).surname("Abdullayev").name("Vusal").appUserOrganization_id(3L).build();
        when(this.eTaskifyService.addUser((UserDto) any(), (AppUser) any())).thenReturn(user);
        when(this.eTaskifyService.getAppUserFromToken()).thenReturn(appUser);
        UserDto userDto = new UserDto();
        userDto.setEmail("theabdullayev6@gmail.com");
        userDto.setPassword("A333333");
        userDto.setName("Vusal");
        userDto.setSurname("Abdullayev");
        String content = (new ObjectMapper()).writeValueAsString(userDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/register/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.eTaskifyController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"email\":\"theabdullayev6@gmail.com\",\"password\":\"A333333\",\"name\":\"Vusal\",\"surname\":\"Abdullayev\"}"));
    }

}

