package pl.elgrandeproject.elgrande.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.elgrandeproject.elgrande.entities.user.UserRepository;
import pl.elgrandeproject.elgrande.security.jwt.JwtAuthenticationFilter;
import pl.elgrandeproject.elgrande.security.jwt.JwtService;

import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(AuthConfigProperties.class)
@EnableMethodSecurity(jsr250Enabled = true)

public class SecurityConfiguration {
    private static final String[] URL_WHITELIST = {"/api/v1/admin/roles/**", "/api/v1/auth/**", "/error/**", "/swagger-ui/**", "/v3/api-docs/**"};
    private static final String[] URL_ADMIN = {"/error/**", "/api/v1/admin/courses/**",
            "/api/v1/admin/users/**", "/api/v1/admin/courses/{courseId}/opinions", "/api/v1/courses/{courseId}/opinions/{opinionId}"};
    private static final String[] URL_USER = {"/api/v1/user/**", "/api/v1/courses/{courseId}/opinions",
            "/api/v1/courses/{courseId}/opinions/{opinionId}"};

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationEntryPoint authenticationEntryPoint,
                                           JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(URL_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/courses/{courseId}/opinions").permitAll()

                        .requestMatchers(URL_ADMIN).hasAnyRole(ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/v1/courses/{courseId}/opinions")
                        .hasAnyRole(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/courses/{courseId}/opinions/{opinionId}")
                        .hasAnyRole(ADMIN)

                        .requestMatchers(URL_USER)
                        .hasAnyRole(USER)
                        .requestMatchers(HttpMethod.GET, "/api/v1/courses/{courseId}/opinions/{opinionId}")
                        .hasAnyRole(USER)
                        .anyRequest().authenticated())

                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))

                .exceptionHandling(eh -> eh.authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return email -> userRepository.findByEmail(email)
                .map(user -> new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return user.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                                .collect(Collectors.toList());
                    }

                    @Override
                    public String getPassword() {
                        return user.getPassword();
                    }

                    @Override
                    public String getUsername() {
                        return user.getEmail();
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
                })
                .orElseThrow(() -> new UsernameNotFoundException("not exist " + email));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtService, userDetailsService);
    }

    @Bean
    public JwtService jwtService(AuthConfigProperties authConfigProperties) {
        return new JwtService(authConfigProperties);
    }
}

