package az.code.etaskifyapi.services;

import az.code.etaskifyapi.dto.AssignDto;
import az.code.etaskifyapi.dto.StatusDto;
import az.code.etaskifyapi.dto.TaskDto;
import az.code.etaskifyapi.enums.Status;
import az.code.etaskifyapi.exceptions.DeadlineException;
import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Task;
import az.code.etaskifyapi.models.User;
import az.code.etaskifyapi.repositories.AppUserRepo;
import az.code.etaskifyapi.repositories.TaskRepo;
import az.code.etaskifyapi.repositories.UserRepo;
import az.code.etaskifyapi.services.email.EmailService;
import az.code.etaskifyapi.services.interfaces.TaskService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepo taskRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private AppUserRepo appUserRepo;
    @Mock
    private EmailService emailService;
    private TaskService underTest;


    @BeforeEach
    void setUp() {
        underTest = new TaskServiceImpl(taskRepo, userRepo, appUserRepo, emailService);
    }

    @Test
    void save() {
        AppUser appUser = AppUser.builder().email("vusalra@code.edu.az").password("A333333").role("admin").build();
        TaskDto taskDto = new TaskDto();
        taskDto.setDeadline("2021-09-19 03:03");
        taskDto.setDescription("Desc");
        taskDto.setTitle("Title");
        underTest.save(taskDto, appUser);
        ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepo).save(taskArgumentCaptor.capture());
        Task capturedTask = taskArgumentCaptor.getValue();
        assertThat(capturedTask.getTitle()).isEqualTo(taskDto.getTitle());
    }

    @Test
    public void findAllByAppUser() {
        when(userRepo.findByAppUserOrganization_id((Long) any())).thenThrow(new DeadlineException());
        TaskServiceImpl taskServiceImpl = new TaskServiceImpl(taskRepo, userRepo, appUserRepo, new EmailService());
        AppUser appUser = AppUser.builder().email("vusalra@code.edu.az").password("A333333")
                .role("admin").id(3L).tasks(new ArrayList<>()).build();
        assertThrows(DeadlineException.class, () -> taskServiceImpl.findAllByAppUser(appUser));
        verify(userRepo).findByAppUserOrganization_id((Long) any());
    }

    @Test
    void findTaskById() {
        Task task = Task.builder().title("title").status(Status.BACKLOG).description("desc").build();
        when(taskRepo.findById(3L)).thenReturn(Optional.of(task));
        assertThat(task.getTitle()).isEqualTo(underTest.findTaskById(3L).getTitle());
    }

    @Test
    void deleteTask() {
        Task task = Task.builder().title("title").status(Status.BACKLOG).description("desc").build();
        Optional<Task> optionalEntityType = Optional.of(task);
        Mockito.when(taskRepo.findById(3L)).thenReturn(optionalEntityType);
        underTest.deleteTask(taskRepo.findById(3L).get());
        Mockito.verify(taskRepo, times(1)).delete(task);
    }


    @Test
    void changeStatus() {
        Task task = Task.builder().title("title").status(Status.BACKLOG).description("desc").build();
        when(taskRepo.findById(3L)).thenReturn(Optional.of(task));
        StatusDto statusDto = new StatusDto();
        statusDto.setStatus(Status.DONE);
        underTest.changeStatus(3L, statusDto);
        assertThat(underTest.findTaskById(3L).getStatus()).isEqualTo(Status.DONE);
    }


    @Test
    public void assign() {
        AppUser appUser = AppUser.builder().email("vusalra@code.edu.az").password("A333333").role("admin").tasks(new ArrayList<Task>()).build();
        Task task = Task.builder().description("desc").status(Status.BACKLOG).title("title").deadline(LocalDateTime.now()).appUsers(appUser).build();
        when(taskRepo.findById((Long) any())).thenReturn(Optional.of(task));
        when(appUserRepo.findByName((String) any())).thenThrow(new DeadlineException());
        TaskServiceImpl taskServiceImpl = new TaskServiceImpl(taskRepo, userRepo, appUserRepo, new EmailService());
        assertThrows(Exception.class, () -> taskServiceImpl.assign(new AssignDto()));
        verify(taskRepo).findById((Long) any());
        verify(appUserRepo).findByName((String) any());
    }

}