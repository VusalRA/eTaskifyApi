package az.code.etaskifyapi.services.interfaces;

import az.code.etaskifyapi.dto.AssignDto;
import az.code.etaskifyapi.dto.StatusDto;
import az.code.etaskifyapi.dto.TaskDto;
import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Task;

import java.util.List;

public interface TaskService {

    Task save(TaskDto taskDto, AppUser appUser);

    List<Task> findAllByAppUser(AppUser appUser);

    Task findTaskById(Long id);

    Task deleteTask(Task task);

    Task changeStatus(Long id, StatusDto statusDto);

    Task assign(AssignDto assignDto);

    List<Task> getAssignments(AppUser appUser);

}
