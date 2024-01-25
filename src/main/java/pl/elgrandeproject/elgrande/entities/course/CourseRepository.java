package pl.elgrandeproject.elgrande.entities.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository  extends JpaRepository<Course, UUID> {
    List<Course> findAll();

    Optional<Course> findOneById(UUID id);





}
