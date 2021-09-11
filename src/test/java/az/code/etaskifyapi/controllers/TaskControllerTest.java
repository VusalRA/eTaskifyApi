package az.code.etaskifyapi.controllers;

import az.code.etaskifyapi.dto.AssignDto;
import az.code.etaskifyapi.enums.Status;
import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Task;
import az.code.etaskifyapi.services.interfaces.ETaskifyService;
import az.code.etaskifyapi.services.interfaces.TaskService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TaskController.class})
@ExtendWith(SpringExtension.class)
public class TaskControllerTest {
    @MockBean
    private ETaskifyService eTaskifyService;

    @Autowired
    private TaskController taskController;

    @MockBean
    private TaskService taskService;

    @Test
    public void findTaskById() throws Exception {
        AppUser appUser = AppUser.builder().email("vusalra@code.edu.az").password("A333333")
                .role("admin").id(3L).tasks(new ArrayList<>()).build();
        Task task = Task.builder().id(3L).title("title").status(Status.BACKLOG).description("desc").appUsers(appUser)
                .deadline(LocalDateTime.of(2022, 9, 19, 19, 9)).build();
        when(this.taskService.findTaskById((Long) any())).thenReturn(task);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/task/{id}", 3L);
        MockMvcBuilders.standaloneSetup(this.taskController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":3,\"title\":\"title\",\"description\":\"desc\",\"deadline\":[2022,9,19"
                                        + ",19,9],\"status\":\"BACKLOG\"}"));
    }


    @Test
    public void deleteTaskById() throws Exception {
        AppUser appUser = AppUser.builder().email("vusalra@code.edu.az").password("A333333")
                .role("admin").id(3L).tasks(new ArrayList<>()).build();

        Task task = Task.builder().id(3L).title("title").status(Status.BACKLOG).description("desc").appUsers(appUser)
                .deadline(LocalDateTime.of(2022, 9, 19, 19, 9)).build();
        when(this.taskService.deleteTask((Task) any())).thenReturn(task);
        when(this.taskService.findTaskById((Long) any())).thenReturn(task);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/task/{id}", 123L);
        MockMvcBuilders.standaloneSetup(this.taskController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":3,\"title\":\"title\",\"description\":\"desc\",\"deadline\":[2022,9,19"
                                        + ",19,9],\"status\":\"BACKLOG\"}"));
    }

    @Test
    public void edit() throws Exception {
        AppUser appUser = AppUser.builder().email("vusalra@code.edu.az").password("A333333")
                .role("admin").id(3L).tasks(new ArrayList<>()).build();
        Task task = Task.builder().id(3L).title("title").status(Status.BACKLOG).description("desc").appUsers(appUser)
                .deadline(LocalDateTime.of(2022, 9, 19, 19, 9)).build();
        when(this.taskService.assign((AssignDto) any())).thenReturn(task);

        AssignDto assignDto = new AssignDto();
        assignDto.setEmail("vusalra@code.edu.az");
        assignDto.setTaskId(3L);
        String content = (new ObjectMapper()).writeValueAsString(assignDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/assign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.taskController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":3,\"title\":\"title\",\"description\":\"desc\",\"deadline\":[2022,9,19"
                                        + ",19,9],\"status\":\"BACKLOG\"}"));
    }

    @Test
    public void getAssignList() throws Exception {
        AppUser appUser = AppUser.builder().email("vusalra@code.edu.az").password("A333333")
                .role("admin").id(3L).tasks(new ArrayList<>()).build();

        Task task = Task.builder().id(3L).title("title").status(Status.BACKLOG).description("desc").appUsers(appUser)
                .deadline(LocalDateTime.of(2022, 9, 19, 19, 9)).build();
        List<Task> assignments = new ArrayList<>();
        assignments.add(task);
        when(this.taskService.getAssignments((AppUser) any())).thenReturn(assignments);
        when(this.eTaskifyService.getAppUserFromToken()).thenReturn(appUser);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/assignments");
        MockMvcBuilders.standaloneSetup(this.taskController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(
                        "[{\"id\":3,\"title\":\"title\",\"description\":\"desc\",\"deadline\":[2022,9,19"
                                + ",19,9],\"status\":\"BACKLOG\"}]"));
    }

    @Test
    public void getTask() throws Exception {
        AppUser appUser = AppUser.builder().email("vusalra@code.edu.az").password("A333333")
                .role("admin").id(3L).tasks(new ArrayList<>()).build();
        Task task = Task.builder().id(3L).title("title").status(Status.BACKLOG).description("desc").appUsers(appUser)
                .deadline(LocalDateTime.of(2022, 9, 19, 19, 9)).build();
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);
        when(this.taskService.findAllByAppUser((AppUser) any())).thenReturn(taskList);
        when(this.eTaskifyService.getAppUserFromToken()).thenReturn(appUser);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/task");
        MockMvcBuilders.standaloneSetup(this.taskController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[{\"id\":3,\"title\":\"title\",\"description\":\"desc\",\"deadline\":[2022,9,19,19,9],\"status\":\"BACKLOG\"}]"));
    }

}

