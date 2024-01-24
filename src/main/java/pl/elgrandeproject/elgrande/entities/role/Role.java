package pl.elgrandeproject.elgrande.entities.role;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.elgrandeproject.elgrande.entities.user.UserClass;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name ="roles")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @EqualsAndHashCode.Include
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<UserClass> users = new HashSet<>();

    public Role(String name) {
        this.name = name;
    }

    public void  assignUser(UserClass user){
        users.add(user);
    }
}
