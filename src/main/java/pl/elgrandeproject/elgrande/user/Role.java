package pl.elgrandeproject.elgrande.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Role {

    @Id
    @EqualsAndHashCode.Include
    private UUID id = UUID.randomUUID();
    private String typeOfRole;

    @ManyToMany(mappedBy = "roles")
    private Set<UserClass> users = new HashSet<>();


}
