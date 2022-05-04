package hu.bme.cah.api.cardsagaintshumanityapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.Black;
import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.White;
import hu.bme.cah.api.cardsagaintshumanityapi.card.service.BlackService;
import hu.bme.cah.api.cardsagaintshumanityapi.card.service.WhiteService;
import hu.bme.cah.api.cardsagaintshumanityapi.user.domain.User;
import hu.bme.cah.api.cardsagaintshumanityapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class CardsAgaintsHumanityApiApplication implements CommandLineRunner {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setUsername("hlev");
        user.setPassword(passwordEncoder.encode("hlev"));
        user.setAccountLocked(false);
        user.setEnabled(true);
        user.setAccountExpired(false);
        user.setCredentialsExpired(false);
        user.setRoles(List.of("ROLE_USER"));

        User czar = new User();
        czar.setUsername("czar");
        czar.setPassword(passwordEncoder.encode("czar"));
        czar.setAccountLocked(false);
        czar.setEnabled(true);
        czar.setAccountExpired(false);
        czar.setCredentialsExpired(false);
        czar.setRoles(List.of("ROLE_CZAR", "ROLE_USER"));

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setAccountLocked(false);
        admin.setEnabled(true);
        admin.setAccountExpired(false);
        admin.setCredentialsExpired(false);
        admin.setRoles(List.of("ROLE_ADMIN", "ROLE_USER"));

        repository.saveAll(List.of(user, czar, admin));
    }
}
