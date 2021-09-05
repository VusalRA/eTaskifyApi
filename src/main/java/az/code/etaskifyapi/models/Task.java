package az.code.etaskifyapi.models;

import az.code.etaskifyapi.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;


@Entity
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")

public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    //    private String deadline;
    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appUser_id")
    private AppUser appUsers;

//    @JsonIgnore
//    @ManyToMany
//    List<AppUser> appUsers;

}
