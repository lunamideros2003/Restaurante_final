package co.ucc.pedidos.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Render expone la base de datos como un connectionString en formato
 * {@code postgresql://user:pass@host:port/db}. Spring requiere el prefijo
 * {@code jdbc:} para inicializar el DataSource, por lo que esta clase lo añade
 * en tiempo de arranque cuando hace falta.
 */
@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url:}")
    private String datasourceUrl;

    @Value("${spring.datasource.username:}")
    private String datasourceUsername;

    @Value("${spring.datasource.password:}")
    private String datasourcePassword;

    @Bean
    public DataSource dataSource() {
        String url = datasourceUrl;
        if (url != null && !url.isBlank() && !url.startsWith("jdbc:")) {
            url = "jdbc:" + url;
        }
        return DataSourceBuilder.create()
                .url(url)
                .username(datasourceUsername)
                .password(datasourcePassword)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
