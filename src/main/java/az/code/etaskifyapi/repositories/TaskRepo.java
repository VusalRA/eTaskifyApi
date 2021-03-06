package az.code.etaskifyapi.repositories;

import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long> {

    List<Task> findTaskByAppUsers(AppUser appUser);

    List<Task> findTaskByAssigned(AppUser appUser);

}
