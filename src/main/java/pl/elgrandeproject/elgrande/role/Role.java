package pl.elgrandeproject.elgrande.role;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.elgrandeproject.elgrande.user.UserClass;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name ="roles")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Role {

    @Id
    @EqualsAndHashCode.Include
    private Integer id;
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<UserClass> users = new HashSet<>();

    public Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public void  assignUser(UserClass user){
        users.add(user);
    }
}
