package hu.bme.cah.api.cardsagainsthumanityapi.room.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
@AllArgsConstructor
@Entity
public class Room {

    public static final String TURN_CHOOSING_CARDS = "TURN_CHOOSING_CARDS";
    public static final String TURN_VOTING = "TURN_VOTING";
    public static final String TURN_END_GAME = "TURN_END_GAME";


    private String roomId;

    public void setRoomId(String roomId) {
        log.trace("In setRoomId setter method");
        this.roomId = roomId;
    }

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    public String getRoomId() {
        log.trace("In getRoomId getter method");
        return roomId;
    }

    private List<Integer> whiteIds;
    private List<Integer> blackIds;

    //private List<String> allowedUsers;
    private List<String> connectedUsers;

    private String czarId;
    private int rounds;
    private int currentRound;
    //private boolean privateRoom;
    private boolean startedRoom;
    private String turnState;

    private Map<String, Integer> userScores;
    private Map<String, String> userVotes;


    private Map<String, Integer> userChosen;

    @ElementCollection
    @CollectionTable(
            name = "userChosen",
            joinColumns=@JoinColumn(name="roomId")
    )
    public Map<String, Integer> getUserChosen() {
        log.trace("In getUserChosen getter method");
        return userChosen;
    }

    public void setUserChosen(Map<String, Integer> userChosen) {
        this.userChosen = userChosen;
    }

    @ElementCollection
    @CollectionTable(
            name = "whites",
            joinColumns=@JoinColumn(name="roomId")
    )
    public List<Integer> getWhiteIds() {
        log.trace("In getWhiteIds getter method");
        return whiteIds;
    }

    public void setWhiteIds(List<Integer> whiteIds) {
        log.trace("In setWhiteIds setter method");
        this.whiteIds = whiteIds;
    }

    @ElementCollection
    @CollectionTable(
            name = "blacks",
            joinColumns=@JoinColumn(name="roomId")
    )
    public List<Integer> getBlackIds() {
        log.trace("In get blackIds getter method");
        return blackIds;
    }

    public void setBlackIds(List<Integer> blackIds) {
        log.trace("In set blackIds setter method");
        this.blackIds = blackIds;
    }

//    @ElementCollection
//    @CollectionTable(
//            name = "allowedUsers",
//            joinColumns=@JoinColumn(name="roomId")
//    )
//    public List<String> getAllowedUsers() {
//        return allowedUsers;
//    }
//
//    public void setAllowedUsers(List<String> userIds) {
//        this.allowedUsers = userIds;
//    }

    public String getCzarId() {
        log.trace("In getCzarId getter method");
        return czarId;
    }

    public void setCzarId(String czarId) {
        log.trace("In setCzarId setter method");
        this.czarId = czarId;
    }

    public int getRounds() {
        log.trace("In getRounds getter method");
        return rounds;
    }

    public void setRounds(int rounds) {
        log.trace("In setRounds setter method");
        this.rounds = rounds;
    }

    public int getCurrentRound() {
        log.trace("In getCurrentRounds getter method");
        return currentRound;
    }

    public void setCurrentRound(int round) {
        log.trace("In setCurrentRounds setter method");
        this.currentRound = round;
    }

    @ElementCollection
    @CollectionTable(
            name = "connectedUsers",
            joinColumns=@JoinColumn(name="roomId")
    )
    public List<String> getConnectedUsers() {
        log.trace("In getConnectedUsers getter method");
        return connectedUsers;
    }

    public void setConnectedUsers(List<String> connectedUsers) {
        log.trace("In setConnectedUsers setter method");
        this.connectedUsers = connectedUsers;
    }

    @ElementCollection
    @CollectionTable(
            name = "userStates",
            joinColumns=@JoinColumn(name="roomId")
    )
    public Map<String, Integer> getUserScores() {
        log.trace("In getUserScores getter method");
        return userScores;
    }

    public void setUserScores(Map<String, Integer> userStates) {
        this.userScores = userStates;
    }

    @ElementCollection
    @CollectionTable(
            name = "userVotes",
            joinColumns=@JoinColumn(name="roomId")
    )
    public Map<String, String> getUserVotes() {
        log.trace("In getUserVotes getter method");
        return userVotes;
    }

    public void setUserVotes(Map<String, String> userVotes) {
        log.trace("In setUserVotes setter method");
        this.userVotes = userVotes;
    }


//    public Map<String, List<White>> getUserWhiteCards() {
//        return userWhiteCards;
//    }
//    List<Integer> getWhiteCardsByUser(String userName)
//    {
//        return userWhiteCardIds.get(userName);
//    }
//
//    public void setUserWhiteCardIds(Map<String, List<Integer>> userWhiteCards) {
//        this.userWhiteCardIds = userWhiteCards;
//    }

//    public boolean isPrivateRoom() {
//        return privateRoom;
//    }
//
//    public void setPrivateRoom(boolean privateRoom) {
//        this.privateRoom = privateRoom;
//    }

    public boolean getStartedRoom() {
        log.trace("In getStartedRoom getter method");
        return startedRoom;
    }

    public void setStartedRoom(boolean started) {
        log.trace("In setStarted setter method");
        this.startedRoom = started;
    }

    public String getTurnState() {
        log.trace("In getTurnState getter method");
        return turnState;
    }

    public void setTurnState(String str) {
        log.trace("In setTurnState setter method");
        turnState = str;

    }

    public Room() {
        log.trace("In Room constructor");
    }

    public void startGame() {
        log.trace("In startGame method");
        setStartedRoom(true);
        currentRound = 1;
        setTurnState(TURN_CHOOSING_CARDS);

    }



    public List<Integer> getUserWhiteIds(String userName)
    {
        log.trace("In getUserWhiteIds");
        List<Integer> userWhiteCardIds = new ArrayList<Integer>();
        if (!connectedUsers.contains(userName)) {
            log.trace("In if branch");
            log.info("The user does not in the list of connected users");
            return userWhiteCardIds;
        }
        int numOfUsers = connectedUsers.size();
        int idx = connectedUsers.indexOf(userName);
        log.trace("In for cycle");
        log.info("Add white cards to user");
        for (int i = 0; i < 5; i++)
        {
            userWhiteCardIds.add(whiteIds.get((currentRound - 1) * numOfUsers * 5 + idx * 5 + i));
        }
        return userWhiteCardIds;
    }

    @Transient
    public int BlackId()
    {
        log.trace("In BlackId method");
        return blackIds.get(currentRound - 1);
    }
}
