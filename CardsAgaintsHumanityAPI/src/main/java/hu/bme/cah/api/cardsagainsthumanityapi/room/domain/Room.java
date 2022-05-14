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

/**
 * Represents a room
 */
@Slf4j
@Data
@AllArgsConstructor
@Entity
public class Room {
    /**
     * Game states
     */
    public static final String TURN_CHOOSING_CARDS = "TURN_CHOOSING_CARDS";
    public static final String TURN_VOTING = "TURN_VOTING";
    public static final String TURN_END_GAME = "TURN_END_GAME";

    /**
     * Room id
     */
    private String roomId;

    /**
     * Id setted
     * @param roomId id
     */
    public void setRoomId(String roomId) {
        log.trace("In setRoomId setter method");
        this.roomId = roomId;
    }

    /**
     * Id getter the rooms name is given by the creator and it mus be unique
     * @return id
     */
    @Id
    public String getRoomId() {
        log.trace("In getRoomId getter method");
        return roomId;
    }

    /**
     * Cards generated for room
     */
    private List<Integer> whiteIds;
    private List<Integer> blackIds;

    /**
     * Users connecred to the room
     */
    private List<String> connectedUsers;

    /**
     * The id of the czar
     */
    private String czarId;
    /**
     * Number of rounds tha users want to play
     */
    private int rounds;
    /**
     * The current round's number
     */
    private int currentRound;
    /**
     * Represents if the game has started or not
     */
    private boolean startedRoom;
    /**
     * Represents the game state
     */
    private String turnState;

    /**
     * A collection that stores (user, point) pairs
     */
    private Map<String, Integer> userScores;
    /**
     * A collection that stores (user, voted_user) pairs
     */
    private Map<String, String> userVotes;

    /**
     * A collection that stores (user, chosen_card) pairs
     */
    private Map<String, Integer> userChosen;

    /**
     * Getters and setters
     */

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



}
