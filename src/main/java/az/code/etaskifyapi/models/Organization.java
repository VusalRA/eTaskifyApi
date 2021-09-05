package az.code.etaskifyapi.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String address;
    private String email;

}
