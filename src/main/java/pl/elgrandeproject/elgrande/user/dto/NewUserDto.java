package pl.elgrandeproject.elgrande.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pl.elgrandeproject.elgrande.user.validation.Unoccupied;

@Getter
@Setter
public class NewUserDto {
        @NotBlank(message = "The field can not be empty.")
        String firstName;
        @NotBlank(message = "The field can not be empty.")
        String lastName;
        @Email(message = "This is not a correct email")
        @NotBlank(message = "The field can not be empty.")
        @Unoccupied
        String email;
        @NotBlank(message = "The field can not be empty.")
        @Size(min = 10, message = "The password must be 10 characters minimum")
        String password;
        @NotBlank(message = "The field can not be empty.")
        String repeatedPassword;

        public NewUserDto(String firstName, String lastName, String email, String password, String repeatedPassword) {
                this.firstName = firstName;
                this.lastName = lastName;
                this.email = email;
                this.password = password;
                this.repeatedPassword = repeatedPassword;
        }
}
