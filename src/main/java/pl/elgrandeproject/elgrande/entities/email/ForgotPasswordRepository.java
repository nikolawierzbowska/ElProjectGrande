package pl.elgrandeproject.elgrande.entities.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.elgrandeproject.elgrande.entities.user.UserClass;

import java.util.Optional;
import java.util.UUID;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {

    @Query("select fp from ForgotPassword fp where fp.otp =:otp and fp.users = :user")
    Optional<ForgotPassword> findByOtpAndUser(int otp , UserClass user);

    @Query("DELETE  ForgotPassword fp where fp.fpid=:fpid ")
    void deleteById(UUID fpid);
}

