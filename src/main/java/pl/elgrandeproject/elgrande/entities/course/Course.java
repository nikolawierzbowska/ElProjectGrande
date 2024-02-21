package pl.elgrandeproject.elgrande.entities.course;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.elgrandeproject.elgrande.entities.opinion.Opinion;

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
    @NotNull
    @Size(max = 50, message = "The name can not be longer than 50 characters")
    private String name;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "courses")
//            cascade = CascadeType.ALL)
//            orphanRemoval = true)
    private List<Opinion> opinions = new ArrayList<>();

    public Course(@NotNull String name) {
        this.name = name;
    }

    public void addOpinion(Opinion opinion){
        opinion.setCourses(this);
        opinions.add(opinion);
    }

    public void removeOpinion(Opinion opinion){
        opinions.remove(opinion);
    }
}
