package pl.elgrandeproject.elgrande.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;
import pl.elgrandeproject.elgrande.registration.UserInfoDetails;

@Service
public class UserInfoDetailsService  {

    private UserRepository userRepository;

    public UserInfoDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                return userRepository.findByEmail(email)
                        .map(UserInfoDetails::new)
                        .orElseThrow(() -> new UsernameNotFoundException("not exist " + email));
            }
        };
    }

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        return userRepository.findByEmail(email)
//                .map(UserInfoDetails::new)
//                .orElseThrow(() -> new UsernameNotFoundException("not exist " + email));
//    }


}
