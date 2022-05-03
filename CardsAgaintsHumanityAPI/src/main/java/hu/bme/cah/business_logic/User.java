package hu.bme.cah.business_logic;

import hu.bme.cah.api.cardsagaintshumanityapi.domain.set.Black;
import hu.bme.cah.api.cardsagaintshumanityapi.domain.set.Set;
import hu.bme.cah.api.cardsagaintshumanityapi.domain.set.White;
import hu.bme.cah.business_logic.Room;

import java.util.ArrayList;
import java.util.List;

public class User implements UserGameInterface, UserRoomInterface{

    private Room currentRoom;
    private boolean isCzar;

    private Game game;

    public List<White> WhiteCards;
    /*private BlackCard blackCard;
    private List<List<WhiteCard>> pickedWhiteCards;*/

    public void setRoom(Room r)
    {
        currentRoom = r;
    }

    public boolean getIsCzar(){
        return  isCzar;
    }

    /*public boolean setGame(Game g)
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
    }*/


    @Override
    public void NotifyGameBegin(List<User> users, int rounds) {

    }

    @Override
    public void NotifyRoundStart(int round, Black blackCard) {

    }

    @Override
    public void NotifyVote() {

    }

    @Override
    public void NotifyGameEnd(List<User> scoreboard) {

    }

    @Override
    public void NotifyJoined() {

    }

    @Override
    public void NotifyLeft() {

    }

    @Override
    public void NotifyKicked() {

    }

    @Override
    public void NotifyRoundsSelected(int rounds) {

    }

    @Override
    public void NotifySetSelected(Set selectedset) {

    }

    @Override
    public void NotifyPlayerJoined(User player) {

    }

    @Override
    public void NotifyPlayerLeft(User player) {

    }
}