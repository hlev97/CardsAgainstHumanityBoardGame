package hu.bme.cah.api.cardsagaintshumanityapi.user.controller;

import hu.bme.cah.api.cardsagaintshumanityapi.user.domain.User;
import hu.bme.cah.api.cardsagaintshumanityapi.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users") //Ha Postmannel tesztelsz emiatt lehetnek hibak a /me /hello /czar_hello-nal
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/me")
    @Secured(User.ROLE_USER)
    public ResponseEntity<Principal> userData(Principal principal) {
        return new ResponseEntity<Principal>(principal, HttpStatus.OK);
    }

    @GetMapping("/hello")
    @Secured(User.ROLE_USER)
    public String authHello(Principal principal) {
        return "Hi, " + principal.getName();
    }

    @GetMapping("/czar_hello")
    @Secured(User.ROLE_CZAR)
    public String adminHello(Principal principal) {
        return "Hi, " + principal.getName() + ". You are the czar.";
    }

    @PostMapping
    public User create(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.userService.save(user);
    }

    @GetMapping("/{userIds}")
    @Secured(User.ROLE_ADMIN)
    public List<User> getUser(@PathVariable List<String> userIds) {

        return userService.findAllByUserId(userIds);
    }

    //TODO: nem mukodik
    @GetMapping("/{userId}")
    @Secured(User.ROLE_ADMIN)
    public User getUser(@PathVariable("userId") String userId) {
        return userService.getByUserId(userId);
    }

    @GetMapping
    @Secured(User.ROLE_ADMIN)
    public List<User> getUsers() {
        return userService.findAll();
    }

    @DeleteMapping("/{userId}")
    @Secured(User.ROLE_ADMIN)
    public void delete(@PathVariable("userId") String userId) {
        userService.deleteByUserId(userId);
    }
}

