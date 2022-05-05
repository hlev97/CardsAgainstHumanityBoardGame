package hu.bme.cah.api.cardsagaintshumanityapi.user.service;

import hu.bme.cah.api.cardsagaintshumanityapi.user.domain.User;
import hu.bme.cah.api.cardsagaintshumanityapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> saveAll(List<User> users) {
        return userRepository.saveAll(users);
    }

    //TODO: nem mukodik
    public User getByUserId(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User returnUser = user.get();
            returnUser.setPassword(null);
            return returnUser;
        } else throw new UsernameNotFoundException("Username not found");
    }

    public List<User> findAllByUserId(List<String> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        for (User user : users) {
            user.setPassword(null);
        }
        return users;
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setPassword(null);
        }
        return users;
    }

    public void deleteByUserId(String userId) {
        userRepository.deleteById(userId);
    }

    public void deleteUsersByUserId(List<String> userIds) {
        userRepository.deleteAllById(userIds);
    }

    public User update(String userId, int point, String rank) {
        User result = getByUserId(userId);
        double userPoint = result.getPoint();
        result.setPoint(userPoint+point);
        switch (rank.toLowerCase()) {
            case "gold":
                int gold = result.getGold();
                result.setGold(gold + 1);
                break;
            case "silver":
                int silver = result.getSilver();
                result.setGold(silver + 1);
                break;
            case "bronze":
                int bronze = result.getBronze();
                result.setGold(bronze + 1);
                break;
        }
        return userRepository.save(result);
    }
}
