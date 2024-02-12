package pl.elgrandeproject.elgrande.entities.opinion;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.elgrandeproject.elgrande.entities.course.Course;

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
    @Size(max = 180, message = "The opinion can not be longer than 180 characters")
    private String description;

    private String userName;

    @ManyToOne(fetch = FetchType.EAGER)
    private Course courses;

    public Opinion(String description, String userName) {
        this.description = description;
        this.userName =userName;
    }


}
