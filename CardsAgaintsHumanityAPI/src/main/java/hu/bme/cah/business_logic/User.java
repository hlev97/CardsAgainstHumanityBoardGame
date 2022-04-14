package hu.bme.cah.business_logic;

import hu.bme.cah.api.cardsagaintshumanityapi.domain.set.Black;
import hu.bme.cah.business_logic.Room;

import java.util.ArrayList;

public class User implements UserGameInterface{

    private Room currentRoom;
    private boolean isCzar;

    private Game game;

    private List<WhiteCard> ownedWhiteCards;
    private BlackCard blackCard;
    private List<List<WhiteCard>> pickedWhiteCards;

    public void setRoom(Room r)
    {
        currentRoom = r;
    }

    public boolean getIsCzar(){
        return  isCzar;
    }

    public boolean setGame(Game g)
    {
        game = g;
    }

    public void voteOnCard(int a)
    {
        game.vote(pickedWhiteCards[a]);     //valahogy így
    }

    public void pickWhiteCard(int a)
    {
        game.pickWhiteCard(ownedWhiteCards[a]);
    }


    @Override
    public void NotifyGameBegin(ArrayList<User> users, int rounds) {

    }

    @Override
    public void NotifyRoundStart(int round, Black blackCard) {

    }

    @Override
    public void NotifyVote() {

    }

    @Override
    public void NotifyGameEnd(User winner) {

    }

    @Override
    public void NotifyKicked() {

    }
}