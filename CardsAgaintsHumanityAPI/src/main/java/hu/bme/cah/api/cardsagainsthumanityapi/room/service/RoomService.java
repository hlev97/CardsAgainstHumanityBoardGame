package hu.bme.cah.api.cardsagainsthumanityapi.room.service;


import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.Black;
import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.White;
import hu.bme.cah.api.cardsagainsthumanityapi.card.repository.BlackRepository;
import hu.bme.cah.api.cardsagainsthumanityapi.card.repository.WhiteRepository;
import hu.bme.cah.api.cardsagainsthumanityapi.room.domain.GameState;
import hu.bme.cah.api.cardsagainsthumanityapi.room.domain.Room;
import hu.bme.cah.api.cardsagainsthumanityapi.room.repository.RoomRepository;
import hu.bme.cah.api.cardsagainsthumanityapi.user.domain.User;
import hu.bme.cah.api.cardsagainsthumanityapi.user.repository.UserRepository;
import hu.bme.cah.api.cardsagainsthumanityapi.util_di.bean.GenerateIdsBean;
import hu.bme.cah.api.cardsagainsthumanityapi.util_di.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import hu.bme.cah.api.cardsagainsthumanityapi.room.controller.RoomController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Ensures that {@link RoomController} can access {@link Room} through {@link RoomRepository}
 */
@Slf4j
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

    /**
     * Gets all room
     * @return rooms
     */
    public List<Room> list() {
        log.trace("In RoomService list() method");
        return roomRepository.list();
    }

    /**
     * Get room with given id
     * @param roomId id
     * @return room
     */
    public Room getByRoomId(String roomId) {
        log.trace("In RoomService getByRoomId(roomId) method");
        return roomRepository.findByRoomId(roomId);
    }

    /**
     * Save room
     * @param room room
     * @return saved room
     */
    public Room save(Room room) {
        log.trace("In RoomService save(room) method");
        return roomRepository.save(room);
    }

    /**
     * Save list of room
     * @param rooms list og room
     */
    public void save(List<Room> rooms) {
        log.trace("In RoomService save(rooms) method");
        roomRepository.save(rooms);
    }

    /**
     * Deletes room with given id
     * @param roomId roomId of removable room
     */
    public void removeByRoomId(String roomId) {
        log.trace("In RoomService removeByRoomId(roomId) method");
        roomRepository.removeByRoomId(roomId);
    }

    /**
     * Gets the size of the room's white card list
     * @return whiteList size
     */
    public int whitesSize() {
        log.trace("In RoomService whiteSize() method");
        return whiteRepository.findAll().size();
    }

    /**
     * Gets the size of the room's black card list
     * @return blackList size
     */
    public int blacksSize() {
        log.trace("In RoomService blackSize() method");
        return blackRepository.findAll().size();
    }

    /**
     * Get specific white card from room
     * @param whiteId white card id
     * @return white card
     */
    public White getByWhiteId(int whiteId) {
        log.trace("In RoomService getByWhiteId(whiteId) method");

        return whiteRepository.findByWhiteId(whiteId);
    }

    /**
     * Get specific black card from room
     * @param blackId black card id
     * @return black card
     */
    public Black getByBlackId(int blackId) {
        log.trace("In RoomService getByBlackId(blackId) method");
        return blackRepository.findByBlackId(blackId);
    }

    /**
     * Gets all white cards from room
     * @return whiteList
     */
    public List<White> listWhites() {
        log.trace("In RoomService listWhites method");
        return whiteRepository.findAll();
    }

    /**
     * Gets specific user from room
     * @param id username
     * @return user
     */
    public User getByUserId(String id) {
        log.trace("In RoomService getByUserId(id) method");
        return userRepository.getById(id);
    }

    /**
     * Gets specific users from room
     * @param userIds usernames
     * @return users
     */
    public List<User> findAllByUserIds(List<String> userIds) {
        log.trace("In RoomService findAllByUserIds(userIds) method");
        return userRepository.findAllById(userIds);
    }

    /**
     * Updates specific room's connected with user
     * @param roomId roomId
     * @param name username
     * @return room
     */
    public Room updateConnectedUsers(String roomId, String name) {
        log.trace("In RoomService updateConnectedUsers(roomId,name) method");
        return roomRepository.updateConnectedUsers(roomId, name);
    }

    /**
     * Removes a user from specific room's connected users
     * @param roomId roomId
     * @param name username
     * @return User
     */
    public Room deleteConnectedUsers(String roomId, String name) {
        log.trace("In RoomService deleteConnectedUsers(roomId,name) method");
        return roomRepository.deleteConnectedUsers(roomId, name);
    }

    /**
     * Creates room with czar
     * @param room room
     * @param czar czar's username
     * @return room
     */
    public Room createRoom(Room room, String czar) {
        log.trace("In RoomService createRoom(room,czar) method");
        room.setCzarId(czar);
        log.info("Czar is set");
        room.setConnectedUsers(new ArrayList<>());
        log.info("Connected users list is initialized");
        room.getConnectedUsers().add(0, czar);
        log.info("Czar is added to connected users list");
        Room savedRoom =  save(room);
        if (savedRoom == null) {
            log.error("Saved room is null");
        } else {
            log.info("Room saved successfully");
        }
        return savedRoom;
    }

    //TODO: ezt szerintem at kene emelni userService-be
    /**
     * Updates user's role with czar role
     * @param name
     * @param roleCzar
     * @return user
     */
    public User updateUserRoleById(String name, String roleCzar) {
        log.trace("In RoomService updateUserRoleById(name, roleCzar) method");
        User user = userRepository.findAllById(List.of(name)).get(0);
        log.info("User is found");
        user.getRoles().add(roleCzar);
        log.info("Czar role is added to user authorities");
        User updatedUser = userRepository.save(user);
        if (updatedUser == null) {
            log.error("Updated user is null");
        } else {
            log.info("User updated successfully");
        }
        return updatedUser;
    }

    /**
     * Initialize game of a specific room
     * @param roomId roomId
     * @return room
     */
    public Room initGame(String roomId) {
        log.trace("In RoomService initGame(roomId)");
        Room room = roomRepository.findByRoomId(roomId);
        int rounds = room.getRounds();
        int whiteSize = whitesSize();
        int blackSize = blacksSize();

        try(AnnotationConfigApplicationContext ctx =
                    new AnnotationConfigApplicationContext(AppConfig.class)) {
            log.trace("In RoomService try block: auth");
            log.info("Get bean for id generation");
            GenerateIdsBean gen = ctx.getBean(GenerateIdsBean.class);
            log.info("Generate white ids");
            List<Integer> whiteIds = gen.randomIds(whiteSize, rounds * room.getConnectedUsers().size() * 5);
            log.info("Initialize white ids with generated dataset");
            room.setWhiteIds(whiteIds);
            log.info("Generate black ids");
            List<Integer> blackIds = gen.randomIds(blackSize, rounds);
            log.info("Initialize black ids with generated dataset");
            room.setBlackIds(blackIds);
        }
        log.trace("Out of try block");
        Map<String, Integer> votes = new HashMap<String, Integer>();
        log.trace("In for cycle");
        for (String user: room.getConnectedUsers())
        {
            votes.put(user,0);
        }
        log.info("set user scores");
        room.setUserScores(votes);
        log.info("init user votes map");
        room.setUserVotes(new HashMap<>());
        log.info("start game");
        room.startGame();
        Room initGameRoom = roomRepository.initGame(roomId);
        if (initGameRoom == null) {
            log.error("Init was unsuccessful");
        } else {
            log.info("Init was successful");
        }
        return initGameRoom;
    }

    /**
     * Gets game state of a room
     * @param room room
     * @param name username
     * @return game state
     */
    public GameState getGameState(Room room, String name) {
        log.trace("In RoomService getGameState(room,name)");
        GameState gs = new GameState();
        List<Integer> whiteIds = room.getUserWhiteIds(name);
        List<White> whites = new ArrayList<White>();
        log.trace("In for cycle");
        for (int i: whiteIds)
        {
            whites.add(getByWhiteId(i));
        }
        log.info("Set turnState");
        gs.turnState = room.getTurnState();
        log.info("Set currentRound");
        gs.currentRound = room.getCurrentRound();
        log.info("Set allRound");
        gs.allRound = room.getRounds();
        log.info("Set black");
        gs.black = getByBlackId(room.BlackId());
        log.info("Set whites");
        gs.whites = whites;
        log.info("Set scores");
        gs.scores = room.getUserScores();

        Map<String, List<White>> chosenCards = new HashMap<String, List<White>>();
        Map<String, Integer> chosenIds = room.getUserChosen();
        log.trace("In for cycle");
        log.info("Init chosenCards collection");
        for (String namesWith_: chosenIds.keySet())
        {
            int indexOf_ = namesWith_.lastIndexOf("_");
            String namesWithout_ = namesWith_.substring(0, indexOf_);

            List<White> list = chosenCards.getOrDefault(namesWithout_, new ArrayList<White>());
            list.add(getByWhiteId(chosenIds.get(namesWith_)));
            chosenCards.put(namesWithout_, list);
        }
        log.info("Set chosenCards");
        gs.chosenCards = chosenCards;
        return gs;
    }

    /**
     * Updates votes of room
     * @param room room
     */
    public void updateVotes(Room room) {
        log.trace("In RoomService updateVotes(room) method");
        Map<String, Integer> numberOfVotes = room.getUserScores();

        Map<String, String> userVotes = room.getUserVotes();
        log.info("Init numberOfVotes");
        for(String key: userVotes.keySet()){
            int votes = numberOfVotes.getOrDefault(userVotes.get(key), 0);
            numberOfVotes.put(userVotes.get(key), votes + 1);
        }

    }
}
