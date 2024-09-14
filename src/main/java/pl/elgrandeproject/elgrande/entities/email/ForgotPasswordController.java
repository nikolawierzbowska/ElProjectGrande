package pl.elgrandeproject.elgrande.entities.email;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.elgrandeproject.elgrande.entities.user.UserClass;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;
import pl.elgrandeproject.elgrande.entities.user.exception.UserNotFoundException;
import pl.elgrandeproject.elgrande.registration.email.EmailService;
import pl.elgrandeproject.elgrande.registration.email.MailBody;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/forgotPassword")
public class ForgotPasswordController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private  final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordController(UserRepository userRepository, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email){
        UserClass userClass = userRepository.findByEmail(email)
                .orElseThrow( ()-> new UserNotFoundException("Please provide an valid email"));

        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for your Forgot Passwordrequest : " + otp)
                .subject("OTP for Forgot Password request")
                .build();

        System.out.println("Sending OTP to: " + email);
        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(Date.from(Instant.now().plus(70, ChronoUnit.SECONDS)))
                .users(userClass)
                .build();

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);

        return ResponseEntity.ok("Email sent for verivication");
    }
    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp( @PathVariable Integer otp,  @PathVariable String email){
        UserClass userClass = userRepository.findByEmail(email)
                .orElseThrow( ()-> new UserNotFoundException("Please provide an valid email"));

        ForgotPassword forgotPassword = forgotPasswordRepository.findByOtpAndUser(otp, userClass)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

        if(forgotPassword.getExpirationTime().before(new Date())){
            forgotPasswordRepository.deleteById(forgotPassword.getFpid());
            return new ResponseEntity<>("OTP  has expire", HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok("OTP verified");

    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword ,@PathVariable String email){

        if(!changePassword.password().equals(changePassword.repeatPassword())){
            return new ResponseEntity<>("Please enter the password again!", HttpStatus.EXPECTATION_FAILED);
        }
        String encodePassword = passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(email, encodePassword);

    return ResponseEntity.ok("Password has changed!");


    }

    private  Integer otpGenerator(){
        Random random = new Random();
        return random.nextInt(100_000, 999_999);

    }
}
