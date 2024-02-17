package pl.elgrandeproject.elgrande.entities.user;

import org.springframework.web.bind.annotation.*;
import pl.elgrandeproject.elgrande.entities.user.dto.UserDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable UUID userId){
       return userService.getUserById(userId);
    }

    @GetMapping("/by-{email}")
    public UserDto getUserByEmail(@PathVariable String email){
        return  userService.getUserByEmail(email);
    }

    @DeleteMapping("/{userId}")
    public void softDeleteUser(@PathVariable UUID userId ){
        userService.softDeleteUser(userId);
    }
}
