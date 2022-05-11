package hu.bme.cah.api.cardsagainsthumanityapi;

import hu.bme.cah.api.cardsagainsthumanityapi.email.service.EmailService;
import hu.bme.cah.api.cardsagainsthumanityapi.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CardsAgainstHumanityApiApplicationTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Test
    void contextLoads() {
        assertThat(userRepository).isNotNull();
        assertThat(passwordEncoder).isNotNull();
        assertThat(emailService).isNotNull();
    }

}
