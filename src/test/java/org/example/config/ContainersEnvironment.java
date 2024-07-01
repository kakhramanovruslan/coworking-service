package org.example.config;

import org.example.containers.PostgresTestContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainersEnvironment {

    public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();

}