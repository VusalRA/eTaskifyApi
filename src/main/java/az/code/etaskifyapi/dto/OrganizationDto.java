package az.code.etaskifyapi.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrganizationDto {

    private String email;
    private String password;
    private String name;
    private String username;
    private String phoneNumber;
    private String address;

}
