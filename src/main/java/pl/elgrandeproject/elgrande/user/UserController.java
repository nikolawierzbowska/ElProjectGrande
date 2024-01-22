package pl.elgrandeproject.elgrande.user;

import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import pl.elgrandeproject.elgrande.user.dto.NewUserDto;
import pl.elgrandeproject.elgrande.user.dto.UserDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private  JwtService jwtService;



    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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
    public UserDto createNewUser(@Valid @RequestBody NewUserDto newUserDto) {
        return userService.saveNewUser(newUserDto);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody NewUserDto userDto){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(),
                userDto.getPassword()));


        if(authenticate.isAuthenticated()){
            return jwtService.generateToken(userDto.getEmail());

        }else {
            throw new UsernameNotFoundException("invalid user request");
        }
    }




}
