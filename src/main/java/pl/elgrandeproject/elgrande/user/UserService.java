package pl.elgrandeproject.elgrande.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.role.RoleRepository;
import pl.elgrandeproject.elgrande.security.UserInfoDetails;
import pl.elgrandeproject.elgrande.user.dto.NewUserDto;
import pl.elgrandeproject.elgrande.user.dto.UserDto;
import pl.elgrandeproject.elgrande.user.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {


    private UserRepository userRepository;
    private UserMapper userMapper;
     private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
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

    public UserDto registerUser(NewUserDto newUserDto) {
        UserClass saveUser = new UserClass();

        if (newUserDto.getPassword().equals(newUserDto.getRepeatedPassword())) {
            newUserDto.setPassword(passwordEncoder.encode((newUserDto.getPassword())));
            newUserDto.setRepeatedPassword(newUserDto.getPassword());
            saveUser = userRepository.save(userMapper.mapNewDtoToEntity(newUserDto));

           return  userMapper.mapEntityToDto(saveUser);
        }
     return null;
    }


    public void softDeleteUser(UUID id) {
        UserClass user = userRepository.findOneById(id)
                .orElseThrow(() -> getUserNotFoundException(id));

        clearSoftData(user);
    }

    private void clearSoftData(UserClass user) {
        int firstNameLength = user.getFirstName().length();
        user.setFirstName("*".repeat(firstNameLength));

        int lastNameLength = user.getLastName().length();
        user.setLastName("*".repeat(lastNameLength));

        int atCharPos = user.getEmail().indexOf('@');
        String randomPref = "deleted-" + UUID.randomUUID();
        user.setEmail(randomPref + user.getEmail().substring(atCharPos));

        int passwordLength = user.getPassword().length();
        user.setPassword("*".repeat(passwordLength));

        int repeatedPasswordLength = user.getRepeatedPassword().length();
        user.setRepeatedPassword("*".repeat(repeatedPasswordLength));
    }

    private UserNotFoundException getUserNotFoundException(String email) {
        return new UserNotFoundException("User with this email " + email + " not exist");
    }

    private UserNotFoundException getUserNotFoundException(UUID id) {
        return new UserNotFoundException("User with this " + id + "  not exist");
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        UserClass user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("not exist"));
//        return new User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
        Optional<UserClass> userinfo = userRepository.findByEmail(email);
        return userinfo.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("not exist " + email));
    }

}
