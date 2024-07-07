package org.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.dto.Authentication;
import org.example.exceptions.UserNotFoundException;
import org.example.service.UserService;

import java.nio.file.AccessDeniedException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

/**
 * Utility class for handling JWT token operations.
 */
public class JwtTokenUtil {

    private final String secret;
    private final Duration jwtLifetime;
    private final UserService userService;

    /**
     * Constructor for JwtTokenUtils.
     *
     * @param secret the secret key for signing JWT
     * @param jwtLifetime the lifetime of the JWT
     * @param userService the user service for user operations
     */
    public JwtTokenUtil(String secret, Duration jwtLifetime, UserService userService) {
        this.secret = secret;
        this.jwtLifetime = jwtLifetime;
        this.userService = userService;
    }

    /**
     * Generates a JWT for the given login.
     *
     * @param username the login for which to generate the JWT
     * @return the generated JWT
     */

    public String generateToken(String username){
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

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

    public String getUsernameClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("ruslan")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }

    /**
     * Validates the given JWT.
     *
     * @param token the JWT to validate
     * @return true if the JWT is valid, false otherwise
     * @throws RuntimeException if the JWT is invalid
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