package hu.bme.cah.business_logic;

import hu.bme.cah.business_logic.Room;

public class User {

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


}