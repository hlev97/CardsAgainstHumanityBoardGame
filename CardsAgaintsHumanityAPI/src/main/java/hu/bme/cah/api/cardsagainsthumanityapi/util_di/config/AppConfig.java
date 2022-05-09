package hu.bme.cah.api.cardsagainsthumanityapi.util_di.config;

import hu.bme.cah.api.cardsagainsthumanityapi.util_di.generate_ids.GenerateIds;
import hu.bme.cah.api.cardsagainsthumanityapi.util_di.generate_ids.GenerateRandomIdsSequence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Application Configuration
 */
@Configuration
@ComponentScan("hu.bme.cah.api.cardsagainsthumanityapi.util_di")
public class AppConfig {
    /**
     * GetMethod()
     * @return generation method of choice
     */
    @Bean
    public GenerateIds getMethod() {
        return new GenerateRandomIdsSequence();
    }
}
