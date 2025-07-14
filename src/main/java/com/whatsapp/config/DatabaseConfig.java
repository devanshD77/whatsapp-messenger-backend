package com.whatsapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DatabaseConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        DataSourceProperties properties = new DataSourceProperties();
        
        System.out.println("🔍 DatabaseConfig: Starting database configuration...");
        System.out.println("🔍 DATABASE_URL: " + (databaseUrl != null && !databaseUrl.isEmpty() ? 
            databaseUrl.substring(0, Math.min(databaseUrl.length(), 30)) + "..." : "NOT SET"));
        
        // Check if we have a Railway DATABASE_URL
        if (databaseUrl != null && !databaseUrl.isEmpty() && databaseUrl.startsWith("postgresql://")) {
            try {
                System.out.println("🔧 Parsing Railway DATABASE_URL...");
                
                URI uri = new URI(databaseUrl);
                String userInfo = uri.getUserInfo();
                
                if (userInfo == null || !userInfo.contains(":")) {
                    throw new IllegalArgumentException("Invalid DATABASE_URL format - missing user info");
                }
                
                String[] userPass = userInfo.split(":", 2);
                String username = userPass[0];
                String password = userPass[1];
                String host = uri.getHost();
                int port = uri.getPort();
                String database = uri.getPath().substring(1); // Remove leading slash
                
                String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, database);
                
                System.out.println("✅ Successfully parsed Railway DATABASE_URL");
                System.out.println("   Host: " + host + ":" + port);
                System.out.println("   Database: " + database);
                System.out.println("   Username: " + username);
                System.out.println("   JDBC URL: " + jdbcUrl);
                
                properties.setUrl(jdbcUrl);
                properties.setUsername(username);
                properties.setPassword(password);
                properties.setDriverClassName("org.postgresql.Driver");
                
            } catch (Exception e) {
                System.err.println("❌ Error parsing DATABASE_URL: " + e.getMessage());
                System.err.println("   DATABASE_URL: " + databaseUrl);
                System.err.println("   Falling back to default configuration");
                
                // Set default configuration for local development
                properties.setUrl("jdbc:postgresql://localhost:5432/whatsapp_chat");
                properties.setUsername("postgres");
                properties.setPassword("password");
                properties.setDriverClassName("org.postgresql.Driver");
            }
        } else if (databaseUrl != null && !databaseUrl.isEmpty() && databaseUrl.startsWith("jdbc:postgresql://")) {
            // Handle JDBC URL format (for local development)
            System.out.println("ℹ️  Using JDBC URL format for local development");
            properties.setUrl(databaseUrl);
            properties.setUsername("postgres");
            properties.setPassword("password");
            properties.setDriverClassName("org.postgresql.Driver");
        } else {
            // No DATABASE_URL provided, use defaults for local development
            System.out.println("ℹ️  No DATABASE_URL provided, using default local configuration");
            properties.setUrl("jdbc:postgresql://localhost:5432/whatsapp_chat");
            properties.setUsername("postgres");
            properties.setPassword("password");
            properties.setDriverClassName("org.postgresql.Driver");
        }
        
        System.out.println("✅ Database configuration complete");
        return properties;
    }

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }
} 