package pl.elgrandeproject.elgrande.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.user.dto.NewUserDto;
import pl.elgrandeproject.elgrande.user.dto.UserDto;
import pl.elgrandeproject.elgrande.user.exception.UserNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
    }

    public List<UserDto> getUsers() {
        return userRepository.findAllBy().stream()
                .map(entity -> userMapper.mapEntityToDto(entity))
                .toList();
    }

    public UserDto getUserById(UUID id) {
        return userRepository.findOneById(id)
                .map(entity -> userMapper.mapEntityToDto(entity))
                .orElseThrow(() -> getUserNotFoundException(id));
    }

    public UserDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(entity -> userMapper.mapEntityToDto(entity))
                .orElseThrow(() -> getUserNotFoundException(email));
    }

    public ResponseEntity<String> saveNewUser(NewUserDto newUserDto) {
        UserClass saveUser = new UserClass();

        Role roles = roleRepository.findByName("USER").get();
        saveUser.setRoles(Collections.singleton(roles));

        if (newUserDto.getPassword().equals(newUserDto.getRepeatedPassword())) {
            newUserDto.setPassword(passwordEncoder.encode((newUserDto.getPassword())));
            newUserDto.setRepeatedPassword(newUserDto.getPassword());
            saveUser = userRepository.save(userMapper.mapNewDtoToEntity(newUserDto));

            userMapper.mapEntityToDto(saveUser);
            return new ResponseEntity<>("User registered success", HttpStatus.OK);
        }
        return new ResponseEntity<>("The passwords are not the same", HttpStatus.BAD_REQUEST);
    }


    private UserNotFoundException getUserNotFoundException(String email) {
        return new UserNotFoundException("User with this email " + email + " not exist");
    }

    private UserNotFoundException getUserNotFoundException(UUID id) {
        return new UserNotFoundException("User with this " + id + "  not exist");
    }


}
