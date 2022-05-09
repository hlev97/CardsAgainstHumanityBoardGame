package hu.bme.cah.api.cardsagainsthumanityapi;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.Black;
import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.White;
import hu.bme.cah.api.cardsagainsthumanityapi.card.service.BlackService;
import hu.bme.cah.api.cardsagainsthumanityapi.card.service.WhiteService;
import hu.bme.cah.api.cardsagainsthumanityapi.email.service.EmailService;
import hu.bme.cah.api.cardsagainsthumanityapi.log.domain.Log;
import hu.bme.cah.api.cardsagainsthumanityapi.log.service.LogService;
import hu.bme.cah.api.cardsagainsthumanityapi.user.domain.User;
import hu.bme.cah.api.cardsagainsthumanityapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.InputStream;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class CardsAgainstHumanityApiApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public static void main(String[] args) {
        SpringApplication.run(CardsAgainstHumanityApiApplication.class, args);
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
        hlev.setEmail("heizerlevente97@gmail.com");
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

        userRepository.saveAll(List.of(hlev, tumay, polya, czar, admin));
    }

    @EventListener(ApplicationReadyEvent.class)
    public void sendMail() {
        emailService.sendEmail(
                "heizerlevente97@gmail.com",
                "The server is online...",
                "Hi Levi,\nThe Server is running..."
        );
    }



    private static final String RESET_COLOR = "\u001B[0m";
    private static final String INFO_COLOR = "\u001B[32m";
    private static final String ERROR_COLOR = "\u001B[31m";
    private static final String WARN_COLOR = "\u001B[33m";
    private static final String TRACE_COLOR = "\u001B[34m";
    private static final String DEBUG_COLOR = "\u001B[36m";

    @Autowired
    private LogService logService;

    @Scheduled(fixedRate=60*1000)
    public void writeToConsole() {
        List<Log> logs = logService.getLogs();
        for (Log log : logs) {
            switch (log.getLevel()) {
                case INFO:
                    System.out.println("Date: " + log.getDate().toString() + INFO_COLOR + "\tINFO\t" + RESET_COLOR + log.getLog());
                    break;
                case TRACE:
                    System.out.println("Date: " + log.getDate().toString() + TRACE_COLOR + "\tTRACE\t" + RESET_COLOR + log.getLog());
                    break;
                case WARN:
                    System.out.println("Date: " + log.getDate().toString() + WARN_COLOR + "\tWARN\t" + RESET_COLOR + log.getLog());
                    break;
                case DEBUG:
                    System.out.println("Date: " + log.getDate().toString() + DEBUG_COLOR + "\tDEBUG\t" + RESET_COLOR + log.getLog());
                    break;
                case ERROR:
                    System.out.println("Date: " + log.getDate().toString() + ERROR_COLOR + "\tERROR\t" + RESET_COLOR + log.getLog());
                    break;
            }
        }
    }


    @Scheduled(fixedRate=24*60*1000)
    public void sendMailDaily() {
        emailService.sendEmail(
                "heizerlevente97@gmail.com",
                "Come play with your friends",
                "Hi Levi,\nCome play with your friends..."
        );
    }

    @Scheduled(fixedRate=7*24*60*1000)
    public void sendMailWeekly() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getEmail()!=null) {
                emailService.sendEmail(
                        user.getEmail(),
                        "Your weekly stats",
                        "Hi buddy,"
                );
            }
        }
    }
}
