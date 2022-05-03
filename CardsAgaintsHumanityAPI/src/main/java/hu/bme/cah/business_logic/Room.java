package hu.bme.cah.business_logic;

import hu.bme.cah.api.cardsagaintshumanityapi.domain.set.Set;
import hu.bme.cah.business_logic.User;

import java.util.ArrayList;
import java.util.List;

public class Room implements RoomUserInterface{

    private List<User> allowedUsers = null;
    private List<User> connectedUsers = new ArrayList<>();
    private Game game = null;
    private int rounds = 1;
    private User czar;
    private boolean priv;
    private List<Set> selectedsets = new ArrayList<>();

    private Room(User czar, boolean isprivate){
        this.czar = czar;
        priv = isprivate;
    }

    public static Room CreatePublicRoom(User czar){
        return new Room(czar, false);
    }

    public static Room CreatePrivateRoom(User czar){
        Room r = new Room(czar, true);
        r.allowedUsers = new ArrayList<>();
        return r;
    }

    @Override
    public void Join(User u) {
        if (!priv || allowedUsers.contains(u))
        {
            u.setRoom(this);
            if (!connectedUsers.contains(u))
                connectedUsers.add(u);
            u.NotifyJoined();
            for(User user : connectedUsers)
                user.NotifyPlayerJoined(u);
        }
    }

    @Override
    public void Leave(User u) {
        if (connectedUsers.contains(u))
            connectedUsers.remove(u);
        if(czar == u){
            czar = connectedUsers.get(0);
        }
        u.NotifyLeft();
        for(User user : connectedUsers)
            user.NotifyPlayerLeft(u);
    }

    @Override
    public void Kick(User player, User target) {
        if(player != czar)
            return;
        if(connectedUsers.contains(target))
            connectedUsers.remove(target);
        if(czar == target){
            czar = connectedUsers.get(0);
        }
        target.NotifyKicked();
        for(User user : connectedUsers)
            user.NotifyPlayerLeft(user);
        if(priv)
            allowedUsers.remove(target);
    }

    @Override
    public void SelectSet(User player, Set s) {
        if(player != czar)
            return;
        if(selectedsets.contains(s))
            selectedsets.remove(s);
        else
            selectedsets.add(s);
        for(User user : connectedUsers)
            user.NotifySetSelected(s);
    }

    @Override
    public void SelectRounds(User player, int rounds) {
        if(player != czar)
            return;
        this.rounds = rounds;
        for(User user : connectedUsers)
            user.NotifyRoundsSelected(rounds);
    }

    @Override
    public void StartGame(User player) {
        if(player != czar || game != null)
            return;
        game = new Game(connectedUsers, rounds, selectedsets, this);
    }

    @Override
    public void InviteUser(User player, User target) {
        if(player != czar)
            return;
        //todo email invite logic
        allowedUsers.add(target);
    }

    public void GameEnded(){
        game = null;
    }
}