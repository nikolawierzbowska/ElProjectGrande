package pl.elgrandeproject.elgrande.entities.opinion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, UUID> {

    @Query("SELECT o FROM Opinion o  LEFT JOIN FETCH o.courses WHERE  o.courses.id = :id")
    List<Opinion> findOpinion(UUID id);

    @Query("SELECT o FROM Opinion o WHERE  o.courses.id = :courseId AND o.id = :opinionId")
    Optional<Opinion> findOneById(UUID courseId, UUID opinionId);

}
