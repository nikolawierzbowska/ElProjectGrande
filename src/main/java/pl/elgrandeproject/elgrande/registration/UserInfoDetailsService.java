package pl.elgrandeproject.elgrande.registration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;

@Service
public class UserInfoDetailsService  {

    private final UserRepository userRepository;

    public UserInfoDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return email -> userRepository.findByEmail(email)
                .map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("not exist " + email));
    }
}

