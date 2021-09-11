package az.code.etaskifyapi.repositories;

import az.code.etaskifyapi.models.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class OrganizationRepoTest {

    @Autowired
    private OrganizationRepo underTest;

    @Test
    void findByName() {
        //Given
        String name = "Vusal";
        Organization organization = Organization.builder().name(name).email("vusalra@code.edu.az")
                .address("Baku").phoneNumber("0776090606").username("abdullayev").build();
        String result = underTest.save(organization).getName();
        //When
        String expected = underTest.findById(organization.getId()).get().getName();
        //Then
        assertThat(result).isEqualTo(expected);

    }

}