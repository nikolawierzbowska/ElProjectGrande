package pl.elgrandeproject.elgrande.security.jwt;

import lombok.Data;

@Data
public class RefreshTokenRequest {

    String token;
}
