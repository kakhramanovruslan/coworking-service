package org.example.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.example.filters.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


/**
 * Configuration class for initializing and configuring a Spring MVC web application.
 */
@Configuration
@EnableWebMvc
//@EnableAspectJAutoProxy
@ComponentScan("org.example")
public class ApplicationConfig implements WebApplicationInitializer {

    /**
     * Initializes the Spring application context and configures the DispatcherServlet.
     *
     * @param servletContext the ServletContext used to register listeners and servlets
     * @throws ServletException if any Servlet-specific initialization tasks fail
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(ApplicationConfig.class);

        servletContext.addListener(new ContextLoaderListener(context));

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        FilterRegistration.Dynamic jwtTokenFilter = servletContext.addFilter("jwtTokenFilter", new DelegatingFilterProxy("jwtTokenFilter"));
        jwtTokenFilter.addMappingForUrlPatterns(null, false, "/*");
        jwtTokenFilter.setInitParameter("order", "1");

        FilterRegistration.Dynamic globalFilter = servletContext.addFilter("globalFilter", GlobalFilter.class);
        globalFilter.addMappingForUrlPatterns(null, false, "/*");
        globalFilter.setInitParameter("order", "2");
    }

    /**
     * Configures the ObjectMapper bean.
     *
     * @return the ObjectMapper bean
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
