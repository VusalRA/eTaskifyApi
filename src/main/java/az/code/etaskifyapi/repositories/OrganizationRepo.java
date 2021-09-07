package az.code.etaskifyapi.repositories;

import az.code.etaskifyapi.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepo extends JpaRepository<Organization, Long> {
}
