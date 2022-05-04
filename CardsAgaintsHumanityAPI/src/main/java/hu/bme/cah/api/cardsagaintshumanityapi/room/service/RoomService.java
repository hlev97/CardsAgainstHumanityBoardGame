package hu.bme.cah.api.cardsagaintshumanityapi.room.service;

import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.Black;
import hu.bme.cah.api.cardsagaintshumanityapi.card.domain.White;
import hu.bme.cah.api.cardsagaintshumanityapi.card.repository.BlackRepository;
import hu.bme.cah.api.cardsagaintshumanityapi.card.repository.WhiteRepository;
import hu.bme.cah.api.cardsagaintshumanityapi.room.domain.Room;
import hu.bme.cah.api.cardsagaintshumanityapi.room.repository.RoomRepository;
import hu.bme.cah.api.cardsagaintshumanityapi.user.domain.User;
import hu.bme.cah.api.cardsagaintshumanityapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static hu.bme.cah.api.cardsagaintshumanityapi.util.Utils.getRandomIds;

@Service
public class RoomService {
    @Autowired
    private WhiteRepository whiteRepository;

    @Autowired
    private BlackRepository blackRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Room> list() {
        return roomRepository.list();
    }

    public Room getByRoomId(long roomId) {
        return roomRepository.findByRoomId(roomId);
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public void save(List<Room> rooms) {
        roomRepository.save(rooms);
    }

    public void removeByRoomId(long roomId) {
        roomRepository.removeByRoomId(roomId);
    }

    public int whitesSize() {
        return whiteRepository.findAll().size();
    }

    public int blacksSize() {
        return blackRepository.findAll().size();
    }

    public White getByWhiteId(int whiteId) {
        return whiteRepository.findByWhiteId(whiteId);
    }

    public Black getByBlackId(int blackId) {
        return blackRepository.findByBlackId(blackId);
    }

    public List<White> listWhites() {
        return whiteRepository.findAll();
    }

    public User getByUserId(List<String> id) {
        return userRepository.findAllById(id).get(0);
    }

    public List<User> findAllByUserIds(List<String> userIds) {
        return userRepository.findAllById(userIds);
    }

    public Room updateConnectedUsers(long roomId, String name) {
        return roomRepository.updateConnectedUsers(roomId, name);
    }

    public Room createRoom(Room room, String czar) {
        room.setRoomId(null);
        room.setCzarId(czar);
        room.getAllowedUsers().add(0, czar);
        room.setConnectedUsers(new ArrayList<>());
        room.getConnectedUsers().add(0, czar);
        int allowedUsersSize = room.getAllowedUsers().size();
        int rounds = room.getRounds();
        int whiteSize = whitesSize();
        int blackSize = blacksSize();
        List<Integer> whiteIds = getRandomIds(whiteSize, rounds * allowedUsersSize);
        room.setWhiteIds(whiteIds);
        List<Integer> blackIds = getRandomIds(blackSize, rounds);
        room.setBlackIds(blackIds);
        room.setUserStates(new HashMap<>());
        return save(room);
    }

    public User updateUserRoleById(String name, String roleCzar) {
        User user = userRepository.findAllById(List.of(name)).get(0);
        user.getRoles().add(roleCzar);
        return userRepository.save(user);
    }
}
