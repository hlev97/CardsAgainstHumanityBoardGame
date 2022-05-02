package hu.bme.cah.api.cardsagaintshumanityapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.Black;
import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.White;
import hu.bme.cah.api.cardsagaintshumanityapi.card.service.BlackService;
import hu.bme.cah.api.cardsagaintshumanityapi.card.service.WhiteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class CardsAgaintsHumanityApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardsAgaintsHumanityApiApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(WhiteService whiteService, BlackService blackService){
        return args -> {
            ObjectMapper mapperWhite = new ObjectMapper();
            TypeReference<List<White>> typeReferenceWhite = new TypeReference<>(){};
            InputStream inputStreamWhite = TypeReference.class.getResourceAsStream("/database/white.json");

            ObjectMapper mapperBlack = new ObjectMapper();
            TypeReference<List<Black>> typeReferenceBlack = new TypeReference<>(){};
            InputStream inputStreamBlack = TypeReference.class.getResourceAsStream("/database/black.json");

            List<White> whites = mapperWhite.readValue(inputStreamWhite,typeReferenceWhite);
            whiteService.save(whites);

            List<Black> blacks = mapperBlack.readValue(inputStreamBlack,typeReferenceBlack);
            blackService.save(blacks);
        };
    }
}
