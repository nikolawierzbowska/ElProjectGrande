package pl.elgrandeproject.elgrande.entities.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserClass, UUID> {

    Optional<UserClass> findByEmail(String email);

    @Query("SELECT us FROM UserClass us LEFT JOIN FETCH us.roles WHERE us.id = :id AND  us.email NOT LIKE 'deleted-%'")
    Optional<UserClass> findOneById(UUID id);

    @Query("SELECT user FROM UserClass user LEFT JOIN FETCH user.roles  WHERE user.email NOT LIKE 'deleted-%'")
    List<UserClass> findAllBy();

}
