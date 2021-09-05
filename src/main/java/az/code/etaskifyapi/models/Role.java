package az.code.etaskifyapi.models;

import lombok.*;
import javax.persistence.*;

@Entity
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;

}
