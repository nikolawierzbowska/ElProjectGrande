package pl.elgrandeproject.elgrande.user;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.elgrandeproject.elgrande.user.dto.NewUserDto;
import pl.elgrandeproject.elgrande.user.dto.UserDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public UserDto getUserById(@PathVariable UUID id){
       return userService.getUserById(id);
    }

    @GetMapping("/users/by-{email}")
    public UserDto getUserByEmail(@PathVariable String email){
        return  userService.getUserByEmail(email);
    }

    @PostMapping("/register")
    public ResponseEntity<String> createNewUser(@Valid @RequestBody NewUserDto newUserDto) {
        return userService.saveNewUser(newUserDto);
    }
}
