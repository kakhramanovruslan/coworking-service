package org.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserDTO;
import org.example.dto.Authentication;
import org.example.exceptions.UserNotFoundException;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.time.Duration;
import java.util.Date;

/**
 * Utility class for handling JWT token operations.
 */
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    private String secret = "fdf203scdjkfkds9sdnfasb8ica12039r1mjkc1a";
    private String lifetime = "PT3H";
    private final UserService userService;

    /**
     * Generates a JWT for the given login.
     *
     * @param username the login for which to generate the JWT
     * @return the generated JWT
     */
    public String generateToken(String username){
        Date expirationDate = new Date(new Date().getTime() + Duration.parse(lifetime).toMillis());

        return JWT.create()
                .withSubject("user details")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer("ruslan")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * Authenticates a user based on the given JWT.
     *
     * @param token the JWT to authenticate
     * @return the authentication result
     * @throws AccessDeniedException if the JWT is invalid or the user does not exist
     */
    public Authentication authentication(String token) throws AccessDeniedException, UserNotFoundException {
        if (!validateToken(token)) {
            throw new AccessDeniedException("Access denied: Invalid token");
        }

        String username = getUsernameClaim(token);
        UserDTO user = userService.getUser(username);

        return new Authentication(username, user.getRole());
    }

    /**
     * Retrieves the username claim from the provided JWT token.
     *
     * @param token the JWT token from which to retrieve the username claim
     * @return the username claim extracted from the JWT token
     * @throws JWTVerificationException if the token verification fails
     */
    private String getUsernameClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("ruslan")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }

    /**
     * Validates the given JWT token.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("ruslan")
                    .build();

            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}