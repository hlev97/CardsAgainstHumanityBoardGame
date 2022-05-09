package hu.bme.cah.api.cardsagainsthumanityapi.user.service;

import hu.bme.cah.api.cardsagainsthumanityapi.user.domain.User;
import hu.bme.cah.api.cardsagainsthumanityapi.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import hu.bme.cah.api.cardsagainsthumanityapi.user.controller.UserController;

import java.util.List;
import java.util.Optional;

/**
 * Ensures that {@link UserController} can access {@link User} through {@link UserRepository}
 */
@Service
@Slf4j
public class UserService {
    /**
     * Injection of {@link UserRepository}
     */
    @Autowired
    UserRepository userRepository;

    /**
     * Save given user
     * @param user user
     * @return {@link User}
     */
    public User save(User user) {
        log.trace("save(user) method is accessed");
        User savedUser = userRepository.save(user);
        log.info("Saving the user was successful");
        return savedUser;
    }

    /**
     * Save all given users
     * @param users
     * @return {@link List} of {@link User}
     */
    public List<User> saveAll(List<User> users) {
        log.trace("saveAll(users) method is accessed");
        List<User> savedUsers = userRepository.saveAll(users);
        log.info("Saving the users was successful");
        return savedUsers;
    }

    /**
     * Getting user by id
     * @param userId
     * @return {@link User}
     */
    public User getByUserId(String userId) {
        log.trace("getByUserId(userId) method is accessed");
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            log.trace("in if block: user is present");
            log.info("User is present");
            User returnUser = user.get();
            returnUser.setPassword(null);
            return returnUser;
        } else {
            log.trace("in else block: user is not present");
            log.error("User with given id was not found");
            throw new UsernameNotFoundException("Username not found");
        }
    }

    /**
     * Get all user with given ids
     * @param userIds ids
     * @return {@link List<User>} of {@link User}
     */
    public List<User> findAllByUserId(List<String> userIds) {
        log.trace("findAllByUserId(userIds) method is accessed");
        List<User> users = userRepository.findAllById(userIds);
        log.info("All user with given ids were found");
        for (User user : users) {
            user.setPassword(null);
        }
        return users;
    }

    /**
     * Get all users
     * @return {@link List<User>} of {@link User}
     */
    public List<User> findAll() {
        log.trace("findAll() method is accessed");
        List<User> users = userRepository.findAll();
        log.info("All user were found");
        for (User user : users) {
            user.setPassword(null);
        }
        return users;
    }

    /**
     * Delete user with given id
     * @param userId id
     */
    public void deleteByUserId(String userId) {
        log.trace("deleteByUserId(userId) method is accessed");
        userRepository.deleteById(userId);
        log.info("User with given id was deleted successfully");
    }

    /**
     * Delete all users with given ids
     * @param userIds
     */
    public void deleteUsersByUserId(List<String> userIds) {
        log.trace("deleteUsersByUserId(userIds) method is accessed");
        userRepository.deleteAllById(userIds);
        log.info("Users with given ids were deleted successfully");
    }

    /**
     * update user after game
     * @param userId user
     * @param point collected points
     * @param rank earned rank
     * @return {@link User}
     */
    public User updateAfterGame(String userId, int point, String rank) {
        log.trace("updateAfterGame(userId,point,rank) method is accessed");
        log.info("Updating user after finishing the game");
        User result = getByUserId(userId);
        int userPoint = result.getPoint();
        result.setPoint(userPoint+point);
        switch (rank.toLowerCase()) {
            case "gold":
                log.trace("In gold case block");
                int gold = result.getGold();
                result.setGold(gold + 1);
                break;
            case "silver":
                log.trace("In silver case block");
                int silver = result.getSilver();
                result.setSilver(silver + 1);
                break;
            case "bronze":
                log.trace("In bronze case block");
                int bronze = result.getBronze();
                result.setBronze(bronze + 1);
                break;
        }
        User updatedUser = userRepository.save(result);
        log.info("User with given id was updated successfully after finishing the game");
        return updatedUser;
    }
}
