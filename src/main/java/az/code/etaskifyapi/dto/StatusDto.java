package az.code.etaskifyapi.dto;

import az.code.etaskifyapi.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusDto {

    @Enumerated(EnumType.STRING)
    private Status status;

}
