package pl.elgrandeproject.elgrande.entities.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserClass, UUID> {

    Optional<UserClass> findByEmail(String email);

    @Query("SELECT us FROM UserClass us LEFT JOIN FETCH us.roles WHERE us.id = :id AND  us.email NOT LIKE 'deleted-%'")
    Optional<UserClass> findOneById(UUID id);

    @Query("SELECT user FROM UserClass user LEFT JOIN FETCH user.roles  WHERE user.email NOT LIKE 'deleted-%'")
    List<UserClass> findAllBy();

    @Transactional
    @Modifying
    @Query("UPDATE UserClass user SET user.password=:password, user.repeatedPassword = :password  WHERE user.email= :email")
    void updatePassword(@Param("email") String email, @Param("password") String password);

}
