package pl.elgrandeproject.elgrande.entities.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository  extends JpaRepository<Course, UUID> {
    List<Course> findAll();

    @Query("SELECT c FROM Course c LEFT  JOIN  FETCH  c.opinions WHERE c.id = :id ")
    Optional<Course> findOneById(UUID id);





}
