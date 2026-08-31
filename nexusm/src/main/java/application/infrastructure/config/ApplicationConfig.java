package application.infrastructure.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración general de la aplicación (reloj, beans de infraestructura).
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}