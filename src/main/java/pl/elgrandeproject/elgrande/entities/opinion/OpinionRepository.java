package pl.elgrandeproject.elgrande.entities.opinion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, UUID> {

    List<Opinion> findAllBy();

    Optional<Opinion> findOneById(UUID id);

}
