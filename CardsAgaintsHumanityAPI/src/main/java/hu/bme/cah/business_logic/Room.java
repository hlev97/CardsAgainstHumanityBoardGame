package hu.bme.cah.business_logic;

import hu.bme.cah.api.cardsagaintshumanityapi.domain.set.Set;
import hu.bme.cah.business_logic.User;

import java.util.ArrayList;
import java.util.List;

public class Room implements RoomUserInterface{
    //todo no action authorization!
    private List<User> allowedUsers = null;
    private List<User> connectedUsers = new ArrayList<>();
    private Game game = null;
    private int rounds;
    private List<Set> selectedsets;

    @Override
    public void Join(User u) {
        if (allowedUsers == null || allowedUsers.contains(u))
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
        u.NotifyLeft();
        for(User user : connectedUsers)
            user.NotifyPlayerLeft(u);
    }

    @Override
    public void Kick(User player, User target) {
        if(connectedUsers.contains(target))
            connectedUsers.remove(target);
        target.NotifyKicked();
        for(User user : connectedUsers)
            user.NotifyPlayerLeft(user);
    }

    @Override
    public void SelectSet(Set s) {
        if(selectedsets.contains(s))
            selectedsets.remove(s);
        else
            selectedsets.add(s);
        for(User user : connectedUsers)
            user.NotifySetSelected(s);
    }

    @Override
    public void SelectRounds(int rounds) {
        this.rounds = rounds;
        for(User user : connectedUsers)
            user.NotifyRoundsSelected(rounds);
    }

    @Override
    public void StartGame() {
        game = new Game(connectedUsers, rounds, selectedsets);
    }
}