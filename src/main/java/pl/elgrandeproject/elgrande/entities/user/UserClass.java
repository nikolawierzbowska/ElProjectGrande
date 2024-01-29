package pl.elgrandeproject.elgrande.entities.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.elgrandeproject.elgrande.entities.opinion.Opinion;
import pl.elgrandeproject.elgrande.entities.role.Role;

import java.util.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserClass {

    @Id
    private UUID id = UUID.randomUUID();
    @NotBlank(message = "The field can not be empty.")
    private String firstName;
    @NotBlank(message = "The field can not be empty.")
    private String lastName;
    @Email
    @NotBlank(message = "The field can not be empty.")
    @EqualsAndHashCode.Include
    @Column(unique = true)
    private String email;
    @NotBlank(message = "The field can not be empty.")
    @Size(min = 10, message = "The password must be 10 characters minimum")
    private String password;
    @NotBlank(message = "The field can not be empty.")
    private String repeatedPassword;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(
            mappedBy = "users",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Opinion> opinions = new ArrayList<>();

    public UserClass(String firstName, String lastName, String email, String password, String repeatedPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.repeatedPassword = repeatedPassword;

    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void clearAssignRole() {
        getRoles().clear();
    }

}
