package hu.bme.cah.api.cardsagainsthumanityapi.user.controller;

import hu.bme.cah.api.cardsagainsthumanityapi.user.domain.User;
import hu.bme.cah.api.cardsagainsthumanityapi.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    /**
     * Injection of {@link UserService}
     */
    @Autowired
    private UserService userService;

    /**
     * Injection of {@link PasswordEncoder}
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Getting the current user's details. The invoker needs at least USER_ROLE to be able to make this request.
     * @param principal principal
     * @return {@link ResponseEntity<Principal>}
     */
    @GetMapping("/me")
    @Secured(User.ROLE_USER)
    public ResponseEntity<Principal> userData(Principal principal) {
        log.trace("userData(principal) method is accessed");
        return new ResponseEntity<Principal>(principal, HttpStatus.OK);
    }

    /**
     * Add new user to database
     * @param user user
     * @return {@link User}
     */
    @PostMapping
    public User create(@RequestBody User user) throws Exception {
        log.trace("create(user) method is accessed");
        try {
            log.trace("In try block");
            log.info("Adding new user...");
            log.info("Encode given password...");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            log.info("The password was encoded successfully");

            log.info("Setting user's roles...");
            user.setRoles(List.of("ROLE_USER"));
            log.info("User's role successfully set");

            log.info("Enabling user's account...");
            user.setEnabled(true);
            log.info("User's account successfully enabled");
            return this.userService.save(user);
        } catch (Exception e) {
            log.trace("In catch block");
            log.error("The given email is invalid");
            throw new Exception("Invalid email");
        }
    }

    /**
     * Get users with given ids. The invoker must have a ROLE_ADMIN authority to be able to make this request.
     * @param userIds list of userIds
     * @return {@link List<User>} of {@link User}
     */
    @GetMapping("/list/{userIds}")
    @Secured(User.ROLE_ADMIN)
    public List<User> getUser(@PathVariable List<String> userIds) {
        log.trace("getUser(userIds) method is accessed");
        log.info("Getting user with given ids");
        return userService.findAllByUserId(userIds);
    }

    /**
     * Get user with given id. The invoker must have a ROLE_ADMIN authority to be able to make this request.
     * @param userId id
     * @return {@link User} with given id
     */
    @GetMapping("/user/{userId}")
    @Secured(User.ROLE_ADMIN)
    public User getUser(@PathVariable("userId") String userId) {
        log.trace("getUser(userId) method is accessed");
        log.info("Getting user with given id");
        return userService.getByUserId(userId);
    }

    /**
     * Get all users. The invoker must have a ROLE_ADMIN authority to be able to make this request.
     * @return {@link List<User>} of {@link User}
     */
    @GetMapping("/list")
    @Secured(User.ROLE_ADMIN)
    public List<User> getUsers() {
        log.trace("getUsers() method is accessed");
        log.info("Getting all user");
        return userService.findAll();
    }

    /**
     * Delete user with given id. The invoker must have a ROLE_ADMIN authority to be able to make this request.
     * @param userId id
     */
    @DeleteMapping("/user/{userId}")
    @Secured(User.ROLE_ADMIN)
    public void delete(@PathVariable("userId") String userId) {
        log.trace("delete(userId) method is accessed");
        log.info("Deleting user with given ids");
        userService.deleteByUserId(userId);
    }
}

