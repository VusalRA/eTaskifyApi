package az.code.etaskifyapi.controllers;

import az.code.etaskifyapi.dto.AssignDto;
import az.code.etaskifyapi.dto.StatusDto;
import az.code.etaskifyapi.dto.TaskDto;
import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Task;
import az.code.etaskifyapi.services.interfaces.ETaskifyService;
import az.code.etaskifyapi.services.interfaces.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TaskController {
    private ETaskifyService eTaskifyService;
    private TaskService taskService;

    public TaskController(ETaskifyService eTaskifyService, TaskService taskService) {
        this.eTaskifyService = eTaskifyService;
        this.taskService = taskService;
    }

    @PostMapping("/task")
    public ResponseEntity<Task> saveTask(@RequestBody TaskDto taskDto) {
        AppUser appUser = eTaskifyService.findOne(eTaskifyService.getAppUserFromToken().getEmail());
        return ResponseEntity.ok(taskService.save(taskDto, appUser));
    }

    @GetMapping("/task")
    public ResponseEntity<?> getTask() {
        AppUser appUser = eTaskifyService.getAppUserFromToken();
        return ResponseEntity.ok(taskService.findAllByAppUser(appUser));
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<Task> findTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findTaskById(id));
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<Task> deleteTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteTask(taskService.findTaskById(id)));
    }

    @PatchMapping("/task/{id}")
    public ResponseEntity<Task> changeStatus(@PathVariable Long id, @RequestBody StatusDto statusDto) {
        return ResponseEntity.ok(taskService.changeStatus(id, statusDto));
    }

    @PostMapping("/assign")
    public ResponseEntity<Task> edit(@RequestBody AssignDto assignDto) {
        return ResponseEntity.ok(taskService.assign(assignDto));
    }

    @GetMapping("/assignments")
    public ResponseEntity<List<Task>> getAssignList() {
        AppUser appUser = eTaskifyService.getAppUserFromToken();
        return ResponseEntity.ok(taskService.getAssignments(appUser));
    }

}
