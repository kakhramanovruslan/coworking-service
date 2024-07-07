package org.example.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.Authentication;
import org.example.dto.ExceptionResponse;
import org.example.exceptions.UserNotFoundException;
import org.example.utils.JwtTokenUtil;

import java.io.IOException;

/**
 * A filter that intercepts all incoming HTTP requests and checks for the presence of a JWT in the Authorization header.
 * If a valid JWT is found, it authenticates the user and stores the authentication in the servlet context.
 * If no JWT is found or the JWT is invalid, it stores an unauthenticated Authentication object in the servlet context.
 */
@WebFilter(urlPatterns = "/*", initParams = @WebInitParam(name = "order", value = "1"))
public class JwtTokenFilter implements Filter {

    private JwtTokenUtil jwtTokenUtil;
    private ServletContext servletContext;
    private ObjectMapper objectMapper;

    /**
     * Initializes the filter.
     *
     * @param config the filter configuration
     */
    @Override
    public void init(FilterConfig config) {
        this.servletContext = config.getServletContext();
        jwtTokenUtil = (JwtTokenUtil) servletContext.getAttribute("jwtTokenUtils");
        objectMapper = (ObjectMapper) servletContext.getAttribute("objectMapper");
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

        if (path.equals("/auth/registration") || path.equals("/auth/login")) {
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
        } catch (RuntimeException e) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}