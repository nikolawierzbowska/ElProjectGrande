package pl.elgrandeproject.elgrande.entities.user;

import org.springframework.web.bind.annotation.*;
import pl.elgrandeproject.elgrande.entities.user.dto.UserDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
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

    @DeleteMapping("/users/{id}")
    public void softDeleteUser(@PathVariable UUID id ){
        userService.softDeleteUser(id);
    }

}
