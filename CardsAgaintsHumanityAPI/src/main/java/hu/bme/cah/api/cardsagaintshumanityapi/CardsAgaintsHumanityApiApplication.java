package hu.bme.cah.api.cardsagaintshumanityapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.Black;
import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.White;
import hu.bme.cah.api.cardsagaintshumanityapi.card.service.BlackService;
import hu.bme.cah.api.cardsagaintshumanityapi.card.service.WhiteService;
import hu.bme.cah.api.cardsagaintshumanityapi.email.service.EmailService;
import hu.bme.cah.api.cardsagaintshumanityapi.user.domain.User;
import hu.bme.cah.api.cardsagaintshumanityapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
public class CardsAgaintsHumanityApiApplication implements CommandLineRunner {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

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
        User hlev = new User();
        hlev.setUsername("hlev");
        hlev.setPassword(passwordEncoder.encode("hlev"));
        hlev.setAccountLocked(false);
        hlev.setEnabled(true);
        hlev.setAccountExpired(false);
        hlev.setCredentialsExpired(false);
        hlev.setRoles(List.of("ROLE_USER"));

        User tumay = new User();
        tumay.setUsername("tumay");
        tumay.setPassword(passwordEncoder.encode("tumay"));
        tumay.setEmail("heizerlevente97@gmail.com");
        tumay.setAccountLocked(false);
        tumay.setEnabled(true);
        tumay.setAccountExpired(false);
        tumay.setCredentialsExpired(false);
        tumay.setRoles(List.of("ROLE_USER"));

        User polya = new User();
        polya.setUsername("polya");
        polya.setPassword(passwordEncoder.encode("polya"));
        polya.setEmail("heizerlevente97@gmail.com");
        polya.setAccountLocked(false);
        polya.setEnabled(true);
        polya.setAccountExpired(false);
        polya.setCredentialsExpired(false);
        polya.setRoles(List.of("ROLE_USER"));

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

        repository.saveAll(List.of(hlev, tumay, polya, czar, admin));
    }

    @EventListener(ApplicationReadyEvent.class)
    public void senMail() {
        emailService.sendEmail(
                "heizerlevente97@gmail.com",
                "The server is online...",
                "Hi Levi,\nThe Server is running..."
        );
    }

}
