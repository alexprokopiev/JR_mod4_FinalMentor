package config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class PostgresqlSessionFactory {
    public static SessionFactory createSessionFactory(String url, String driver, String username, String password) {
        return new Configuration()
                .setProperty("hbm2ddl", "validate")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.format_sql", "true")
                .setProperty("hibernate.highlight_sql", "true")
                .setProperty("hibernate.connection.password", password)
                .setProperty("hibernate.connection.username", username)
                .setProperty("hibernate.current_session_context_class", "thread")
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .setProperty("hibernate.connection.driver_class", driver)
                .setProperty("hibernate.connection.url", url)
                .addAnnotatedClass(model.entity.TagEntity.class)
                .addAnnotatedClass(model.entity.TaskEntity.class)
                .addAnnotatedClass(model.entity.UserEntity.class)
                .addAnnotatedClass(model.entity.CommentEntity.class)
                .buildSessionFactory();
    }
}
