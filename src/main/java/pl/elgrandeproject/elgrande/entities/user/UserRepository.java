package pl.elgrandeproject.elgrande.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserClass, UUID> {

    Optional<UserClass> findByEmail(String email);

    @Query("SELECT us FROM UserClass us LEFT JOIN FETCH us.roles WHERE us.id = :id")
    Optional<UserClass> findOneById(UUID id);

    @Query("SELECT user FROM UserClass user LEFT JOIN FETCH user.roles")
    List<UserClass> findAllBy();

}