package pl.elgrandeproject.elgrande.registration;

import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.elgrandeproject.elgrande.entities.user.dto.NewUserDto;
import pl.elgrandeproject.elgrande.entities.user.dto.UserDto;
import pl.elgrandeproject.elgrande.security.jwt.JwtAuthenticationResponse;
import pl.elgrandeproject.elgrande.security.jwt.JwtService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private AuthenticationService authenticationService;
//    private ApplicationEventPublisher publisher;
//    private VerificationTokenService tokenService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    public AuthenticationController(AuthenticationService authenticationService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationService = authenticationService;
//        this.publisher = publisher;
//        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/sign-up")
    public UserDto signUp(@Valid @RequestBody NewUserDto newUserDto) {
        UserDto user = authenticationService.registerUser(newUserDto);
//        publisher.publishEvent(new RegistrationCompleteEvent(user,
//                UrlUtil.getApplicationUrl(request)));

        return user;
    }
    @PostMapping("/login")
    public JwtAuthenticationResponse loginUser(@RequestBody LoginUser loginUser){
        return  authenticationService.login(loginUser);
    }

}
