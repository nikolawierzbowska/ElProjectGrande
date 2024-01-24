package pl.elgrandeproject.elgrande.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.elgrandeproject.elgrande.security.UserInfoDetailsService;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private JwtTokenService jwtTokenService;
    private UserInfoDetailsService userInfoDetailsService;

    public JwtFilter(JwtTokenService jwtTokenService, UserInfoDetailsService userInfoDetailsService) {
        this.jwtTokenService = jwtTokenService;
        this.userInfoDetailsService = userInfoDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");
    String token = null;
    String email= null;
    if(authHeader != null && authHeader.startsWith("Bearer")){
        token = authHeader.substring(7);
        email = jwtTokenService.extractEmail(token);

        }
    if(email !=null && SecurityContextHolder.getContext().getAuthentication()==null){
        UserDetails userDetails = userInfoDetailsService.loadUserByUsername(email);
        if(jwtTokenService.validateToken(token, userDetails)){
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }
    filterChain.doFilter(request, response );

    }
}
