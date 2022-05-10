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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Room deleteConnectedUsers(long roomId, String name) {
        return roomRepository.deleteConnectedUsers(roomId, name);
    }

    public Room createRoom(Room room, String czar) {
        room.setRoomId(null);
        room.setCzarId(czar);
        room.setConnectedUsers(new ArrayList<>());
        room.getConnectedUsers().add(0, czar);

        return save(room);
    }

    public User updateUserRoleById(String name, String roleCzar) {
        User user = userRepository.findAllById(List.of(name)).get(0);
        user.getRoles().add(roleCzar);
        return userRepository.save(user);
    }

    public Room initGame(long roomId) {
        Room room = roomRepository.findByRoomId(roomId);
        int rounds = room.getRounds();
        int whiteSize = whitesSize();
        int blackSize = blacksSize();

        try(AnnotationConfigApplicationContext ctx =
                    new AnnotationConfigApplicationContext(AppConfig.class)) {
            GenerateIdsBean gen = ctx.getBean(GenerateIdsBean.class);
            List<Integer> whiteIds = gen.randomIds(whiteSize, rounds * room.getConnectedUsers().size() * 5);
            room.setWhiteIds(whiteIds);
            List<Integer> blackIds = gen.randomIds(blackSize, rounds);
            room.setBlackIds(blackIds);
        }

        Map<String, Integer> votes = new HashMap<String, Integer>();
        for (String user: room.getConnectedUsers())
        {
            votes.put(user,0);
        }

        room.setUserScores(votes);
        room.setUserVotes(new HashMap<>());
        room.startGame();
        return roomRepository.initGame(roomId);
    }

    public GameState getGameState(Room room, String name)
    {
        GameState gs = new GameState();
        List<Integer> whiteIds = room.getUserWhiteIds(name);
        List<White> whites = new ArrayList<White>();
        for (int i: whiteIds)
        {
            whites.add(getByWhiteId(i));
        }

        gs.turnState = room.getTurnState();
        gs.currentRound = room.getCurrentRound();
        gs.allRound = room.getRounds();
        gs.black = getByBlackId(room.BlackId());
        gs.whites = whites;
        gs.scores = room.getUserScores();

        Map<String, List<White>> chosenCards = new HashMap<String, List<White>>();
        Map<String, Integer> chosenIds = room.getUserChosen();

        for (String namesWith_: chosenIds.keySet())
        {
            int indexOf_ = namesWith_.lastIndexOf("_");
            String namesWithout_ = namesWith_.substring(0, indexOf_);

            List<White> list = chosenCards.getOrDefault(namesWithout_, new ArrayList<White>());
            list.add(getByWhiteId(chosenIds.get(namesWith_)));
            chosenCards.put(namesWithout_, list);
        }
        gs.chosenCards = chosenCards;
        return gs;
    }

    public void updateVotes(Room room)
    {
        Map<String, Integer> numberOfVotes = room.getUserScores();

        Map<String, String> userVotes = room.getUserVotes();
        for(String key: userVotes.keySet()){
            int votes = numberOfVotes.getOrDefault(userVotes.get(key), 0);
            numberOfVotes.put(userVotes.get(key), votes + 1);
        }

    }

}
