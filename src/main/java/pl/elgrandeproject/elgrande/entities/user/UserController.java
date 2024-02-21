package pl.elgrandeproject.elgrande.entities.user;

import org.springframework.web.bind.annotation.*;
import pl.elgrandeproject.elgrande.entities.user.dto.UserDto;
import pl.elgrandeproject.elgrande.registration.Principal;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public List<UserDto> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/admin/users/{userId}")
    public UserDto getUserById(@PathVariable UUID userId){
       return userService.getUserById(userId);
    }

    @GetMapping("/users/by-{email}")
    public UserDto getUserByEmail(@PathVariable String email, Principal principal){
        return  userService.getUserByEmail(email, principal);
    }

    @DeleteMapping("/admin/users/{userId}")
    public void softDeleteUser(@PathVariable UUID userId ){
        userService.softDeleteUser(userId);
    }
}
