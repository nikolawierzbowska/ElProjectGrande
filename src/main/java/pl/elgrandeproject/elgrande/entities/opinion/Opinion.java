package pl.elgrandeproject.elgrande.entities.opinion;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.elgrandeproject.elgrande.entities.course.Course;
import pl.elgrandeproject.elgrande.entities.user.UserClass;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "opinions")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Opinion {

    @Id
    @EqualsAndHashCode.Include
    private UUID id = UUID.randomUUID();

    @NotBlank(message = "The field can not be empty.")
    @Size(max = 120, message = "The opinion can not be longer than 120 characters")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserClass users;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course courses;

    public Opinion(String description) {
        this.description = description;
    }

    public void addUser(UserClass user) {
        this.users = user;
    }

    public void setUser(UserClass user) {
        this.users = user;
    }


}
