package org.example.filters;



import javax.servlet.*;
import java.io.IOException;

/**
 * A global filter that sets the request character encoding to UTF-8 and the response content type to JSON with UTF-8 encoding.
 */
public class GlobalFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
