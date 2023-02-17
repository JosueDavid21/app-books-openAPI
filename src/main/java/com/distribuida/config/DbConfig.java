package com.distribuida.config;

import com.zaxxer.hikari.HikariDataSource;
import io.helidon.config.Config;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import javax.sql.DataSource;

@ApplicationScoped
public class DbConfig {
    @Inject
    @ConfigProperty(name="db.connection.username")
    private String dbUser;

    @Inject
    @ConfigProperty(name="db.connection.password")
    private String dbPassword;

    @Inject
    @ConfigProperty(name="db.connection.url")
    private String dbUrl;

    @Produces
    @ApplicationScoped
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();

        ds.setDriverClassName("org.postgresql.Driver");
        ds.setJdbcUrl(dbUrl);
        ds.setUsername(dbUser);
        ds.setPassword(dbPassword);
        return ds;
    }
//    @Produces
//    @ApplicationScoped
//    public DbClient dbClent() {
//
//        var pool = ConnectionPool.builder()
//                .username(dbUser)
//                .password(dbPassword)
//                .url( dbUrl )
//                .build();
//
//        return JdbcDbClientProviderBuilder
//                .create()
//                .connectionPool(pool)
//                .build();
//    }
    @Produces
    @ApplicationScoped
    public EntityManager entityManager() {
        return Persistence
                .createEntityManagerFactory("books")
                .createEntityManager();
    }
}
