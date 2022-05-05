package hu.bme.cah.api.cardsagaintshumanityapi.util_di.config;

import hu.bme.cah.api.cardsagaintshumanityapi.util_di.generate_ids.GenerateIds;
import hu.bme.cah.api.cardsagaintshumanityapi.util_di.generate_ids.GenerateRandomIdsSequence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("hu.bme.cah.api.cardsagaintshumanityapi.util_di")
public class AppConfig {
    @Bean
    public GenerateIds getMethod() {
        return new GenerateRandomIdsSequence();
    }
}
