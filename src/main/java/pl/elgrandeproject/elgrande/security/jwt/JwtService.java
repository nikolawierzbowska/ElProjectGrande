package pl.elgrandeproject.elgrande.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.elgrandeproject.elgrande.entities.user.UserClass;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    public String generateToken(UserClass userClass) {
        return Jwts.builder()
                .setSubject(userClass.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Map<String,Object> extraClaims, UserClass userClass) {
        return Jwts.builder().setClaims(extraClaims)
                .setSubject(userClass.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60480000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode("413F4428472B4B6413F4428472B4B6413F4428472B4B6413F4428472B4B6");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token ){
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    public  Boolean isTokenValid(String token, UserDetails userDetails){
        final String userEmail = extractUserName(token);
        return (userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
