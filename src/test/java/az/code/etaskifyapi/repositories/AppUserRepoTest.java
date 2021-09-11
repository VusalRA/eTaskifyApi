package az.code.etaskifyapi.repositories;

import az.code.etaskifyapi.models.AppUser;
import az.code.etaskifyapi.models.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AppUserRepoTest {

    @Autowired
    private AppUserRepo underTest;

    @Test
    void findByName() {
        //Given
        String email = "vusalra@code.edu.az";
        List<Task> list = new ArrayList<>();
        AppUser appUser = AppUser.builder().password("test").email("vusalra@code.edu.az").role("admin").tasks(list).build();
        AppUser result = underTest.save(appUser);
        //When
        AppUser expected = underTest.findByName(email);
        //Then
        assertThat(result).isEqualTo(expected);
    }
}