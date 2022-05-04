package hu.bme.cah.api.cardsagaintshumanityapi.room.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Entity
public class Room {

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

    private List<String> allowedUsers;
    private List<String> connectedUsers;

    private String czarId;
    private int rounds;
    private boolean privateRoom;

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

    @ElementCollection
    @CollectionTable(
            name = "allowedUsers",
            joinColumns=@JoinColumn(name="roomId")
    )
    public List<String> getAllowedUsers() {
        return allowedUsers;
    }

    public void setAllowedUsers(List<String> userIds) {
        this.allowedUsers = userIds;
    }

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

    public boolean isPrivateRoom() {
        return privateRoom;
    }

    public void setPrivateRoom(boolean privateRoom) {
        this.privateRoom = privateRoom;
    }

    public Room() {
    }
}
