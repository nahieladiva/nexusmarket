package application.infrastructure.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;

/**
 * Configuración de infraestructura de datos.
 *
 * <p>La conexión MySQL (JPA/Hibernate) y el cliente MongoDB se auto-configuran
 * a partir de las propiedades {@code spring.datasource.*} y
 * {@code spring.data.mongodb.*}. Aquí se personalizan beans de datos.</p>
 */
@Configuration
public class DatabaseConfig {

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "nexusmarket");
    }
}