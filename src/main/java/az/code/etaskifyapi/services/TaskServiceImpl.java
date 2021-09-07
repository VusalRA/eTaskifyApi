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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepo taskRepo;
    private UserRepo userRepo;
    private AppUserRepo appUserRepo;
    private EmailService emailService;

    @Value("${message}")
    private String message;

    public TaskServiceImpl(TaskRepo taskRepo, UserRepo userRepo, AppUserRepo appUserRepo, EmailService emailService) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.appUserRepo = appUserRepo;
        this.emailService = emailService;
    }

    @Override
    public Task save(TaskDto taskDto, AppUser appUser) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = null;
        try {
            dateTime = LocalDateTime.parse(taskDto.getDeadline(), formatter);
            if (LocalDateTime.now().isAfter(dateTime)) {
                throw new DeadlineException();
            }
        } catch (Exception e) {
            throw new DeadlineException();
        }

        return taskRepo.save(Task.builder().status(Status.BACKLOG).title(taskDto.getTitle())
                .description(taskDto.getDescription()).deadline(dateTime).appUsers(appUser).build());
    }

    @Override
    public List<Task> findAllByAppUser(AppUser appUser) {
        List<Task> tasks = new ArrayList<>();
        if (appUser.getRole().equals("user")) {
            User user = userRepo.findByAppUser(appUser);
            tasks = getTasksForUser(user.getAppUserOrganization_id(), tasks);
        } else {
            tasks = getTasksForUser(appUser.getId(), tasks);
        }
        return tasks;
    }

    @Override
    public Task findTaskById(Long id) {
        return taskRepo.findById(id).get();
    }

    @Override
    public Task deleteTask(Task task) {
        taskRepo.delete(task);
        return task;
    }

    @Override
    public Task changeStatus(Long id, StatusDto statusDto) {
        Task task = taskRepo.findById(id).get();
        task.setStatus(statusDto.getStatus());
        return taskRepo.save(task);
    }

    @Override
    public Task assign(AssignDto assignDto) {
        Task task = taskRepo.findById(assignDto.getTaskId()).get();
        task.getAssigned().add(appUserRepo.findByName(assignDto.getEmail()));
        emailService.send(assignDto.getEmail(), message + task.getTitle());
        return taskRepo.save(task);
    }

    @Override
    public List<Task> getAssignments(AppUser appUser) {
        return taskRepo.findTaskByAssigned(appUser);
    }

    public List<Task> getTasksForUser(Long id, List<Task> tasks) {
        List<User> users = userRepo.findByAppUserOrganization_id(id);
        tasks.addAll(taskRepo.findTaskByAppUsers(appUserRepo.findById(users.get(0).getAppUser().getId()).get()));
        return tasks;
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void checkDeadline() {
        List<Task> tasks = taskRepo.findAll();
        tasks.forEach(task -> {
            if (LocalDateTime.now().isAfter(task.getDeadline())) {
                task.setStatus(Status.EXPIRED);
                taskRepo.save(task);
            }
        });
    }

}
