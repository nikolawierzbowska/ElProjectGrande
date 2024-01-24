package pl.elgrandeproject.elgrande.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.elgrandeproject.elgrande.registration.UserInfoDetailsService;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private UserInfoDetailsService userInfoDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserInfoDetailsService userInfoDetailsService) {
        this.jwtService = jwtService;
        this.userInfoDetailsService = userInfoDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt;
        String email;

        if (StringUtils.isEmpty(authHeader) || !org.apache.commons.lang3.StringUtils.startsWith
                (authHeader,"Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        email = jwtService.extractUserName(jwt);


        if (StringUtils.isEmpty(email) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userInfoDetailsService.userDetailsService().loadUserByUsername(email);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                securityContext.setAuthentication(authToken);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        filterChain.doFilter(request, response);

    }
}
