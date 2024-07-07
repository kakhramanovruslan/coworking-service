package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.dao.BookingDao;
import org.example.dao.UserDao;
import org.example.dao.WorkspaceDao;
import org.example.dao.impl.BookingDaoImpl;
import org.example.dao.impl.UserDaoImpl;
import org.example.dao.impl.WorkspaceDaoImpl;
import org.example.liquibase.LiquibaseManager;
import org.example.utils.JwtTokenUtil;
import org.example.service.BookingService;
import org.example.service.SecurityService;
import org.example.service.UserService;
import org.example.service.WorkspaceService;
import org.example.utils.ConnectionManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Properties;

/**
 * Application context for managing beans and dependencies.
 */
@WebListener
public class ApplicationContextListener implements ServletContextListener {

    private Properties properties;
    private ConnectionManager connectionManager;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();

        loadProperties(servletContext);
        databaseConfiguration(servletContext);
        serviceContextInit(servletContext);
        loadMappers(servletContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    private void loadProperties(ServletContext servletContext) {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(servletContext.getResourceAsStream("/WEB-INF/classes/application.properties"));
                servletContext.setAttribute("servletProperties", properties);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Property file not found!");
            } catch (IOException e) {
                throw new RuntimeException("Error reading configuration file: " + e.getMessage());
            }
        }
    }

    private void databaseConfiguration(ServletContext servletContext) {
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        String driver = properties.getProperty("db.driver-class-name");

        connectionManager = new ConnectionManager(url, username, password, driver);
        servletContext.setAttribute("connectionManager", connectionManager);

        if (Boolean.parseBoolean(properties.getProperty("liquibase.enabled"))) {
            LiquibaseManager liquibaseManager = LiquibaseManager.getInstance();
            liquibaseManager.runMigrations(connectionManager.getConnection());
            servletContext.setAttribute("liquibaseManager", liquibaseManager);
        }
    }

    private void serviceContextInit(ServletContext servletContext) {
        UserDao userDao = new UserDaoImpl(connectionManager);
        WorkspaceDao workspaceDao = new WorkspaceDaoImpl(connectionManager);
        BookingDao bookingDao = new BookingDaoImpl(connectionManager);

        UserService userService = new UserService(userDao);
        WorkspaceService workspaceService = new WorkspaceService(workspaceDao);
        BookingService bookingService = new BookingService(userService, workspaceService, bookingDao);

        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(
                properties.getProperty("jwt.secret"),
                Duration.parse(properties.getProperty("jwt.lifetime")),
                userService
        );

        SecurityService securityService = new SecurityService(userDao, jwtTokenUtil);

        servletContext.setAttribute("jwtTokenUtils", jwtTokenUtil);
        servletContext.setAttribute("userService", userService);
        servletContext.setAttribute("workspaceService", workspaceService);
        servletContext.setAttribute("bookingService", bookingService);
        servletContext.setAttribute("securityService", securityService);
    }

    private void loadMappers(ServletContext servletContext) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        objectMapper.registerModule(module);
        servletContext.setAttribute("objectMapper", objectMapper);
    }
}