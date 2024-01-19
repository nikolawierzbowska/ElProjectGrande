package pl.elgrandeproject.elgrande.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserClass {

    @Id
    private UUID id= UUID.randomUUID();
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

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles = new HashSet<>();





}
