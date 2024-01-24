package pl.elgrandeproject.elgrande.registration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.elgrandeproject.elgrande.security.jwt.JwtTokenService;
import pl.elgrandeproject.elgrande.user.dto.NewUserDto;
import pl.elgrandeproject.elgrande.user.dto.UserDto;

@RestController
@RequestMapping("/api/v1")
public class RegistrationController {

    private AuthenticationService authenticationService;
    private ApplicationEventPublisher publisher;
//    private VerificationTokenService tokenService;
    private AuthenticationManager authenticationManager;
    private JwtTokenService jwtTokenService;

    public RegistrationController(AuthenticationService authenticationService, ApplicationEventPublisher publisher,AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.authenticationService = authenticationService;
        this.publisher = publisher;
//        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/register")
    public UserDto createNewUser(@Valid @RequestBody NewUserDto newUserDto, HttpServletRequest request) {
        UserDto user = authenticationService.registerUser(newUserDto);
//        publisher.publishEvent(new RegistrationCompleteEvent(user,
//                UrlUtil.getApplicationUrl(request)));

        return user;
    }

//    public  String verifyEmail(@RequestParam("token") String token){
//       Optional<VerificationToken> theToken= tokenService.findByToken(token);
//        if(theToken.isPresent() && theToken.get().getUser().)
//    }
}
