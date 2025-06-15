package br.com.pitarelo.api_todo.application.services;

import br.com.pitarelo.api_todo.domain.dto.login.TokenData;
import br.com.pitarelo.api_todo.domain.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.accessSecret}")
    private String accessSecret;

    public String generateAccessToken(User user) {
        try {
            Algorithm encriptAlgorithm = Algorithm.HMAC256(accessSecret);

            return JWT.create()
                    .withIssuer("TASK API")
                    .withClaim("id", user.getId())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60)) // 1h
                    .sign(encriptAlgorithm);

        } catch (JWTCreationException e) {
            throw new RuntimeException("Error to generate JWT acessToken", e);
        }
    }

    public TokenData getSubject(String jwt) {
        DecodedJWT decodedJWT;
        try {
            Algorithm encriptAlgorithm = Algorithm.HMAC256(accessSecret);

            JWTVerifier verifier = JWT.require(encriptAlgorithm).withIssuer("TASK API").build();

            decodedJWT = verifier.verify(jwt);

            Long id = decodedJWT.getClaim("id").asLong();
            String subject = decodedJWT.getSubject();

            return new TokenData(id, subject);
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("JWT token is invalid or are experienced");
        }
    }
}
