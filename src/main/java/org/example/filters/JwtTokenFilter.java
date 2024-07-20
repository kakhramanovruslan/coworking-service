package org.example.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.dto.Authentication;
import org.example.dto.ExceptionResponse;
import org.example.exceptions.AuthenticationException;
import org.example.exceptions.UserNotFoundException;
import org.example.filters.config.Routing;
import org.example.utils.JwtTokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * A filter that intercepts all incoming HTTP requests and checks for the presence of a JWT in the Authorization header.
 * If a valid JWT is found, it authenticates the user and stores the authentication in the servlet context.
 * If no JWT is found or the JWT is invalid, it stores an unauthenticated Authentication object in the servlet context.
 */
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final ObjectMapper objectMapper;

    private final ServletContext servletContext;

    /**
     * Checks for the presence of a JWT in the Authorization header of the incoming request.
     * If a valid JWT is found, it authenticates the user and stores the authentication in the servlet context.
     * If no JWT is found or the JWT is invalid, it stores an unauthenticated Authentication object in the servlet context.
     *
     * @param httpRequest the incoming request
     * @param httpRequest the outgoing response
     * @param filterChain the filter chain
     * @throws IOException if an I/O error occurs during this filter's processing of the request
     * @throws ServletException if the processing fails for any other reason
     */
    @Override
    public void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain) throws IOException, ServletException {

        String path = httpRequest.getRequestURI();

        if (Routing.isPathOnWhiteList(path)) {
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }

        String bearerToken = httpRequest.getHeader("Authorization");
        try {
            if (bearerToken != null && bearerToken.startsWith("Bearer ") && jwtTokenUtil.validateToken(bearerToken.substring(7))) {
                Authentication authentication = jwtTokenUtil.authentication(bearerToken.substring(7));
                servletContext.setAttribute("authentication", authentication);
            } else {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } catch (UserNotFoundException e) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(httpResponse.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (AuthenticationException e) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (RuntimeException e) {
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        filterChain.doFilter(httpRequest, httpResponse);
    }

}