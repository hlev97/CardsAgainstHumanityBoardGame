package hu.bme.cah.api.cardsagainsthumanityapi.room.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Entity
public class Room {

    public static final String TURN_CHOOSING_CARDS = "TURN_CHOOSING_CARDS";
    public static final String TURN_VOTING = "TURN_VOTING";

    private Long roomId;

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getRoomId() {
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

    @ElementCollection
    @CollectionTable(
            name = "whites",
            joinColumns=@JoinColumn(name="roomId")
    )
    public List<Integer> getWhiteIds() {
        return whiteIds;
    }

    public void setWhiteIds(List<Integer> whiteIds) {
        this.whiteIds = whiteIds;
    }

    @ElementCollection
    @CollectionTable(
            name = "blacks",
            joinColumns=@JoinColumn(name="roomId")
    )
    public List<Integer> getBlackIds() {
        return blackIds;
    }

    public void setBlackIds(List<Integer> blackIds) {
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
        return czarId;
    }

    public void setCzarId(String czarId) {
        this.czarId = czarId;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int round) {
        this.currentRound = round;
    }

    @ElementCollection
    @CollectionTable(
            name = "connectedUsers",
            joinColumns=@JoinColumn(name="roomId")
    )
    public List<String> getConnectedUsers() {
        return connectedUsers;
    }

    public void setConnectedUsers(List<String> connectedUsers) {
        this.connectedUsers = connectedUsers;
    }

    @ElementCollection
    @CollectionTable(
            name = "userStates",
            joinColumns=@JoinColumn(name="roomId")
    )
    public Map<String, Integer> getUserScores() {
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
        return userVotes;
    }

    public void setUserVotes(Map<String, String> userVotes) {
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
        return startedRoom;
    }

    public void setStartedRoom(boolean started) {
        this.startedRoom = started;
    }

    public String getTurnState() {
        return turnState;
    }

    public void setTurnState(String str) {
        turnState = str;
//        if (str.equals(TURN_CHOOSING_CARDS))
//            turnState = TURN_CHOOSING_CARDS;
//        else if (str.equals(TURN_VOTING))
//            turnState = TURN_VOTING;
//hibát dob
    }

    public Room() {
    }

    public void startGame() {
        setStartedRoom(true);
        currentRound = 0;
        initTurnState();

        //TODO: játék logika
    }

    public void initTurnState(){
        setTurnState(TURN_CHOOSING_CARDS);
        currentRound++;
    }

    public List<Integer> getUserWhiteIds(String userName)
    {
        List<Integer> userWhiteCardIds = new ArrayList<Integer>();
        if (!connectedUsers.contains(userName))
            return userWhiteCardIds;

        int numOfUsers = connectedUsers.size();
        int idx = connectedUsers.indexOf(userName);
        for (int i = 0; i < 5; i++)
        {
            userWhiteCardIds.add(whiteIds.get(currentRound * numOfUsers * 5 + idx * 5 + i));
        }
        return userWhiteCardIds;
    }

    @Transient
    public int BlackId()
    {
        return blackIds.get(currentRound);
    }
}
