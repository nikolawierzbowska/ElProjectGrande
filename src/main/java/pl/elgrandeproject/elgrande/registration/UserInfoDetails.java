package pl.elgrandeproject.elgrande.registration;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.elgrandeproject.elgrande.entities.user.UserClass;

import java.util.Collection;
import java.util.stream.Collectors;


@Data
public class UserInfoDetails implements UserDetails {

    private UserClass userClass;
    public UserInfoDetails(UserClass userClass){
        super();
        this.userClass=userClass;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userClass.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return userClass.getPassword();
    }

    @Override
    public String getUsername() {
        return userClass.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
