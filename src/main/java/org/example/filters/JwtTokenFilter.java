package org.example.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.dto.Authentication;
import org.example.dto.ExceptionResponse;
import org.example.exceptions.AuthenticationException;
import org.example.exceptions.UserNotFoundException;
import org.example.utils.JwtTokenUtil;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * A filter that intercepts all incoming HTTP requests and checks for the presence of a JWT in the Authorization header.
 * If a valid JWT is found, it authenticates the user and stores the authentication in the servlet context.
 * If no JWT is found or the JWT is invalid, it stores an unauthenticated Authentication object in the servlet context.
 */
@Component
@RequiredArgsConstructor
public class JwtTokenFilter implements Filter {

    private final JwtTokenUtil jwtTokenUtil;
    private final ObjectMapper objectMapper;

    private final ServletContext servletContext;
    private List<String> whiteList = List.of("/auth/", "/swagger-resources", "/swagger-ui", "/v2/api-docs");

    /**
     * Initializes the filter.
     *
     * @param config the filter configuration
     */
    @Override
    public void init(FilterConfig config) {
    }

    /**
     * Checks for the presence of a JWT in the Authorization header of the incoming request.
     * If a valid JWT is found, it authenticates the user and stores the authentication in the servlet context.
     * If no JWT is found or the JWT is invalid, it stores an unauthenticated Authentication object in the servlet context.
     *
     * @param servletRequest the incoming request
     * @param servletResponse the outgoing response
     * @param filterChain the filter chain
     * @throws IOException if an I/O error occurs during this filter's processing of the request
     * @throws ServletException if the processing fails for any other reason
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String path = httpRequest.getRequestURI();

        if (isPathOnWhiteList(path)) {
            filterChain.doFilter(servletRequest, servletResponse);
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

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }

    private boolean isPathOnWhiteList(String path){
        for (String p : whiteList) {
            if(path.startsWith(p)) { return true;}
        }
        return false;
    }
}