package hu.bme.cah.api.cardsagainsthumanityapi.user.service;

import hu.bme.cah.api.cardsagainsthumanityapi.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
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

        userService.saveAll(List.of(hlev, tumay, polya, czar, admin));
    }

    @Test
    void contextLoads() {
        assertThat(userService).isNotNull();
    }

    @Test
    void save() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@test.com");
        int beforeSave = userService.findAll().size();
        userService.save(user);
        int afterSave = userService.findAll().size();

        assertEquals(beforeSave+1,afterSave);
        assertTrue(userService.findAll().contains(user));
    }

    @Test
    void saveAll() {
        User user1 = new User();
        user1.setUsername("test1");
        User user2 = new User();
        user2.setUsername("test2");

        List<User> users = List.of(user1, user2);

        int beforeSave = userService.findAll().size();
        userService.saveAll(users);
        int afterSave = userService.findAll().size();

        assertEquals(beforeSave+ users.size(),afterSave);
        assertTrue(userService.findAll().contains(user1));
        assertTrue(userService.findAll().contains(user2));
    }

    @Test
    void getByUserId() {
        User hlev = userService.getByUserId("hlev");
        assertTrue("hlev".equals(hlev.getUsername()));
    }

    @Test
    void findAllByUserId() {
        List<String> usernames = List.of("hlev", "polya", "tumay");
        List<User> users = userService.findAllByUserId(usernames);
        assertEquals(usernames.size(), users.size());
        for (int i = 0; i < users.size(); i++)
            assertEquals(usernames.get(i), users.get(i).getUsername());
    }

    @Test
    void findAll() {
        List<User> allUsersBefore = userService.findAll();
        List<String> usernames = new ArrayList<>();
        for (User user : allUsersBefore) {
            usernames.add(user.getUsername());
            assertTrue(userService.findAll().contains(user));
        }
        userService.deleteUsersByUserId(usernames);
        List<User> allUsersAfter = userService.findAll();
        assertEquals(allUsersBefore.size(), allUsersAfter.size() + allUsersBefore.size());
    }

    @Test
    void deleteByUserId() {
        User hlev = userService.getByUserId("hlev");
        List<User> usersBefore = userService.findAll();
        int sizeBeforeDelete = usersBefore.size();
        userService.deleteByUserId("hlev");
        List<User> usersAfter = userService.findAll();
        int sizeAfterDelete = usersAfter.size();
        assertEquals(sizeBeforeDelete-1,sizeAfterDelete);
        assertTrue(usersBefore.contains(hlev));
        assertFalse(usersAfter.contains(hlev));
    }

    @Test
    void deleteUsersByUserId() {
        List<String> usernames = List.of("hlev", "polya", "tumay");
        List<User> usersBefore = userService.findAll();
        int sizeBeforeDelete = usersBefore.size();
        userService.deleteUsersByUserId(usernames);
        List<User> usersAfter = userService.findAll();
        int sizeAfterDelete = usersAfter.size();
        assertEquals(sizeBeforeDelete-usernames.size(),sizeAfterDelete);
    }

    @Test
    void updateAfterGame() {
        int point = 50;
        String rank = "bronze";
        User userBefore = userService.getByUserId("hlev");
        userService.updateAfterGame("hlev", point, rank);
        User userAfter = userService.getByUserId("hlev");
        assertEquals(userBefore.getPoint() + point, userAfter.getPoint());
        assertEquals(userBefore.getBronze() + 1, userAfter.getBronze());

        int point2 = 100;
        String rank2 = "silver";
        User userBefore2 = userService.getByUserId("tumay");
        userService.updateAfterGame("tumay", point2, rank2);
        User userAfter2 = userService.getByUserId("tumay");
        assertEquals(userBefore2.getPoint() + point2, userAfter2.getPoint());
        assertEquals(userBefore2.getSilver() + 1, userAfter2.getSilver());

        int point3 = 150;
        String rank3 = "gold";
        User userBefore3 = userService.getByUserId("tumay");
        userService.updateAfterGame("tumay", point3, rank3);
        User userAfter3 = userService.getByUserId("tumay");
        assertEquals(userBefore3.getPoint() + point3, userAfter3.getPoint());
        assertEquals(userBefore3.getGold() + 1, userAfter3.getGold());
        
    }
}