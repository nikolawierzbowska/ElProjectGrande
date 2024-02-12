package pl.elgrandeproject.elgrande.registration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.entities.role.Role;
import pl.elgrandeproject.elgrande.entities.role.RoleRepository;
import pl.elgrandeproject.elgrande.entities.user.UserClass;
import pl.elgrandeproject.elgrande.entities.user.UserMapper;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;
import pl.elgrandeproject.elgrande.entities.user.dto.NewUserDto;
import pl.elgrandeproject.elgrande.entities.user.dto.UserDto;
import pl.elgrandeproject.elgrande.entities.user.exception.UserNotFoundException;
import pl.elgrandeproject.elgrande.registration.exception.PasswordsNotMatchException;
import pl.elgrandeproject.elgrande.security.jwt.JwtAuthenticationResponse;
import pl.elgrandeproject.elgrande.security.jwt.JwtService;
import pl.elgrandeproject.elgrande.security.jwt.RefreshTokenRequest;

import java.util.HashMap;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public UserDto registerUser(NewUserDto newUserDto) {
        UserClass user = new UserClass();

        if (newUserDto.getPassword().equals(newUserDto.getRepeatedPassword())) {
            user.setFirstName(newUserDto.getFirstName());
            user.setLastName(newUserDto.getLastName());
            user.setEmail(newUserDto.getEmail());
            user.setPassword(passwordEncoder.encode((newUserDto.getPassword())));
            user.setRepeatedPassword(passwordEncoder.encode((newUserDto.getRepeatedPassword())));


            Role role = roleRepository.findByName("USER").orElseThrow(
                    () -> new RuntimeException("not exist this role"));
            user.addRole(role);
            role.assignUser(user);

            UserClass savedUser = userRepository.save(user);
            UserDto userDto = userMapper.mapEntityToDto(savedUser);
            return userDto;
        }

        throw new PasswordsNotMatchException("Passwords do not match! ");

    }

    public JwtAuthenticationResponse login(LoginUser loginUser) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getEmail(),
                loginUser.getPassword()));


        UserClass userClass = userRepository.findByEmail(loginUser.getEmail())
                .orElseThrow(() -> getUserNotFoundException());
        UserDetails user = new UserInfoDetails(userClass);


        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }


    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String email  = jwtService.extractUserName(refreshTokenRequest.getToken());
        UserClass userClass = userRepository.findByEmail(email)
                .orElseThrow(() -> getUserNotFoundException());
        UserDetails user = new UserInfoDetails(userClass);

        if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }
        return  null;
    }

    private UserNotFoundException getUserNotFoundException() {
        return new UserNotFoundException("Invalid email or password");
    }
}
