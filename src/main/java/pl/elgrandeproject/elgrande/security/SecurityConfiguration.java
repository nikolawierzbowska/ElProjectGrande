package pl.elgrandeproject.elgrande.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.elgrandeproject.elgrande.registration.UserInfoDetailsService;
import pl.elgrandeproject.elgrande.security.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserInfoDetailsService userInfoDetailsService;




    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, UserInfoDetailsService userInfoDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userInfoDetailsService = userInfoDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/courses/{courseId}/opinions").permitAll()

                        .requestMatchers("/api/v1/admin/roles/**",
                                "/api/v1/admin/courses/**",
                                "/api/v1/admin/users/**",
                                "/api/v1/admin/courses/{courseId}/opinions",
                                "/api/v1/courses/{courseId}/opinions/{opinionId}")
                        .hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/courses/{courseId}/opinions/{opinionId}")
                        .hasAnyRole("ADMIN")

                        .requestMatchers("/api/v1/user/**",
                                "/api/v1/courses/{courseId}/opinions",
                                "/api/v1/courses/{courseId}/opinions/{opinionId}")
                        .hasAnyRole("USER")

                        .anyRequest().authenticated())

                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))

//                .exceptionHandling(eh -> eh.authenticationEntryPoint(authenticationEntryPoint))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userInfoDetailsService.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
    }
}

