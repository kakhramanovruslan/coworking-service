package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.aspects.AuditingAspect;
import org.example.dao.AuditDao;
import org.example.dao.BookingDao;
import org.example.dao.UserDao;
import org.example.dao.WorkspaceDao;
import org.example.dao.impl.AuditDaoImpl;
import org.example.dao.impl.BookingDaoImpl;
import org.example.dao.impl.UserDaoImpl;
import org.example.dao.impl.WorkspaceDaoImpl;
import org.example.liquibase.LiquibaseManager;
import org.example.service.*;
import org.example.utils.JwtTokenUtil;
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

    /**
     * Initializes the application context when the servlet context is initialized.
     *
     * @param sce the servlet context event
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();

        loadProperties(servletContext);
        databaseConfiguration(servletContext);
        serviceContextInit(servletContext);
        loadMappers(servletContext);
    }

    /**
     * Cleans up resources when the servlet context is destroyed.
     *
     * @param sce the servlet context event
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    /**
     * Loads application properties from a properties file.
     *
     * @param servletContext the servlet context
     */
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

    /**
     * Configures database connection and runs Liquibase migrations if enabled.
     *
     * @param servletContext the servlet context
     */
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

    /**
     * Initializes service layer components and sets them as servlet context attributes.
     *
     * @param servletContext the servlet context
     */
    private void serviceContextInit(ServletContext servletContext) {
        UserDao userDao = new UserDaoImpl(connectionManager);
        WorkspaceDao workspaceDao = new WorkspaceDaoImpl(connectionManager);
        BookingDao bookingDao = new BookingDaoImpl(connectionManager);
        AuditDao auditDao = new AuditDaoImpl(connectionManager);

        UserService userService = new UserService(userDao);
        WorkspaceService workspaceService = new WorkspaceService(workspaceDao);
        BookingService bookingService = new BookingService(userService, workspaceService, bookingDao);
        AuditService auditService = new AuditService(auditDao);

        AuditingAspect auditingAspect = new AuditingAspect();
        AuditingAspect.setAuditService(auditService);

        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(
                properties.getProperty("jwt.secret"),
                Duration.parse(properties.getProperty("jwt.lifetime")),
                userService
        );

        SecurityService securityService = new SecurityService(userDao, jwtTokenUtil);

        servletContext.setAttribute("jwtTokenUtils", jwtTokenUtil);
        servletContext.setAttribute("userService", userService);
        servletContext.setAttribute("auditService", auditService);
        servletContext.setAttribute("workspaceService", workspaceService);
        servletContext.setAttribute("bookingService", bookingService);
        servletContext.setAttribute("securityService", securityService);
        servletContext.setAttribute("auditingAspect", auditingAspect);
    }

    /**
     * Loads and configures object mappers, such as Jackson ObjectMapper.
     *
     * @param servletContext the servlet context
     */
    private void loadMappers(ServletContext servletContext) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        objectMapper.registerModule(module);
        servletContext.setAttribute("objectMapper", objectMapper);
    }
}