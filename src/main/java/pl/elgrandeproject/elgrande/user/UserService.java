package pl.elgrandeproject.elgrande.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.role.RoleRepository;
import pl.elgrandeproject.elgrande.user.dto.NewUserDto;
import pl.elgrandeproject.elgrande.user.dto.UserDto;
import pl.elgrandeproject.elgrande.user.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

//    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,RoleRepository roleRepository) {
//        this.userRepository = userRepository;
//        this.userMapper = userMapper;
//        this.passwordEncoder = passwordEncoder;
//        this.roleRepository = roleRepository;
//    }

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

    public UserDto saveNewUser(NewUserDto newUserDto) {
        UserClass saveUser = new UserClass();

//        Role roles = roleRepository.findByName("USER").get();
//        saveUser.setRoles(Collections.singleton(roles));

        if (newUserDto.getPassword().equals(newUserDto.getRepeatedPassword())) {
            newUserDto.setPassword(passwordEncoder.encode((newUserDto.getPassword())));
            newUserDto.setRepeatedPassword(newUserDto.getPassword());
            saveUser = userRepository.save(userMapper.mapNewDtoToEntity(newUserDto));

           return  userMapper.mapEntityToDto(saveUser);

        }
     return null;
    }

//    public ResponseEntity<String> login(NewUserDto userDto) {
//        System.out.println(userDto.getEmail());
//        System.out.println(userDto.getPassword());
//
//
//        Authentication authentication = authenticationManager
//                .authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(),
//                        userDto.getPassword()));
//
//
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return new ResponseEntity<>("User signed success", HttpStatus.OK);
//    }


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

//    private Collection<GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
//        return roles.stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toList());
//    }
}
