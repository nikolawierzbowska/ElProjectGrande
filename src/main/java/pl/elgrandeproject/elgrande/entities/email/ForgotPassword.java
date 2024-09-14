package pl.elgrandeproject.elgrande.entities.email;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.elgrandeproject.elgrande.entities.user.UserClass;

import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Builder
public class ForgotPassword {
    @Id
    private UUID fpid;
    @Column(nullable = false)
    private int otp;
    @Column(nullable = false)
    private Date expirationTime;
    @JoinColumn(name = "user_id")
    @OneToOne
    private UserClass users;

    @Builder
    public ForgotPassword(UUID fpid, int otp, Date expirationTime, UserClass users) {
        this.fpid = fpid != null ? fpid : UUID.randomUUID();
        this.otp = otp;
        this.expirationTime = expirationTime;
        this.users = users;
    }
}
