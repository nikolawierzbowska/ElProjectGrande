package pl.elgrandeproject.elgrande.registration;

import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import pl.elgrandeproject.elgrande.entities.user.dto.NewUserDto;
import pl.elgrandeproject.elgrande.entities.user.dto.UserDto;
import pl.elgrandeproject.elgrande.security.jwt.JwtAuthenticationResponse;
import pl.elgrandeproject.elgrande.security.jwt.JwtService;
import pl.elgrandeproject.elgrande.security.jwt.RefreshTokenRequest;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
//    private ApplicationEventPublisher publisher;
//    private VerificationTokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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
        return authenticationService.login(loginUser);
    }
    @PostMapping("/refresh")
    public JwtAuthenticationResponse refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return  authenticationService.refreshToken(refreshTokenRequest);
    }

}
