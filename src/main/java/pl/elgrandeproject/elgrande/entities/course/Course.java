package pl.elgrandeproject.elgrande.entities.course;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.elgrandeproject.elgrande.entities.opinion.Opinion;
import pl.elgrandeproject.elgrande.entities.user.UserClass;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Course {

    @Id
    @EqualsAndHashCode.Include
    private UUID id = UUID.randomUUID();

    @NotBlank(message =  "The field can not be empty.")
    @Size(max = 25, message = "The name can not be longer than 25 characters")
    private String name;

    @OneToMany(
            mappedBy = "courses",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Opinion> opinions = new ArrayList<>();


    public Course(String name) {
        this.name = name;
    }


    public void addOpinion(Opinion opinion, UserClass userClass){
        opinion.setUser(userClass);
        opinions.add(opinion);
    }
}
